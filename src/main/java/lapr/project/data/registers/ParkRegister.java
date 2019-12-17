/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.*;
import java.util.*;

import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Capacity;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.VehicleType;

/**
 * This class regist and manipulates the data of all parks in data base
 */
public class ParkRegister {
    private DataHandler dataHandler;

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
    public void addPark(String id, String description, Coordinates coord, float parkInputVoltage, float parkInputCurrent) throws SQLException {
        try (CallableStatement cs = dataHandler.prepareCall("{call register_park(?, ?, ?, ?, ?, ?, ?)}")) {
            cs.setString(1, id);
            cs.setDouble(2, coord.getLatitude());
            cs.setDouble(3, coord.getLongitude());
            cs.setInt(4, coord.getAltitude());
            cs.setString(5, description);
            cs.setFloat(6, parkInputVoltage);
            cs.setFloat(7, parkInputCurrent);
            dataHandler.execute(cs);
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
    public boolean removeParkById(int id) {
        try {
            PreparedStatement statement = dataHandler.prepareStatement("DELETE FROM PARKS WHERE PARK_ID=?");
            statement.setInt(1, id);
            ResultSet resultSet = dataHandler.executeQuery(statement);
            if (resultSet == null) {
                return false;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
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
            latitude = rs.getDouble(2);
            longitude = rs.getDouble(3);
            altitude = rs.getInt(8);
            coord = new Coordinates(latitude, longitude, altitude);
            parkInputVoltage = rs.getFloat(4);
            parkInputCorrent = rs.getFloat(5);
            return new Park(id, parkInputVoltage, parkInputCorrent, getListOfCapacities(id), rs.getString(9), coord);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param parkId park id
     * @return return a list of capacitys (of different types)
     */
    private List<Capacity> getListOfCapacities(String parkId) {
        VehicleType vehicleType;
        List<Capacity> capacity = new ArrayList<>();
        int parkCapacity;
        int amountOccupied;
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from park_capacity where park_id=?");
            statement.setString(1, parkId);
            ResultSet resultForCapacity = dataHandler.executeQuery(statement);
            if (resultForCapacity == null) {
                return null;
            }
            while (resultForCapacity.next()) {
                String vehicleTypeName = resultForCapacity.getString("vehicle_type_name");
                parkCapacity = resultForCapacity.getInt("park_capacity");
                amountOccupied = resultForCapacity.getInt("amount_occupied");
                if (vehicleTypeName.trim().equalsIgnoreCase(VehicleType.BICYCLE.getSQLName())) {
                    vehicleType = VehicleType.BICYCLE;
                } else {
                    vehicleType = VehicleType.ELECTRIC_SCOOTER;
                }
                capacity.add(new Capacity(parkCapacity, amountOccupied, vehicleType));
        }
            resultForCapacity.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return capacity;
    }

    /**
     * Update a park from database
     *
     * @param cord              of the park
     * @param vehicleCapacities of the park
     * @param id                the park id
     * @param parkInputVoltage  the voltage of the park
     * @param parkInputCurrent  the current corrent of the park
     * @return return true if successfully removed and false otherwise
     */
    public boolean updatePark(Coordinates cord, Set<Capacity> vehicleCapacities, int id, float parkInputVoltage, float parkInputCurrent) {
        try {
            double latitude = cord.getLatitude();
            double longintude = cord.getLongitude();

            PreparedStatement statement = dataHandler.prepareStatement("Update park SET park_id =? ,latitude=? ,longitude= ?,park_input_voltage= ?,park_input_current =?  FROM park WHERE park_id=?");
            statement.setObject(1, "hel");
            statement.setInt(1, id);
            statement.setDouble(2, latitude);
            statement.setDouble(3, longintude);
            statement.setFloat(4, parkInputVoltage);
            statement.setFloat(5, parkInputCurrent);
            statement.setInt(6, id);
            int nLinhas = dataHandler.executeUpdate(statement);
            if (nLinhas == 0) {
                return false;
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Loads a list of parks that exist in sql table
     * @return list of all parks that exist in sql table 'Parks'
     */
    private List<Park> fetchAllParks(){
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
            while(rs.next()){ // if it has next
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
    public HashMap<Park,List<Capacity>> getNearestParksAndAvailability(Coordinates coords, double radius){
        HashMap<Park,List<Capacity>> nearestParksAvailability = new HashMap<>();
        for(Park park : fetchAllParks()){
            if(coords.distance(park.getCoordinates()) <= radius){
                nearestParksAvailability.put(park, getListOfCapacities(park.getId()));
            }
        }
        return nearestParksAvailability;
    }
}
