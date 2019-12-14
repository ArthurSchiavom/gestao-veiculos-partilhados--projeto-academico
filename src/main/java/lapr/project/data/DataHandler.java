package lapr.project.data;


import lapr.project.model.Company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

import static java.lang.Thread.sleep;

/**
 * Exemplo de classe cujas instâncias manipulam dados de BD Oracle.
 */
public class DataHandler {
    private static final int MAX_ATTEMPTS = 3;
    private static final int RECONNECTION_INTERVAL_MILLIS = 2000;

    /**
     * O URL da BD.
     */
    private String jdbcUrl;

    /**
     * O nome de utilizador da BD.
     */
    private String username;

    /**
     * A password de utilizador da BD.
     */
    private String password;

    /**
     * A ligação à BD.
     */
    private Connection connection;

    /**
     * A invocação de "stored procedures".
     */
    private CallableStatement callStmt;

    /**
     * Conjunto de resultados retornados por "stored procedures".
     */
    private ResultSet rSet;

    /**
     * Use connection properties set on file application.properties
     */
    public DataHandler() {
        this.jdbcUrl = System.getProperty("database.url");
        this.username = System.getProperty("database.username");
        this.password = System.getProperty("database.password");
    }

    /**
     * Constrói uma instância de "DataHandler" recebendo, por parâmetro, o URL
     * da BD e as credenciais do utilizador.
     *
     * @param jdbcUrl  o URL da BD.
     * @param username o nome do utilizador.
     * @param password a password do utilizador.
     */
    public DataHandler(String jdbcUrl, String username, String password) throws SQLException {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        connection = null;
        callStmt = null;
        rSet = null;
        openConnection();
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
        return message.toString();
    }

    private static boolean attemptingToReconnect = false;
    private static synchronized void continuousReconnectAttempt() {
        if (attemptingToReconnect)
            return;

        attemptingToReconnect = true;
        new Thread(() -> {
            boolean connected = false;
            while (!connected) {
                connected = true;
                try { Company.getInstance().getDataHandler().openConnection(); } catch (Exception e) { connected = false;}
                try { sleep(2000); } catch (InterruptedException e) {}
            }
            attemptingToReconnect = false;
        }).start();
    }

    public <T> T executeRecoverableSQLOperation(SQLOperation<T> operation) throws SQLException {
        int nAttempt = 1;
        while (true) {
            try {
                return operation.executeOperation();
            } catch (SQLException e) {
                if (e.getErrorCode() == 17008 && nAttempt < 3) {
                    try {
                        sleep(RECONNECTION_INTERVAL_MILLIS);
                    } catch (InterruptedException ex) {}
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

    public <T> T executeUnrecoverableSQLOperation(SQLOperation<T> operation) throws SQLException {
        try {
            return operation.executeOperation();
        } catch (SQLException e) {
            if (e.getErrorCode() == 17008)
                continuousReconnectAttempt();
            throw e;
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        SQLOperation<PreparedStatement> operation = () -> {return connection.prepareStatement(query);};
        return executeRecoverableSQLOperation(operation);
    }

    public CallableStatement prepareCall(String query) throws SQLException {
        SQLOperation<CallableStatement> operation = () -> {return connection.prepareCall(query);};
        return executeRecoverableSQLOperation(operation);
    }

    public ResultSet executeQuery(PreparedStatement preparedStatement) throws SQLException {
        SQLOperation<ResultSet> operation = () -> {return preparedStatement.executeQuery();};
        return executeUnrecoverableSQLOperation(operation);
    }

    public Integer executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        SQLOperation<Integer> operation = () -> {return preparedStatement.executeUpdate();};
        return executeUnrecoverableSQLOperation(operation);
    }
}
