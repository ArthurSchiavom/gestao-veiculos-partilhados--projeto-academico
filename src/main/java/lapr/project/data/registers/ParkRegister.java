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
    DataHandler dataHandler;

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
            return new Park(id, parkInputVoltage, parkInputCorrent, getListOfCapacitys(id), rs.getString(9), coord);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param parkId park id
     * @return return a list of capacitys (of different types)
     */
    private List<Capacity> getListOfCapacitys(String parkId) {
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
                String vehicle_type_name = resultForCapacity.getString(1);
                parkCapacity = resultForCapacity.getInt(2);
                amountOccupied = resultForCapacity.getInt(3);
                if (vehicle_type_name.trim().equalsIgnoreCase(VehicleType.BICYCLE.getSQLName())) {
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
}
