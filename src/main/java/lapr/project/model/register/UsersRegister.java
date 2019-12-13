package lapr.project.model.register;

import lapr.project.data.DataHandler;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Handles the users and its subclasses
 */
public class UsersRegister {
    private DataHandler dataHandler;

    public UsersRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Fetches a client object from the oraqle sql table
     *
     * @param email email of the client
     * @return client object
     */
    public Client fetchClient(String email) {
        PreparedStatement stm = null;
        Client client;
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM clients where user_email=?");
            stm.setString( 1, email);
            ResultSet resultSet = dataHandler.executeQuery(stm);
            if(resultSet == null || resultSet.next()== false){ //resultSet is empty
                resultSet.close();
                stm.close();
                return null;
            }
            int points = resultSet.getInt( 2);
            String creditCardNumber = resultSet.getString( 3);
            Date creditCardExpiration = resultSet.getDate( 4);
            int creditCardSecret = resultSet.getInt( 5);
            int height =  resultSet.getInt( 6);
            int weight =  resultSet.getInt( 7);
            char gender = resultSet.getString( 8).charAt(0);
            int age = resultSet.getInt( 10);
            float cyclingAvgSpeed = resultSet.getFloat( 11);

            // get password of client
            stm = dataHandler.prepareStatement("SELECT * FROM registered_users where user_email=?");
            stm.setString( 1, email);
            resultSet = dataHandler.executeQuery(stm);
            if(resultSet == null|| !resultSet.next()){
                resultSet.close();
                stm.close();
                return null;
            }
            String password = resultSet.getString( 3);
            String username = resultSet.getString( 4);
            resultSet.close();
            stm.close();
            return new Client(email,username ,password, points, age, height, weight, gender,cyclingAvgSpeed, new CreditCard(creditCardNumber, creditCardExpiration.toLocalDate(), creditCardSecret));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a client user into the sql database
     *
     * @param email email of the user
     * @param paid amount paid from the total amount
     * @param amountLeftToPay total amount left to pay
     * @param creditCardSecret 3 last digits of the credit card
     * @param age age of the client
     * @param height height of the client
     * @param weight weight of the client
     * @param gender gender of the client
     * @param creditCardNumber credit card number of the client
     * @param creditCardExpiration credit card expiration date of the client
     */
    public boolean insertClient(String email, String username, String password, float paid, float amountLeftToPay, int creditCardSecret, int age, int height, int weight, char gender, String creditCardNumber, String creditCardExpiration, float cyclingAvgSpeed) {
        //create statement to be executed later
        PreparedStatement stm = null;
        String userTypeName = "Client";
        Date date;
        try {
            date = new Date(new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExpiration).toInstant().toEpochMilli());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return false; // exit method
        }
        try {
            //pending registrations
            stm = dataHandler.prepareStatement("Insert into pending_registrations(email,amount_left_to_pay,credit_card_number,credit_card_expiration,credit_card_secret,height,weight,gender,age,user_name,user_password,cycling_average_speed) values (?,?,?,?, ?, ?,?,?,?,?,?,?,?);");
            stm.setString( 1, email);
            stm.setFloat( 2, amountLeftToPay);
            stm.setString( 3, creditCardNumber);
            stm.setDate( 4, date); // creditCardExpiration (Date)
            stm.setInt( 5, creditCardSecret);
            stm.setInt( 6, height);
            stm.setInt( 7, weight);
            System.out.println("ola1");
            stm.setString( 8, String.valueOf(gender)); // uses setString even
            // tho its a char
            System.out.println("ola2");
            stm.setInt( 9, age);
            stm.setString( 10, username);
            stm.setString( 11, password);
            stm.setFloat( 12, cyclingAvgSpeed);
            System.out.println("ola3");
            dataHandler.executeUpdate(stm);
            System.out.println("ola4");
            stm.close(); // closes statement
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
