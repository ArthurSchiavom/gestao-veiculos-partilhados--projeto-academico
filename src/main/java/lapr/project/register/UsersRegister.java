package lapr.project.register;

import lapr.project.data.DataHandler;
import lapr.project.model.Company;
import lapr.project.model.Users.ClientType;
import lapr.project.model.Users.User;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Handles the Users and its subclasses
 */
public class UsersRegister {
    DataHandler dataHandler;
    public UsersRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Inserts a client user into the sql database
     * @param email email of the user
     * @param password password of the user
     * @param creditCardSecret  3 last digits of the credit card
     * @param age age of the client
     * @param height height of the client
     * @param weight weight of the client
     * @param gender gender of the client
     * @param creditCardNumber credit card number of the client
     * @param creditCardExpiration credit card expiration date of the client
     */
    public void insertClient(String email, String password, int creditCardSecret, int age, float height, float weight, char gender, String creditCardNumber, String creditCardExpiration){
        //create statement to be executed later
        PreparedStatement stm = null;
        String userTypeName = "Client";
        Date date;
        try {
            date = (Date) new SimpleDateFormat("dd-MM-yyyy").parse(creditCardExpiration);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return; // exit method
        }
        try {
            //insert registered_user
            stm = dataHandler.prepareStatement("Insert into registered_users(user_email,user_type_name,user_password) values (?,?,?); ");
            dataHandler.setString(stm, 1, email);
            dataHandler.setString(stm,2, userTypeName); // have to verify if I can do this or have to select the userTypeName from TABLE 'user_type'
            dataHandler.setString(stm, 3, password);
            dataHandler.executeUpdate(stm);

            //insert Client
            stm = dataHandler.prepareStatement("INSERT INTO clients VALUES(?,0,?,?, ?,?,?,?,'0',?);");
            dataHandler.setString(stm, 1, email);
            dataHandler.setString(stm,2,creditCardNumber);
            dataHandler.setDate(stm,3,date); // creditCardExpiration (Date)
            dataHandler.setInt(stm,4,creditCardSecret);
            dataHandler.setDouble(stm,5,height);
            dataHandler.setDouble(stm,6,weight);
            dataHandler.setString(stm,7,String.valueOf(gender)); // uses setString even tho its a char
            dataHandler.setInt(stm,8,age);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
