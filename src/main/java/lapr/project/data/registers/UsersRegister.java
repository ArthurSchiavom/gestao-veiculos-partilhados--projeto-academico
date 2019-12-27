package lapr.project.data.registers;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.Path;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.utils.physics.calculations.PhysicsMethods;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the users and its subclasses
 */
public class UsersRegister {
    private DataHandler dataHandler;
    private static float DEFAULT_VALUE_TO_PAY = 0;
    private static final Logger LOGGER = Logger.getLogger("usersRegisterLogger");

    public UsersRegister(DataHandler dataHandler) {
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
     * Fetches a client object from the oracle sql table
     *
     * @param email email of the client
     * @return client object
     */
    public Client fetchClient(String email) {
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM clients where lower(user_email) like ?"); // capital letters do not matter in emails
            autoCloseableManager.addAutoCloseable(stm);
            stm.setString( 1, email);
            resultSet = dataHandler.executeQuery(stm);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            int points = resultSet.getInt( "points");
            String creditCardNumber = resultSet.getString( "visa");
            int height =  resultSet.getInt( "height_cm");
            int weight =  resultSet.getInt( "weight_kg");
            char gender = resultSet.getString( "gender").charAt(0);
            float cyclingAvgSpeed = resultSet.getFloat( "cycling_average_speed");

            // get password of client
            stm = dataHandler.prepareStatement("SELECT * FROM registered_users where user_email=?");
            autoCloseableManager.addAutoCloseable(stm);

            stm.setString( 1, email);
            resultSet = dataHandler.executeQuery(stm);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (resultSet == null || !resultSet.next())
                return null;

            String password = resultSet.getString( "user_password");
            String username = resultSet.getString( "user_name");
            return new Client(email,username ,password, points, height, weight, gender,cyclingAvgSpeed, new CreditCard(creditCardNumber));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return null;
    }

    /**
     * Inserts a client user into the sql database
     *
     * @param email email of the user
     * @param amountLeftToPay total amount left to pay
     * @param creditCardSecret 3 last digits of the credit card
     * @param age age of the client
     * @param height height of the client
     * @param weight weight of the client
     * @param gender gender of the client
     * @param creditCardNumber credit card number of the client
     * @param creditCardExpiration credit card expiration date of the client
     */
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

    /**
     * Calculates the prediction for calory loss of the route choosen by the client
     */
    private double predictBurntCalories(Client cli, Bicycle bicycle, List<Path> route){
        double totalCaloryLoss = 0;
        for(Path path : route){
            totalCaloryLoss+= PhysicsMethods.calculateCaloriesBurnt(cli.getCyclingAverageSpeed(),path.getWindSpeed(),path.getKineticCoefficient(),bicycle.getAerodynamicCoefficient(),bicycle.getFrontalArea(),1.2f,cli.getWeight(),bicycle.getWeight(),0,0);
        }
        return totalCaloryLoss;
    }

    /**
     *
     * @param listScooters all the scooters in the company
     * @param trip the path of the trip
     * @return a list of scooters which are able to comply the trip and keep 10% of the battery capacity
     */

    private List<ElectricScooter> getScootersEnoughBatteryMore10 (List<ElectricScooter> listScooters, List<Path> trip){
        List<ElectricScooter> scooters = new ArrayList<>();
        for(ElectricScooter electricScooter : listScooters){
            if(electricScooter.hasAutonomy(trip) == true){
                scooters.add(electricScooter);
            }
        }
        return scooters;
    }
}
