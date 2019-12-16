package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.Trip;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TripRegister {
    private DataHandler dataHandler;

    public TripRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Fetches a trip from the database
     *
     * @param clientEmail the email of the client
     * @param startTime   the start time of the trip
     * @return a trip object with data from the database
     */
    public Trip fetchTrip(String clientEmail, LocalDateTime startTime) {
        PreparedStatement prepStat = null;
        try {
            prepStat = dataHandler.prepareStatement(
                    "SELECT * FROM trip where start_time=? AND user_email=?");
            prepStat.setTimestamp( 1, Timestamp.valueOf(startTime));
            prepStat.setString( 2, clientEmail);
            ResultSet resultSet = dataHandler.executeQuery(prepStat);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            int vehicleId = resultSet.getInt(3);
            String startParkId = resultSet.getString(4);
            String endParkId = resultSet.getString(5);
            Timestamp endTimeTimeStamp = resultSet.getTimestamp(6);
            LocalDateTime endTime = endTimeTimeStamp.toLocalDateTime();

            return new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a trip with a preset end park
     *
     * @param startTime   the start time of the trip
     * @param clientEmail the end time of the trip
     * @param startParkId the id of the start park
     * @param endParkId   the id of the end park
     * @param vehicleId   the id of the vehicle
     * @return a trip with all the arguments
     */
    public Trip createNewTrip(LocalDateTime startTime, String clientEmail, String startParkId,
            String endParkId, int vehicleId) {
        return new Trip(startTime, clientEmail, startParkId, endParkId, vehicleId);
    }


    /**
     * Loads a trip without a preset end
     *
     * @param startTime   the start time of the trip
     * @param clientEmail the end time of the trip
     * @param startParkId the id of the start park
     * @param vehicleId   the id of the vehicle
     * @return a trip with all the arguments
     */
    public Trip createNewTrip(LocalDateTime startTime, String clientEmail, String startParkId, int vehicleId) {
        return new Trip(startTime, clientEmail, startParkId, vehicleId);
    }


    /**
     * Loads a trip with all the arguments
     *
     * @param startTime   the start time of the trip
     * @param endTime     the end time of the trip
     * @param clientEmail the end time of the trip
     * @param startParkId the id of the start park
     * @param endParkId   the id of the end park
     * @param vehicleId   the id of the vehicle
     * @return a trip with all the arguments
     */
    public Trip createNewTrip(LocalDateTime startTime, LocalDateTime endTime, String clientEmail,
            String startParkId, String endParkId, int vehicleId) {
        return new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
    }
}
