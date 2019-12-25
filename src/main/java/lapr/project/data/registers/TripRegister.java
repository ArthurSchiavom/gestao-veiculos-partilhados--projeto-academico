package lapr.project.data.registers;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.Trip;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            prepStat = dataHandler.prepareStatement(
                    "SELECT * FROM TRIPS where start_time=? AND user_email=?");
            autoCloseableManager.addAutoCloseable(prepStat);
            prepStat.setTimestamp( 1, Timestamp.valueOf(startTime));
            prepStat.setString( 2, clientEmail);
            ResultSet resultSet = dataHandler.executeQuery(prepStat);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            String vehicleDescription = resultSet.getString(3);
            String startParkId = resultSet.getString(4);
            String endParkId = resultSet.getString(5);
            Timestamp endTimeTimeStamp = resultSet.getTimestamp(6);
            LocalDateTime endTime = endTimeTimeStamp.toLocalDateTime();

            return new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleDescription);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     *  return the trip of the client
     * @param email of the client who its doing the trip
     * @return startTime of the trip
     */

    public Trip fetchUnfinishedTrip (String email){
        PreparedStatement prepStat = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            prepStat = dataHandler.prepareStatement("SELECT * FROM trips where user_email=? AND end_time=?");
            autoCloseableManager.addAutoCloseable(prepStat);
            prepStat.setString( 1, email);
            prepStat.setTimestamp(2, null);
            ResultSet resultSet = dataHandler.executeQuery(prepStat);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (resultSet == null || !resultSet.next() ) {
                return null;
            }
            Timestamp startTimeTimeStamp = resultSet.getTimestamp(1);
            LocalDateTime startTime = startTimeTimeStamp.toLocalDateTime();
            String startParkId = resultSet.getString(4);
            String endParkId = resultSet.getString(5);
            String vehicleId = resultSet.getString(3);
            LocalDateTime endDate = LocalDateTime.now();
            return new Trip(startTime,endDate,email,startParkId,endParkId,vehicleId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            autoCloseableManager.closeAutoCloseables();
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
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            prepStat = dataHandler.prepareStatement(
                    "INSERT INTO park_vehicle (PARK_ID, VEHICLE_DESCRIPTION) VALUES(?,?)");
            autoCloseableManager.addAutoCloseable(prepStat);
            prepStat.setString(1,trip.getEndParkId());
            prepStat.setString(2,trip.getVehicleDescription());

            dataHandler.commitTransaction();
            return true;
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     * Update to the trip in sql
     * @param trip of the client
     * @return
     */

    public boolean updateEndTrip(Trip trip){
        PreparedStatement prepStat = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            prepStat = dataHandler.prepareStatement(
                    "UPDATE TRIPS SET start_time =? ,user_email=?,VEHICLE_DESCRIPTION =?,start_park_id=?,end_park_id=?,end_time=?");
            autoCloseableManager.addAutoCloseable(prepStat);
            prepStat.setTimestamp(1,Timestamp.valueOf(trip.getStartTime()));
            prepStat.setString(2,trip.getClientEmail());
            prepStat.setString(3,trip.getVehicleDescription());
            prepStat.setString(4,trip.getStartParkId());
            prepStat.setString(5,trip.getEndParkId());
            prepStat.setTimestamp(6,Timestamp.valueOf(trip.getEndTime()));
            dataHandler.commitTransaction();
            return true;
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
        } finally {
            autoCloseableManager.closeAutoCloseables();
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
            String endParkId, String vehicleId) {
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
    public Trip createNewTrip(LocalDateTime startTime, String clientEmail, String startParkId, String vehicleId) {
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
     * @param vehicleDescription   the id of the vehicle
     * @return a trip with all the arguments
     */
    public Trip createNewTrip(LocalDateTime startTime, LocalDateTime endTime, String clientEmail,
            String startParkId, String endParkId, String vehicleDescription) {
        return new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleDescription);
    }

    /**
     * returns a list of vehicles that are currently being used
     * @param startTime
     * @return
     */


    public List<Trip> getListOfVehiclesNotAvailable(LocalDateTime startTime,LocalDateTime endTime) {
        List<Trip> dispVehicles  = new ArrayList<>();
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from trips  WHERE (? >= start_time AND ? < nvl(end_time, sysdate)) OR (? > start_time AND ? <= nvl(end_time, sysdate) ) OR (? >= nvl(end_time, sysdate) AND ? <= start_time)");
            autoCloseableManager.addAutoCloseable(statement);
            statement.setTimestamp(1,Timestamp.valueOf(startTime));
            statement.setTimestamp(2,Timestamp.valueOf(startTime));
            statement.setTimestamp(3,Timestamp.valueOf(endTime));
            statement.setTimestamp(4,Timestamp.valueOf(endTime));
            statement.setTimestamp(5,Timestamp.valueOf(endTime));
            statement.setTimestamp(6,Timestamp.valueOf(startTime));
            ResultSet resultVehicles = dataHandler.executeQuery(statement);
            autoCloseableManager.addAutoCloseable(resultVehicles);
            if (resultVehicles == null) {
                return dispVehicles;
            }
            while (resultVehicles.next()) {
                Timestamp startT = resultVehicles.getTimestamp("start_time");
                LocalDateTime start_time = startT.toLocalDateTime();
                String userEmail = resultVehicles.getString("user_email");
                String vehicleDescription = resultVehicles.getString("VEHICLE_DESCRIPTION");
                String startParkId = resultVehicles.getString("start_park_id");

                Trip trip = new Trip(start_time,null,userEmail,startParkId,null,vehicleDescription);

               dispVehicles.add(trip);
            }
        } catch (SQLException e) {
            return dispVehicles;
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return dispVehicles;
    }
}
