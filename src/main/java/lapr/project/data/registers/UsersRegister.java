package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lapr.project.model.Coordinates;
import lapr.project.model.park.Park;
import lapr.project.model.vehicles.VehicleType;

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
        ResultSet resultSet = null;
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM clients where user_email=?");
            stm.setString( 1, email);
            resultSet = dataHandler.executeQuery(stm);
            if (resultSet == null || !resultSet.next())
                return null;

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
            if (resultSet == null || !resultSet.next())
                return null;

            String password = resultSet.getString( 3);
            String username = resultSet.getString( 4);
            resultSet.close();
            stm.close();
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
    public boolean insertClient(String email, String username, String password, float amountLeftToPay, int creditCardSecret, int age, int height, int weight, char gender, String creditCardNumber, String creditCardExpiration, float cyclingAvgSpeed) {
        //create statement to be executed later
        PreparedStatement stm = null;
        Date date;
        try {
            date = new Date(new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExpiration).toInstant().toEpochMilli());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return false; // exit method
        }
        try {
            //pending registrations
            stm = dataHandler.prepareStatement("INSERT INTO pending_registrations(email, amount_left_to_pay, credit_card_number, credit_card_expiration, credit_card_secret, height, weight, gender, age, cycling_average_speed, user_password, user_name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

            stm.setString( 1, email);
            stm.setFloat( 2, amountLeftToPay);
            stm.setString( 3, creditCardNumber);
            stm.setDate( 4, date); // creditCardExpiration (Date)
            stm.setInt( 5, creditCardSecret);
            stm.setInt( 6, height);
            stm.setInt( 7, weight);
            stm.setString( 8, String.valueOf(gender)); // uses setString even
            // tho its a char
            stm.setInt( 9, age);
            stm.setFloat( 10, cyclingAvgSpeed);
            stm.setString( 11, password);
            stm.setString( 12, username);
            int nrLines = dataHandler.executeUpdate(stm);
            if (nrLines == 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (stm != null)
                try {
                    stm.close(); // closes statement
                } catch (SQLException e) {}
        }
        return true;
    }
    
     public double distanceOfParkById(int id,Coordinates coordClient){
        Company company = Company.getInstance();
        ParkRegister pr = company.getParkRegister();
        Park park = pr.fetchParkById(id);
       double dist = coordClient.distance(park.getCoords());
       return dist;
    }

    public int numPlacesOfParkById(int id,VehicleType vT){
        Company company = Company.getInstance();
        ParkRegister pr = company.getParkRegister();
        Park park = pr.fetchParkById(id);
        int places = (park.getMaxAmountByType(vT)- park.getAmountOccupiedByType(vT));
        return places;
    }
}
