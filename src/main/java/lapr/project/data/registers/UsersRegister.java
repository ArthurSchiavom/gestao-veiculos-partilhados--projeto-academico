package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Handles the users and its subclasses
 */
public class UsersRegister {
    private DataHandler dataHandler;

    public UsersRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Inserts a list of clients
     */
    public void insertClients(List<String> email, List<String> username, List<String> password, List<Float> amountLeftToPay, List<Integer> creditCardSecret, List<Integer> age, List<Integer> height, List<Integer> weight, List<Character> gender, List<String> creditCardNumber, List<String> creditCardExpiration, List<Float> cyclingAvgSpeed) throws SQLException {
        if(!(email.size()==username.size() && username.size() == password.size() && password.size() == amountLeftToPay.size() && amountLeftToPay.size() == creditCardExpiration.size() && creditCardExpiration.size() == age.size() && age.size() == height.size() && height.size() == weight.size() && weight.size()== gender.size() && gender.size() == creditCardNumber.size() && creditCardNumber.size() == creditCardExpiration.size() && creditCardExpiration.size() == cyclingAvgSpeed.size() && cyclingAvgSpeed.size() == creditCardSecret.size())){
            throw new IllegalArgumentException("Lists have different sizes.");
        }
        for(int i = 0 ; i < email.size(); i++){
            try {
                insertClient(email.get(i), username.get(i), password.get(i), amountLeftToPay.get(i), creditCardSecret.get(i), age.get(i), height.get(i), weight.get(i), gender.get(i),  creditCardNumber.get(i), creditCardExpiration.get(i), cyclingAvgSpeed.get(i));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        dataHandler.commitTransaction(); // commits all the clients at once contained in the current transaction
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
            String creditCardNumber = resultSet.getString( "credit_card_number");
            Date creditCardExpiration = resultSet.getDate( "credit_card_expiration");
            int creditCardSecret = resultSet.getInt( "credit_card_secret");
            int height =  resultSet.getInt( "height_m");
            int weight =  resultSet.getInt( "weight_kg");
            char gender = resultSet.getString( "gender").charAt(0);
            int age = resultSet.getInt( "age");
            float cyclingAvgSpeed = resultSet.getFloat( "cycling_average_speed");

            // get password of client
            stm = dataHandler.prepareStatement("SELECT * FROM registered_users where user_email=?");
            stm.setString( 1, email);
            resultSet = dataHandler.executeQuery(stm);
            if (resultSet == null || !resultSet.next())
                return null;

            String password = resultSet.getString( "user_password");
            String username = resultSet.getString( "user_name");
            return new Client(email,username ,password, points, age, height, weight, gender,cyclingAvgSpeed, new CreditCard(creditCardNumber, creditCardExpiration.toLocalDate(), creditCardSecret));
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
    private void insertClient(String email, String username, String password, float amountLeftToPay, int creditCardSecret, int age, int height, int weight, char gender, String creditCardNumber, String creditCardExpiration, float cyclingAvgSpeed) throws ParseException, SQLException {
        //create statement to be executed later
        PreparedStatement stm = null;
        Date date = new Date(new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExpiration).toInstant().toEpochMilli());
        try {
            //pending registrations
            stm = dataHandler.prepareStatement("INSERT INTO pending_registrations(email, amount_left_to_pay, credit_card_number, credit_card_expiration, credit_card_secret, height, weight, gender, age, cycling_average_speed, user_password, user_name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

            stm.setString( 1, email.toLowerCase().trim()); // capital letters do not matter in email addresses
            stm.setFloat( 2, amountLeftToPay);
            stm.setString( 3, creditCardNumber.trim());
            stm.setDate( 4, date); // creditCardExpiration (Date)
            stm.setInt( 5, creditCardSecret);
            stm.setInt( 6, height);
            stm.setInt( 7, weight);
            stm.setString( 8, String.valueOf(gender)); // uses setString even
            // tho its a char
            stm.setInt( 9, age);
            stm.setFloat( 10, cyclingAvgSpeed);
            stm.setString( 11, password.trim());
            stm.setString( 12, username.trim());
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
        dataHandler.commitTransaction();
    }
}
