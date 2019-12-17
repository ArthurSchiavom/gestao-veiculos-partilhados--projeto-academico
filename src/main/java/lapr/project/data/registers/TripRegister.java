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
     *  return the trip of the client
     * @param email of the client who its doing the trip
     * @return startTime of the trip
     */

    public Trip fetchUnfinishedTrip (String email){
        PreparedStatement prepStat = null;
        try {
            prepStat = dataHandler.prepareStatement(
                    "SELECT * FROM trip where user_email=? AND end_time=? ");
            prepStat.setString( 1, email);
            prepStat.setTimestamp(2, null);
            ResultSet resultSet = dataHandler.executeQuery(prepStat);
            if (resultSet == null || !resultSet.next() ) {
                return null;
            }
            Timestamp startTimeTimeStamp = resultSet.getTimestamp(1);
            LocalDateTime startTime = startTimeTimeStamp.toLocalDateTime();
            String startParkId = resultSet.getString(4);
            String endParkId = resultSet.getString(5);
            int vehicleId = resultSet.getInt(3);
            LocalDateTime endDate = LocalDateTime.now();
            return new Trip(startTime,endDate,email,startParkId,endParkId,vehicleId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * update in park_vehicle sql
     * @param email of the client
     */

    public boolean updateReturnVehicle(String email){
        PreparedStatement prepStat = null;
        Trip trip = fetchUnfinishedTrip (email);
        updateEndTrip(trip);
        try {
            prepStat = dataHandler.prepareStatement(
                    "INSERT INTO park_vehicle  park_id =?,vehicle_id=?" + "VALUES(?,?)");
            prepStat.setString(1,trip.getEndParkId());
            prepStat.setInt(2,trip.getVehicleId());
            return true;
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Update to the trip in sql
     * @param trip of the client
     * @return
     */

    public boolean updateEndTrip(Trip trip){
        PreparedStatement prepStat = null;
        try {
            prepStat = dataHandler.prepareStatement(
                    "UPDATE TRIP SET start_time =? ,user_email=?,vehicle_id =?,start_park_id=?,end_park_id=?,end_time=?");
            prepStat.setTimestamp(1,Timestamp.valueOf(trip.getStartTime()));
            prepStat.setString(2,trip.getClientEmail());
            prepStat.setInt(3,trip.getVehicleId());
            prepStat.setString(4,trip.getStartParkId());
            prepStat.setString(5,trip.getEndParkId());
            prepStat.setTimestamp(6,Timestamp.valueOf(trip.getEndTime()));
            return true;
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
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
