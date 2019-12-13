package lapr.project.register;

import lapr.project.data.DataHandler;
import lapr.project.model.Company;
import lapr.project.model.Trip;
import lapr.project.model.Users.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TripRegister {
    private DataHandler dataHandler;
    private UsersRegister usersRegister;

    public TripRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        Company comp = Company.getInstance();
        usersRegister = comp.getUsersRegister();
    }

    public Trip fetchTrip(String clientEmail, LocalDateTime startTime) {
        PreparedStatement prepStat = null;
        Trip trip;
        Client client = usersRegister.fetchClient(clientEmail);
        try {
            prepStat = dataHandler.prepareStatement("SELECT * FROM trip where start_time=? AND user_email=?");
            dataHandler.setTimestamp(prepStat, 1, Timestamp.valueOf(startTime));
            dataHandler.setString(prepStat, 2, clientEmail);
            ResultSet resultSet = dataHandler.executeQuery(prepStat);
            if (resultSet == null) {
                return null;
            }

            int vehicleId = dataHandler.getInt(resultSet, 3);
            int startParkId = dataHandler.getInt(resultSet, 4);
            int endParkId = dataHandler.getInt(resultSet, 5);
            LocalDateTime endTimestamp = dataHandler.getTimestamp(resultSet, 6).toLocalDateTime();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new UnsupportedOperationException("TO FINISH TALK TO PEDRO IVO");
    }

    public Trip createNewTrip(LocalDateTime startTime, String clientEmail, int startParkId, int endParkId, int vehicleId) {
        return new Trip(startTime, clientEmail, startParkId, endParkId, vehicleId);
    }

    public Trip createNewTrip(LocalDateTime startTime, String clientEmail, int startParkId, int vehicleId) {
        return new Trip(startTime, clientEmail, startParkId, vehicleId);
    }

    public Trip createNewTrip(LocalDateTime startTime, LocalDateTime endTime, String clientEmail, int startParkId, int endParkId, int vehicleId) {
        return new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
    }
}
