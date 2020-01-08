package lapr.project.data.registers;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.data.Emailer;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.Invoice;
import lapr.project.model.Trip;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.users.User;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.utils.UnregisteredDataException;
import lapr.project.utils.Utils;
import lapr.project.utils.physics.calculations.PhysicsMethods;

import javax.mail.MessagingException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TripAPI {
    private DataHandler dataHandler;

    public TripAPI(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<Trip> fetchAllClientTrips(String clientEmail) throws SQLException {
        List<Trip> allClientTrips = new ArrayList<>();
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dataHandler.prepareStatement("select * from TRIPS where USER_EMAIL = ?");
            autoCloseableManager.addAutoCloseable(preparedStatement);
            preparedStatement.setString(1, clientEmail);

            ResultSet resultSet = dataHandler.executeQuery(preparedStatement);
            autoCloseableManager.addAutoCloseable(resultSet);

            while (resultSet.next()) {
                allClientTrips.add(new Trip(resultSet.getTimestamp("start_time"),
                        resultSet.getTimestamp("end_time"),
                        clientEmail,
                        resultSet.getString("start_park_id"),
                        resultSet.getString("end_park_id"),
                        resultSet.getString("vehicle_description")));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to access database when fetching trips", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }

        return allClientTrips;
    }

    /**
     * Fetches a trip from the database
     *
     * @param clientEmail the email of the client
     * @param startTime   the start time of the trip
     * @return a trip object with data from the database
     */
    public Trip fetchTrip(String clientEmail, Timestamp startTime) {
        PreparedStatement prepStat = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            prepStat = dataHandler.prepareStatement(
                    "SELECT * FROM TRIPS where start_time=? AND user_email=?");
            autoCloseableManager.addAutoCloseable(prepStat);
            prepStat.setTimestamp(1, startTime);
            prepStat.setString(2, clientEmail);
            ResultSet resultSet = dataHandler.executeQuery(prepStat);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            String vehicleDescription = resultSet.getString(3);
            String startParkId = resultSet.getString(4);
            String endParkId = resultSet.getString(5);
            Timestamp endTime = resultSet.getTimestamp(6);

            return new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleDescription);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    public Trip fetchUnfinishedTrip(String email) throws SQLException {
        PreparedStatement prepStat = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            prepStat = dataHandler.prepareStatement("SELECT * FROM trips where user_email=? AND end_time is null");
            autoCloseableManager.addAutoCloseable(prepStat);
            prepStat.setString(1, email);
            ResultSet resultSet = dataHandler.executeQuery(prepStat);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (!resultSet.next()) {
                return null;
            }
            Timestamp startTime = resultSet.getTimestamp("start_time");
            String startParkId = resultSet.getString("start_park_id");
            String endParkId = resultSet.getString("end_park_id");
            String vehicleDescription = resultSet.getString("vehicle_description");
            Timestamp endTime = resultSet.getTimestamp("end_time");
            return new Trip(startTime, endTime, email, startParkId, endParkId, vehicleDescription);
        } catch (SQLException ex) {
            throw new SQLException("database access error", ex.getSQLState(), ex.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
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
    public Trip createNewTrip(Timestamp startTime, String clientEmail, String startParkId, String vehicleId) {
        return new Trip(startTime, clientEmail, startParkId, vehicleId);
    }


    /**
     * Loads a trip with all the arguments
     *
     * @param startTime          the start time of the trip
     * @param endTime            the end time of the trip
     * @param clientEmail        the end time of the trip
     * @param startParkId        the id of the start park
     * @param endParkId          the id of the end park
     * @param vehicleDescription the id of the vehicle
     * @return a trip with all the arguments
     */
    public Trip createNewTrip(Timestamp startTime, Timestamp endTime, String clientEmail,
                              String startParkId, String endParkId, String vehicleDescription) {
        return new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleDescription);
    }

    /**
     * returns a list of vehicles that are currently being used
     *
     * @param startTime
     * @return
     */


    public List<Trip> getListOfVehiclesNotAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        List<Trip> dispVehicles = new ArrayList<>();
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from trips  WHERE (? >= start_time AND ? < nvl(end_time, sysdate)) OR (? > start_time AND ? <= nvl(end_time, sysdate) ) OR (? >= nvl(end_time, sysdate) AND ? <= start_time)");
            autoCloseableManager.addAutoCloseable(statement);
            statement.setTimestamp(1, Timestamp.valueOf(startTime));
            statement.setTimestamp(2, Timestamp.valueOf(startTime));
            statement.setTimestamp(3, Timestamp.valueOf(endTime));
            statement.setTimestamp(4, Timestamp.valueOf(endTime));
            statement.setTimestamp(5, Timestamp.valueOf(endTime));
            statement.setTimestamp(6, Timestamp.valueOf(startTime));
            ResultSet resultVehicles = dataHandler.executeQuery(statement);
            autoCloseableManager.addAutoCloseable(resultVehicles);
            if (resultVehicles == null) {
                return dispVehicles;
            }
            while (resultVehicles.next()) {
                Timestamp start_time = resultVehicles.getTimestamp("start_time");
                String userEmail = resultVehicles.getString("user_email");
                String vehicleDescription = resultVehicles.getString("VEHICLE_DESCRIPTION");
                String startParkId = resultVehicles.getString("start_park_id");

                Trip trip = new Trip(start_time, null, userEmail, startParkId, null, vehicleDescription);

                dispVehicles.add(trip);
            }
        } catch (SQLException e) {
            return dispVehicles;
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return dispVehicles;
    }

    private int registerNewTripNoCommit(String userEmail, String vehicleDescription, String startParkId) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement preparedStatement = dataHandler.prepareStatement("INSERT INTO TRIPS(start_time, user_email, vehicle_description, start_park_id)" +
                    "VALUES(current_timestamp, ?, ?, ?)");
            autoCloseableManager.addAutoCloseable(preparedStatement);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setString(2, vehicleDescription);
            preparedStatement.setString(3, startParkId);

            return dataHandler.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            throw new SQLException("Failed to access the database", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     * Unlocks a vehicle and starts a new trip for the user.
     *
     * @param username           user unlocking the vehicle
     * @param vehicleDescription vehicle being unlocked
     */
    public void startTrip(String username, String vehicleDescription) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        String parkId = null;
        try {
            Company company = Company.getInstance();
            ParkAPI parkAPI = company.getParkAPI();
            parkId = parkAPI.fetchParkIdVehicleIsIn(vehicleDescription);
            if (parkId == null)
                throw new SQLException("Vehicle is not in any park.");
            Client client = company.getUserAPI().fetchClientByUsername(username);
            if (client == null)
                throw new SQLException("Client doesn't exist.");

            // 1. unlock
            parkAPI.unlockVehicleNoCommit(vehicleDescription); // done
            // 2. create trip
            registerNewTripNoCommit(client.getEmail(), vehicleDescription, parkId);
            // 3. set user status to is riding
            company.getUserAPI().updateClientIsRidingNoCommit(username, true);
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {
                dataHandler.rollbackTransaction();
            } catch (SQLException e2) {
            }
            ;
            throw new SQLException("Failed to start a new trip: " + e.getMessage(), e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     * Finds the user that is currently riding a given vehicle.
     *
     * @param vehicleDescription vehicle to search for
     * @return (1) email of the user riding the given vehicle or (2) null if no one is riding that vehicle
     * @throws SQLException if a database access error occurs
     */
    public String fetchUserEmailRiding(String vehicleDescription) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        CallableStatement cs = null;
        try {
            cs = dataHandler.prepareCall("{? = call find_user_email_riding(?)}");
            autoCloseableManager.addAutoCloseable(cs);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setString(2, vehicleDescription);
            dataHandler.executeUpdate(cs);
            return cs.getString(1);
        } catch (SQLException e) {
            if (e.getErrorCode() != DataHandler.ORA_ERROR_CODE_NO_DATA_FOUND)
                throw new SQLException("Failed to access the database", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return null;
    }

    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    //TODO - TEST
    /**
     * Locks a vehicle and ends a trip if there is one by setting the user status to not riding,
     * updating the trip information, updating user points, updating user debt and emailing the user.
     *
     * @param parkId             id of the park where the vehicle is inserted
     * @param vehicleDescription vehicle0s description
     * @param sendEmail if the application should email the user that was in a trip with that vehicle is appliable
     * @return (1) true if success, (2) if the vehicle and parkId can't be associated
     * @throws SQLException       if a database access error occurs
     * @throws MessagingException if there is an on-going trip with the vehicle and the system fails to email the user
     * @return true if the park-vehicle association was possible and false otherwise
     */
    public boolean lockVehicle(String parkId, String vehicleDescription, boolean sendEmail) throws SQLException, MessagingException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();

        String clientEmail = fetchUserEmailRiding(vehicleDescription);
        Trip trip = null;
        if (clientEmail != null) {
            trip = Company.getInstance().getTripAPI().fetchUnfinishedTrip(clientEmail);
        }

        int nLinesChanged = -1;
        try {
            PreparedStatement ps = dataHandler.prepareStatement("Insert into park_vehicle(park_id, vehicle_description) values (?,?)");
            closeableManager.addAutoCloseable(ps);
            ps.setString(1, parkId);
            ps.setString(2, vehicleDescription);
            nLinesChanged = dataHandler.executeUpdate(ps);
            if (nLinesChanged == 0)
                return false;

            dataHandler.commitTransaction();

            if (sendEmail && clientEmail != null) {
                long duration = trip.getTripDurationMillis() / 60000;
                Emailer.sendEmailCurrentThread(clientEmail, "Trip end", "Your vehicle was successfully locked! Trip duration: " + duration + " min");
            }
            return true;
        } catch (SQLException e) {
            try {
                dataHandler.rollbackTransaction();
            } catch (SQLException e2) {
            }

            if (nLinesChanged == 0)
                throw e;
            throw new SQLException("Failed to return vehicle to park", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    public List<Trip> fetchUserTripsInDebt(String username) throws SQLException, UnregisteredDataException {
        List<Trip> allTripsInDebt = new ArrayList<>();
        UserAPI userAPI = Company.getInstance().getUserAPI();
        InvoiceAPI invoiceAPI = Company.getInstance().getInvoiceAPI();

        User user = userAPI.fetchClientByUsername(username);
        if (user == null)
            throw new UnregisteredDataException("user " + username + " does not exist.");

        String userEmail = user.getEmail();
        // FETCH UNPAID INVOICES
        List<Invoice> unpaidInvoices = invoiceAPI.fetchUnpaidInvoicesFor(userEmail);
        long[] startInvoicesDateEpochSeconds = new long[unpaidInvoices.size()];
        long[] endInvoicesDateEpochSeconds = new long[unpaidInvoices.size()];
        for (int i = 0; i < unpaidInvoices.size(); i++) {
            Date invoiceStartDate = unpaidInvoices.get(i).getPaymentStartDate();
            startInvoicesDateEpochSeconds[i] = invoiceStartDate.getTime();
            endInvoicesDateEpochSeconds[i] = invoiceStartDate.toLocalDate().plusMonths(1).toEpochDay() * 86400;
        }

        // FETCH TRIPS THAT ADDED UP TO THOSE INVOICES
        List<Trip> allClientTrips = fetchAllClientTrips(userEmail);
        for (Trip trip : allClientTrips) {
            if (trip.calculateTripCost() == 0)
                continue;

            long tripEndTimeEpochSeconds = (trip.getEndTime().getTime() / 1000);

            int i = 0;
            boolean tripIsInDebt = false;
            while (i < startInvoicesDateEpochSeconds.length && !tripIsInDebt) {
                if (tripEndTimeEpochSeconds < endInvoicesDateEpochSeconds[i]
                        && tripEndTimeEpochSeconds > startInvoicesDateEpochSeconds[i])
                    tripIsInDebt = true;

                i++;
            }
            if (tripIsInDebt)
                allTripsInDebt.add(trip);
        }

        return allTripsInDebt;
    }

    public List<Trip> fetchUserTripsLastInvoice(String username, boolean onlyUnpaid) throws SQLException, UnregisteredDataException {
        List<Trip> allTripsLastInvoice = new ArrayList<>();
        UserAPI userAPI = Company.getInstance().getUserAPI();
        InvoiceAPI invoiceAPI = Company.getInstance().getInvoiceAPI();

        User user = userAPI.fetchClientByUsername(username);
        if (user == null)
            throw new UnregisteredDataException("user " + username + " does not exist.");

        String userEmail = user.getEmail();
        // FETCH LATEST INVOICES
        Invoice lastInvoice = invoiceAPI.fetchLastInvoiceFor(userEmail);
        Date invoiceStartDate = lastInvoice.getPaymentStartDate();
        long startInvoicesDateEpochSeconds = invoiceStartDate.getTime();
        long endInvoicesDateEpochSeconds = invoiceStartDate.toLocalDate().plusMonths(1).toEpochDay() * 86400;

        // FETCH TRIPS THAT ADDED UP TO THIS INVOICE
        List<Trip> allClientTrips = fetchAllClientTrips(userEmail);
        for (Trip trip : allClientTrips) {
            if (onlyUnpaid && trip.calculateTripCost() == 0)
                continue;

            long tripEndTimeEpochSeconds = (trip.getEndTime().getTime() / 1000);

            boolean shouldAddTrip = false;
            if (tripEndTimeEpochSeconds < endInvoicesDateEpochSeconds
                    && tripEndTimeEpochSeconds > startInvoicesDateEpochSeconds)
                shouldAddTrip = true;
            if (shouldAddTrip)
                allTripsLastInvoice.add(trip);
        }

        return allTripsLastInvoice;
    }

    /**
     * Calculates the amount of points this trip gave to the user.
     *
     * @return amount of points this trip gave to the user; 0 if it didn't end yet
     */
    public int calculatePointsGivenToUser(Trip trip) throws SQLException {
        Timestamp endTime = trip.getEndTime();
        if (endTime == null)
            return 0;
        Timestamp startTime = trip.getStartTime();

        ParkAPI parkAPI = Company.getInstance().getParkAPI();

        int points = 0;
        long durationMin = (endTime.getTime() - startTime.getTime()) / 60000;
        if (durationMin < 15)
            points += 5;

        Park startPark = parkAPI.fetchParkById(trip.getStartParkId());
        Park endPark = parkAPI.fetchParkById(trip.getEndParkId());
        int altitudeDiff = endPark.getCoordinates().getAltitude() - startPark.getCoordinates().getAltitude();
        if (altitudeDiff > 50)
            points += 15;
        else if (altitudeDiff > 25)
            points += 5;

        return points;
    }

    /**
     * Finds the trip a vehicle is in.
     *
     * @param vehicleDescription vehicle description
     * @return (1) trip a vehicle is in or (2) null if the vehicle is not in a trip
     * @throws SQLException if a database access error occurs
     */
    public Trip fetchTripVehicleIsIn(String vehicleDescription) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement preparedStatement = dataHandler.prepareStatement("select user_email from trips where vehicle_description = ? and end_park_id is null");
            autoCloseableManager.addAutoCloseable(preparedStatement);
            preparedStatement.setString(1, vehicleDescription);
            ResultSet resultSet = dataHandler.executeQuery(preparedStatement);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (!resultSet.next())
                return null;

            String userEmail = resultSet.getString(1);
            return fetchUnfinishedTrip(userEmail);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("failed to fetch vehicle from the database", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     * Calculates the amount of calories burnt between two parks
     * @param username
     * @param vehicleDescription
     * @param latA
     * @param lonA
     * @param latB
     * @param lonB
     * @return
     * @throws SQLException 
     */
    public static double predictCalories(String username, String vehicleDescription, double latA, double lonA, double latB, double lonB) throws SQLException {
            Client client = Company.getInstance().getUserAPI().fetchClientByUsername(username);
            Bicycle bicycle = (Bicycle) Company.getInstance().getVehicleAPI().fetchVehicle(vehicleDescription);
            PointOfInterest poiA = Company.getInstance().getPoiAPI().fetchPoi(latA, lonA);
            PointOfInterest poiB = Company.getInstance().getPoiAPI().fetchPoi(latB, lonB);

        LinkedList<PointOfInterest> prevision = new LinkedList<>();
        double energySpent = MapGraphAlgorithms.shortestPath(Company.getInstance().initializeEnergyGraph(client, bicycle), poiA, poiB, prevision);
        return PhysicsMethods.convertJouleToCal(energySpent);
    }

    public List<Trip> fetchTripsForInvoice(Invoice invoice, boolean onlyUnpaid) throws SQLException {
        List<Trip> allTripsLastInvoice = new ArrayList<>();

        Date invoiceStartDate = invoice.getPaymentStartDate();
        long startInvoicesDateEpochSeconds = invoiceStartDate.getTime();
        long endInvoicesDateEpochSeconds = invoiceStartDate.toLocalDate().plusMonths(1).toEpochDay() * 86400000;

        // FETCH TRIPS THAT ADDED UP TO THIS INVOICE
        List<Trip> allClientTrips = fetchAllClientTrips(invoice.getClientEmail());
        for (Trip trip : allClientTrips) {
            if (onlyUnpaid && trip.calculateTripCost() == 0)
                continue;

            long tripEndTimeEpochSeconds = trip.getEndTime().getTime();

            boolean shouldAddTrip = false;
            if (tripEndTimeEpochSeconds < endInvoicesDateEpochSeconds
                    && tripEndTimeEpochSeconds > startInvoicesDateEpochSeconds)
                shouldAddTrip = true;
            if (shouldAddTrip)
                allTripsLastInvoice.add(trip);
        }

        return allTripsLastInvoice;
    }
}
