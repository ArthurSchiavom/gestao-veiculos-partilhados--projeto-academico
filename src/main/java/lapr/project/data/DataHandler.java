package lapr.project.data;


import lapr.project.data.registers.Company;
import lapr.project.utils.Updateable;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exemplo de classe cujas instâncias manipulam dados de BD Oracle.
 */
public class DataHandler {
    private static final Logger LOGGER = Logger.getLogger("DataHandlerLog");
    private static final int MAX_RECONNECTION_ATTEMPTS_RECOVERABLE = 3;
    private static final int MAX_RECONNECTION_ATTEMPTS_UNRECOVERABLE = 10;
    private static final int CONNECTION_FAILURE_ORA_CODE = 17008;
    private static final int RECONNECTION_INTERVAL_MILLIS = 3000;
    private static final int QUERY_TIMEOUT_SECONDS = 10;
    private static final String NOT_CONNECTED_ERROR_MSG = "Not connected to the database";

    private static Updateable<Boolean> created = new Updateable<>(false);


    /**
     * O URL da BD.
     */
    private final String jdbcUrl;

    /**
     * O nome de utilizador da BD.
     */
    private final String username;

    /**
     * A password de utilizador da BD.
     */
    private final String password;

    /**
     * A ligação à BD.
     */
    private Connection connection = null;

    /**
     * A invocação de "stored procedures".
     */
    private CallableStatement callStmt = null;

    /**
     * Conjunto de resultados retornados por "stored procedures".
     */
    private ResultSet rSet = null;

    private List<AutoCloseable> autoCloseablesCloseQueue = new ArrayList<>();

    /**
     * <b>Only one instance allowed at all times</b>
     * Use connection properties set on file application.properties
     */
    public DataHandler() throws IllegalAccessException, SQLException {
        synchronized (created) { // Ensure that at no time the application can have 2 instances
            if (created.getValue())
                throw new IllegalAccessException("Only a single DataHandler instance is allowed");
            else
                created.setValue(true);
        }
        this.jdbcUrl = System.getProperty("database.url");
        this.username = System.getProperty("database.username");
        this.password = System.getProperty("database.password");
        openConnection();
    }

    public void queueForClose(AutoCloseable autoCloseable) {
        autoCloseablesCloseQueue.add(autoCloseable);
    }

    public void closeQueuedAutoCloseables() {
        for (AutoCloseable obj : autoCloseablesCloseQueue) {
            if (obj != null) {
                try {
                    obj.close();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Failed to close an open AutoCloseable object: \n" + ex.getMessage());
                }
            }
        }
    }

    /**
     * Allows running entire scripts
     *
     * @param fileName
     * @throws IOException
     * @throws SQLException
     */
    public void scriptRunner(String fileName) throws IOException, SQLException {
        openConnection();

        if (connection == null)
            openConnection();
        ScriptRunner runner = new ScriptRunner(connection, true, false);

        runner.runScript(new BufferedReader(new FileReader(fileName)));

        closeAll();

    }

    /**
     * Estabelece a ligação à BD. <b>Não são efetuadas quaisquer verificações sobre o estado da conexão atual.</b>
     */
    private void openConnection() throws SQLException {
        connection = DriverManager.getConnection(
                jdbcUrl, username, password);
        connection.setAutoCommit(true);
    }

    /**
     * Fecha os objetos "ResultSet", "CallableStatement" e "Connection", e
     * retorna uma mensagem de erro se alguma dessas operações não for bem
     * sucedida. Caso contrário retorna uma "string" vazia.
     */
    public String closeAll() {
        StringBuilder message = new StringBuilder();

        if (rSet != null) {
            try {
                rSet.close();
            } catch (SQLException ex) {
                message.append(ex.getMessage());
                message.append("\n");
            }
            rSet = null;
        }

        if (callStmt != null) {
            try {
                callStmt.close();
            } catch (SQLException ex) {
                message.append(ex.getMessage());
                message.append("\n");
            }
            callStmt = null;
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                message.append(ex.getMessage());
            }
            connection = null;
        }
        closeQueuedAutoCloseables();
        return message.toString();
    }

    private static boolean attemptingToReconnect = false;
    private synchronized void continuousReconnectAttempt() {
        if (attemptingToReconnect)
            return;

        attemptingToReconnect = true;
        new Thread(() -> {
            int nAttempt = 1;
            boolean connected = false;
            while (!connected && nAttempt < MAX_RECONNECTION_ATTEMPTS_UNRECOVERABLE) {
                nAttempt++;
                connected = true;
                try { Company.getInstance().getDataHandler().openConnection(); } catch (Exception e) { connected = false;}
                try { wait(RECONNECTION_INTERVAL_MILLIS); } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "Failed to make thread wait, skipping wait period.");
                }
            }
            attemptingToReconnect = false;
        }).start();
    }

    private <T> T executeRecoverableSQLOperation(SQLOperation<T> operation) throws SQLException {
        int nAttempt = 1;
        while (true) {
            try {
                return operation.executeOperation();
            } catch (SQLException e) {
                if (e.getErrorCode() == CONNECTION_FAILURE_ORA_CODE && nAttempt < MAX_RECONNECTION_ATTEMPTS_RECOVERABLE) {
                    try {
                        wait(RECONNECTION_INTERVAL_MILLIS);
                    } catch (InterruptedException ex) {
                        LOGGER.log(Level.WARNING, "Failed to make thread wait, skipping wait period.");
                    }
                    openConnection();
                }
                else {
                    continuousReconnectAttempt();
                    throw e;
                }
                nAttempt++;
            }
        }
    }

    private <T> T executeUnrecoverableSQLOperation(SQLOperation<T> operation) throws SQLException {
        try {
            return operation.executeOperation();
        } catch (SQLException e) {
            if (e.getErrorCode() == 17008)
                continuousReconnectAttempt();
            throw e;
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<PreparedStatement> operation = () -> {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            return stm;
        };
        return executeRecoverableSQLOperation(operation);
    }

    public CallableStatement prepareCall(String query) throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<CallableStatement> operation = () -> {
            CallableStatement cs = connection.prepareCall(query);
            cs.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
            return cs;
        };
        return executeRecoverableSQLOperation(operation);
    }

    public ResultSet executeQuery(PreparedStatement preparedStatement) throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<ResultSet> operation = () -> preparedStatement.executeQuery();
        return executeUnrecoverableSQLOperation(operation);
    }

    public Integer executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<Integer> operation = () -> preparedStatement.executeUpdate();
        return executeUnrecoverableSQLOperation(operation);
    }

    public Boolean execute(PreparedStatement preparedStatement) throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<Boolean> operation = () -> preparedStatement.execute();
        return executeUnrecoverableSQLOperation(operation);
    }

    public int[] executeBatch(Statement statement) throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<int[]> operation = () -> statement.executeBatch();
        return executeUnrecoverableSQLOperation(operation);
    }

    public Boolean commitTransaction() throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<Boolean> operation = () -> {connection.commit(); return true;};
        return executeUnrecoverableSQLOperation(operation);
    }

    public Boolean rollbackTransaction() throws SQLException {
        if (connection == null)
            throw new SQLException(NOT_CONNECTED_ERROR_MSG);
        SQLOperation<Boolean> operation = () -> {connection.commit(); return true;};
        return executeUnrecoverableSQLOperation(operation);
    }
}
