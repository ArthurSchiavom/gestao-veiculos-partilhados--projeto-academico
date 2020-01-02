package lapr.project.data.registers;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the users and its subclasses
 */
public class UserAPI {
    private DataHandler dataHandler;
    private static float DEFAULT_VALUE_TO_PAY = 0;
    private static final Logger LOGGER = Logger.getLogger("usersRegisterLogger");

    public UserAPI(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Inserts a list of clients
     */
    public int insertClients(List<String> email, List<String> username, List<Integer> height, List<Integer> weight, List<Character> gender, List<String> creditCardNumber, List<Float> cyclingAvgSpeed, List<String> password) throws SQLException {
        if(!(password.size() == email.size() && email.size()==username.size() && username.size() == height.size() && height.size() == weight.size() && weight.size()== gender.size() && gender.size() == creditCardNumber.size() && creditCardNumber.size() == cyclingAvgSpeed.size())){
            throw new IllegalArgumentException("Lists have different sizes.");
        }
        int i;
        for( i = 0 ; i < email.size(); i++){
            try {
                insertClient(email.get(i), username.get(i), height.get(i), weight.get(i), gender.get(i),  creditCardNumber.get(i), cyclingAvgSpeed.get(i),password.get(i));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
                dataHandler.rollbackTransaction();
                throw e;
            }
        }
        dataHandler.commitTransaction(); // commits all the clients at once contained in the current transaction
        return i;
    }

    /**
     * Fetches a client from the database.
     *
     * @param username client's username
     * @return (1) client object or (2) null if no such client
     */
    public Client fetchClientByUsername(String username) {
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM registered_users where USER_NAME = ?");
            autoCloseableManager.addAutoCloseable(stm);

            stm.setString( 1, username);
            resultSet = dataHandler.executeQuery(stm);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (!resultSet.next())
                return null;

            String password = resultSet.getString( "user_password");
            String email = resultSet.getString("user_email");

            stm = dataHandler.prepareStatement("SELECT * FROM clients where USER_EMAIL like ?");
            autoCloseableManager.addAutoCloseable(stm);
            stm.setString( 1, email);

            resultSet = dataHandler.executeQuery(stm);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (!resultSet.next()) {
                return null;
            }

            int points = resultSet.getInt( "points");
            String creditCardNumber = resultSet.getString( "visa");
            int height =  resultSet.getInt( "height_cm");
            int weight =  resultSet.getInt( "weight_kg");
            char gender = resultSet.getString( "gender").charAt(0);
            float cyclingAvgSpeed = resultSet.getFloat( "cycling_average_speed");
            boolean isRiding = resultSet.getBoolean("is_riding");
            return new Client(email, username, password, points, height, weight, gender, cyclingAvgSpeed, isRiding, new CreditCard(creditCardNumber));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return null;
    }

    private void insertClient(String email, String username, int height, int weight, char gender, String creditCardNumber,  float cyclingAvgSpeed, String password) throws SQLException {
        //create statement to be executed later
        PreparedStatement stm = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            //pending registrations
            stm = dataHandler.prepareStatement("INSERT INTO pending_registrations(email, amount_left_to_pay, visa, height_cm, weight, gender, cycling_average_speed, user_password, user_name) VALUES(?,?,?,?,?,?,?,?,?)");
            autoCloseableManager.addAutoCloseable(stm);

            stm.setString( 1, email.toLowerCase().trim()); // capital letters do not matter in email addresses
            stm.setFloat( 2, DEFAULT_VALUE_TO_PAY);
            stm.setString( 3, creditCardNumber.trim());
            stm.setInt( 4, height);
            stm.setInt( 5, weight);
            stm.setString( 6, String.valueOf(gender)); // uses setString even
            // tho its a char
            stm.setFloat( 7, cyclingAvgSpeed);
            stm.setString(8,password);
            stm.setString( 9, username.trim());
            int nrLines = dataHandler.executeUpdate(stm);
            if (nrLines == 0) {
                throw new IllegalArgumentException("Client not inserted correctly");
            }
        } catch (SQLException e) {
            throw e; // throws the exception it catches, because it needs the finally clause to close the statement
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

//    /**
//     * Calculates the prediction for calory loss of the route choosen by the client
//     */
//    private double predictBurntCalories(Client cli, Bicycle bicycle, List<Path> route){
//        double totalCaloryLoss = 0;
//        for(Path path : route){
//            totalCaloryLoss+= PhysicsMethods.calculateCaloriesBurnt(cli.getCyclingAverageSpeed(),path.getWindSpeed(),path.getKineticCoefficient(),bicycle.getAerodynamicCoefficient(),bicycle.getFrontalArea(),1.2f,cli.getWeight(),bicycle.getWeight(),0,0);
//        }
//        return totalCaloryLoss;
//    }

    int updateClientIsRidingNoCommit(String username, boolean newValue) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        int isRidingInt = newValue ? 1 : 0;
        Client client = null;
        try {
            client = fetchClientByUsername(username);
            if (client == null)
                throw new SQLException("No such client");

            PreparedStatement preparedStatement = dataHandler.prepareStatement("update clients set is_riding = ? where user_email = ?");
            autoCloseableManager.addAutoCloseable(preparedStatement);
            preparedStatement.setInt(1, isRidingInt);
            preparedStatement.setString(2, client.getEmail());

            return dataHandler.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            if (client == null)
                throw e;

            throw new SQLException("Failed to access the database", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }
}
