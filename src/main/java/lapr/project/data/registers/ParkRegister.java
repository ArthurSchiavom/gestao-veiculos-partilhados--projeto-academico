/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Capacity;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.VehicleType;

/**
 * This class regist and manipulates the data of all parks in data base
 */
public class ParkRegister {
    private static final Logger LOGGER = Logger.getLogger("ParkRegisterLog");
    private final DataHandler dataHandler;

    public ParkRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Adds a park to the database
     *
     * @param coord            Park coordinates
     * @param parkInputVoltage Park input voltage
     * @param parkInputCurrent Park input current
     * @return true if added with success the park, false otherwise
     */
    public void addPark(String id, String description, Coordinates coord, float parkInputVoltage, float parkInputCurrent, int maxEletricScooters, int maxBicycles) throws SQLException {
        try (CallableStatement cs = dataHandler.prepareCall("{call register_park(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) { // ensure everything is done with one request to the database => ensure database information consistency
            cs.setString(1, id);
            cs.setDouble(2, coord.getLatitude());
            cs.setDouble(3, coord.getLongitude());
            cs.setInt(4, coord.getAltitude());
            cs.setString(5, description);
            cs.setFloat(6, parkInputVoltage);
            cs.setFloat(7, parkInputCurrent);
            cs.setInt(8, maxEletricScooters);
            cs.setInt(9, maxBicycles);
            dataHandler.execute(cs);
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            throw e;
        }

    }

    /**
     * Removes a park from database
     *
     * @param id a given id
     * @return return true if successfully removed and false otherwise
     */
    public void setAvailability(String id, boolean newStatus) throws SQLException {
        try (PreparedStatement ps = dataHandler.prepareStatement("UPDATE parks SET available = ? WHERE park_id = ?")) {
            int newStatusInt = newStatus ? 1 : 0;
            ps.setInt(1, newStatusInt);
            ps.setString(2, id);
            dataHandler.executeUpdate(ps);
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Fetch a parkk by id
     *
     * @param id park id
     * @return return a park
     */
    public Park fetchParkById(String id) {
        Coordinates coord;
        double latitude;
        double longitude;
        int altitude;
        float parkInputVoltage;
        float parkInputCorrent;
        try {
            PreparedStatement ps = dataHandler.prepareStatement("Select * from parks p, points_of_interest poi where park_id=? AND p.latitude = poi.latitude AND p.longitude = poi.longitude");
            ps.setString(1, id);
            ResultSet rs = dataHandler.executeQuery(ps);
            if (rs == null || !rs.next()) {
                return null;
            }
            latitude = rs.getDouble("latitude");
            longitude = rs.getDouble("longitude");
            altitude = rs.getInt("altitude_m");
            coord = new Coordinates(latitude, longitude, altitude);
            parkInputVoltage = rs.getFloat("park_input_voltage");
            parkInputCorrent = rs.getFloat("park_input_current");
            return new Park(id, parkInputVoltage, parkInputCorrent, getListOfCapacities(id), rs.getString("poi_description"), coord);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param parkId park id
     * @return return a list of capacitys (of different types)
     */
    private List<Capacity> getListOfCapacities(String parkId) throws SQLException {
        VehicleType vehicleType;
        List<Capacity> capacity = new ArrayList<>();
        int parkCapacity;
        int amountOccupied;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = dataHandler.prepareStatement("Select * from park_capacity where park_id=?");
            ps.setString(1, parkId);
            rs = dataHandler.executeQuery(ps);
            if (rs == null) {
                return null;
            }
            while (rs.next()) {
                String vehicleTypeName = rs.getString("vehicle_type_name");
                parkCapacity = rs.getInt("park_capacity");
                amountOccupied = rs.getInt("amount_occupied");
                vehicleType = VehicleType.parseVehicleType(vehicleTypeName);
                capacity.add(new Capacity(parkCapacity, amountOccupied, vehicleType));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "failed to close result set.");
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "failed to close prepared statement.");
                }
            }
        }
        return capacity;
    }

    public void updatePark(String id, String description, Coordinates coord, Float parkInputVoltage, Float parkInputCurrent, Integer maxEletricScooters, Integer maxBicycles) {

    }

    /**
     * Loads a list of parks that exist in sql table
     *
     * @return list of all parks that exist in sql table 'Parks'
     */
    private List<Park> fetchAllParks() {
        List<Park> parkList = new ArrayList<>();
        PreparedStatement stm = null;
        double latitude;
        double longitude;
        int altitude;
        Coordinates coord;
        float parkInputVoltage;
        float parkInputCorrent;
        String parkId;
        try {
            stm = dataHandler.prepareStatement("Select * from parks p, points_of_interest poi WHERE p.latitude = poi.latitude AND p.longitude = poi.longitude");
            ResultSet rs = dataHandler.executeQuery(stm);
            if (rs == null) {
                return null;
            }
            while (rs.next()) { // if it has next
                parkId = rs.getString("park_id");
                latitude = rs.getDouble("latitude");
                longitude = rs.getDouble("longitude");
                altitude = rs.getInt("altitude_m");
                coord = new Coordinates(latitude, longitude, altitude);
                parkInputVoltage = rs.getFloat("park_input_voltage");
                parkInputCorrent = rs.getFloat("park_input_current");
                parkList.add(new Park(parkId, parkInputVoltage, parkInputCorrent, getListOfCapacities(parkId), rs.getString("poi_description"), coord));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return parkList;
    }

    /**
     * Gets the nearest parks from a certain coordinate and returns their availability
     *
     * <h1></h1>We are running through every park in the sql table instead of parsing it to a tree because, it has a smaller complexity initially
     * and if we were to need the tree again, it could be outdated by then, so we would have to again load the tree with all the information
     * in the table, making it higher complexity than simply iterating all of them</h1>
     *
     * @param coords
     * @param radius
     * @return hashmap containing parks and their corresponding capacities
     */
    public HashMap<Park, List<Capacity>> getNearestParksAndAvailability(Coordinates coords, double radius) throws SQLException {
        HashMap<Park, List<Capacity>> nearestParksAvailability = new HashMap<>();
        for (Park park : fetchAllParks()) {
            if (coords.distance(park.getCoordinates()) <= radius) {
                nearestParksAvailability.put(park, getListOfCapacities(park.getId()));
            }
        }
        return nearestParksAvailability;
    }
}
