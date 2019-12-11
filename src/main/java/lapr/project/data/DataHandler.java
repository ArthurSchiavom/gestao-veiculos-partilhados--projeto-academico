package lapr.project.data;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

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
    public DataHandler(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        connection = null;
        callStmt = null;
        rSet = null;
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

        ScriptRunner runner = new ScriptRunner(getConnection(), true, false);

        runner.runScript(new BufferedReader(new FileReader(fileName)));

        closeAll();

    }

    /**
     * Estabelece a ligação à BD.
     */
    protected void openConnection() {
        try {
            connection = DriverManager.getConnection(
                    jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fecha os objetos "ResultSet", "CallableStatement" e "Connection", e
     * retorna uma mensagem de erro se alguma dessas operações não for bem
     * sucedida. Caso contrário retorna uma "string" vazia.
     */
    protected String closeAll() {

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
                message.append("\n");
            }
            connection = null;
        }
        return message.toString();
    }


    public Connection getConnection() {
        if (connection == null)
            openConnection();
        return connection;
    }

    public <T> T executeSQLOperation(SQLOperation<T> operation) throws SQLException {
        int nAttempt = 1;
        while (true) {
            try {
                return operation.executeOperation();
            } catch (SQLException e) {
                if (nAttempt < 3) {
                    try {
                        nAttempt++;
                        Thread.sleep(RECONNECTION_INTERVAL_MILLIS);
                    } catch (InterruptedException ex) {}
                    openConnection();
                }
                else
                    throw e;
            }
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        SQLOperation<PreparedStatement> operation = () -> {return connection.prepareStatement(query);};
        return executeSQLOperation(operation);
    }

    public ResultSet executeQuery(PreparedStatement preparedStatement) throws SQLException {
        SQLOperation<ResultSet> operation = () -> {return preparedStatement.executeQuery();};
        return executeSQLOperation(operation);
    }

    public Integer executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        SQLOperation<Integer> operation = () -> {return preparedStatement.executeUpdate();};
        return executeSQLOperation(operation);
    }

    public void setInt(PreparedStatement preparedStatement, int position, int value) throws SQLException {
        SQLOperation<Boolean> operation = () -> {preparedStatement.setInt(position, value); return true;};
        executeSQLOperation(operation);
    }

    public void setDouble(PreparedStatement preparedStatement, int position, double value) throws SQLException {
        SQLOperation<Boolean> operation = () -> {preparedStatement.setDouble(position, value); return true;};
        executeSQLOperation(operation);
    }

    public void setString(PreparedStatement preparedStatement, int position, String value) throws SQLException {
        SQLOperation<Boolean> operation = () -> {preparedStatement.setString(position, value); return true;};
        executeSQLOperation(operation);
    }

    public void setTimestamp(PreparedStatement preparedStatement, int position, Timestamp value) throws SQLException {
        SQLOperation<Boolean> operation = () -> {preparedStatement.setTimestamp(position, value); return true;};
        executeSQLOperation(operation);
    }

    public void setDate(PreparedStatement preparedStatement, int position, Date value) throws SQLException {
        SQLOperation<Boolean> operation = () -> {preparedStatement.setDate(position, value); return true;};
        executeSQLOperation(operation);
    }

    public Timestamp getString(ResultSet resultSet, int columnPosition) throws SQLException {
        SQLOperation<Timestamp> operation = () -> {return resultSet.getTimestamp(columnPosition);};
        return executeSQLOperation(operation);
    }

    public int getInt(ResultSet resultSet, int columnPosition) throws SQLException {
        SQLOperation<Integer> operation = () -> {return resultSet.getInt(columnPosition);};
        return executeSQLOperation(operation);
    }

    public double getDouble(ResultSet resultSet, int columnPosition) throws SQLException {
        SQLOperation<Double> operation = () -> {return resultSet.getDouble(columnPosition);};
        return executeSQLOperation(operation);
    }

    public Date getDate(ResultSet resultSet, int columnPosition) throws SQLException {
        SQLOperation<Date> operation = () -> {return resultSet.getDate(columnPosition);};
        return executeSQLOperation(operation);
    }

    public Timestamp getTimestamp(ResultSet resultSet, int columnPosition) throws SQLException {
        SQLOperation<Timestamp> operation = () -> {return resultSet.getTimestamp(columnPosition);};
        return executeSQLOperation(operation);
    }
}
