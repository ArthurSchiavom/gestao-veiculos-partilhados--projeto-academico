package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.Path;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.utils.physics.calculations.PhysicsMethods;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Handles the users and its subclasses
 */
public class UsersRegister {
    private DataHandler dataHandler;
    private static float DEFAULT_VALUE_TO_PAY = 0;

    public UsersRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Inserts a list of clients
     */
    public int insertClients(List<String> email, List<String> username, List<Integer> height, List<Integer> weight, List<Character> gender, List<String> creditCardNumber, List<Float> cyclingAvgSpeed) throws SQLException {
        if(!(email.size()==username.size() && username.size() == height.size() && height.size() == weight.size() && weight.size()== gender.size() && gender.size() == creditCardNumber.size() && creditCardNumber.size() == cyclingAvgSpeed.size())){
            throw new IllegalArgumentException("Lists have different sizes.");
        }
        int i;
        for( i = 0 ; i < email.size(); i++){
            try {
                insertClient(email.get(i), username.get(i), height.get(i), weight.get(i), gender.get(i),  creditCardNumber.get(i), cyclingAvgSpeed.get(i));
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM clients where lower(user_email) like ?"); // capital letters do not matter in emails
            stm.setString( 1, email);
            resultSet = dataHandler.executeQuery(stm);
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
            stm.setString( 1, email);
            resultSet = dataHandler.executeQuery(stm);
            if (resultSet == null || !resultSet.next())
                return null;

            String password = resultSet.getString( "user_password");
            String username = resultSet.getString( "user_name");
            return new Client(email,username ,password, points, height, weight, gender,cyclingAvgSpeed, new CreditCard(creditCardNumber));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {}
            }

            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {}
            }
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
    private void insertClient(String email, String username, int height, int weight, char gender, String creditCardNumber,  float cyclingAvgSpeed) throws ParseException, SQLException {
        //create statement to be executed later
        PreparedStatement stm = null;
        try {
            //pending registrations
            stm = dataHandler.prepareStatement("INSERT INTO pending_registrations(email, amount_left_to_pay, visa, height_cm, weight, gender, cycling_average_speed, user_password, user_name) VALUES(?,?,?,?,?,?,?,DEFAULT,?)");

            stm.setString( 1, email.toLowerCase().trim()); // capital letters do not matter in email addresses
            stm.setFloat( 2, DEFAULT_VALUE_TO_PAY);
            stm.setString( 3, creditCardNumber.trim());
            stm.setInt( 4, height);
            stm.setInt( 5, weight);
            stm.setString( 6, String.valueOf(gender)); // uses setString even
            // tho its a char
            stm.setFloat( 7, cyclingAvgSpeed);
            stm.setString( 8, username.trim());
            int nrLines = dataHandler.executeUpdate(stm);
            if (nrLines == 0) {
                throw new IllegalArgumentException("Client not inserted correctly");
            }
        } catch (SQLException e) {
            throw e; // throws the exception it catches, because it needs the finally clause to close the statement
        } finally {
            if (stm != null)
                try {
                    stm.close(); // closes statement
                } catch (SQLException e) {}
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
}
