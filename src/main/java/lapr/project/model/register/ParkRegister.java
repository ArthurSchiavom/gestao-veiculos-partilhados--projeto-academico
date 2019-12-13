/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.register;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.park.Capacity;
import lapr.project.model.park.Park;
import lapr.project.model.Vehicles.VehicleType;

/**
 * This class regist and manipulates the data of all parks in data base
 */
public class ParkRegister {

    DataHandler dataHandler;

    public ParkRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     *
     * Adds a park to the database
     *
     * @param name Park name
     * @param cord Park coordinates
     * @param park_descripton Description of the park
     * @param park_input_voltage Park input voltage
     * @param park_input_corrent Park input corrent
     * @param name park name
     * @param cord park coordinates
     * @return true if added with success the park, false otherwise
     */
    public boolean addPark(String name, Coordinates cord, String park_description, float park_input_voltage, float park_input_corrent) {
        try {
            double latitude = cord.getLatitude();
            double longitude = cord.getLongitude();
            PreparedStatement statement = dataHandler.prepareStatement("INSERT INTO PARKS(latitude, longitude, park_name,park_description,park_input_voltage,park_input_current) " + "VALUES(?,?,?,?,?,?)");
            System.out.println("aqui");
            dataHandler.setDouble(statement, 1, latitude);
            dataHandler.setDouble(statement, 2, longitude);
            dataHandler.setString(statement, 3, name);
            dataHandler.setString(statement, 4, park_description);
            dataHandler.setFloat(statement, 5, park_input_voltage);
            dataHandler.setFloat(statement, 6, park_input_corrent);
            int nrLines = dataHandler.executeUpdate(statement);
            if (nrLines == 0) {
                return false;
            }
            dataHandler.close(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Removes a park from database
     *
     * @param id a given id
     * @return return true if successfully removed and false otherwise
     */
    public boolean removeParkById(int id) {
        try {
            PreparedStatement statement = dataHandler.prepareStatement("DELETE FROM parks WHERE park_id=?;");
            dataHandler.setInt(statement, 1, id);
            ResultSet resultSet = dataHandler.executeQuery(statement);
            if (resultSet == null) {
                return false;
            }
            dataHandler.close(resultSet);
            dataHandler.close(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Fetch a parks with the name given by parameter
     *
     * @param name Name of park
     * @return Returns a list of parks with the same name (or just one)
     */
    public List<Park> fetchParkByName(String name) {
        int parkId;
        double latitude;
        double longitude;
        String park_description;
        float park_input_voltage;
        float park_input_corrent;
        int altitude;
        String parkName = name.toLowerCase().trim();
        List<Park> parksSameNameOrNot = new ArrayList<>();
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where lower(park_name)=?;");
            dataHandler.setString(statement, 1, name);
            ResultSet result = dataHandler.executeQuery(statement);
            if (result == null) {
                return null;
            }
            while (result.next()) {
                parkId = dataHandler.getInt(result, 1);
                latitude = dataHandler.getDouble(result, 2);
                longitude = dataHandler.getDouble(result, 3);
                parkName = dataHandler.getString(result, 4);
                park_description = dataHandler.getString(result, 5);
                park_input_voltage = dataHandler.getFloat(result, 7);
                park_input_corrent = dataHandler.getFloat(result, 8);
                altitude = getAltitude(latitude, longitude);
                Set<Capacity> listOfCapacitys = getListOfCapacitys(parkId);
                parksSameNameOrNot.add(new Park(parkName, new Coordinates(latitude, longitude, altitude), listOfCapacitys, parkId, park_description, park_input_voltage, park_input_corrent));
            }
            dataHandler.close(result);
            dataHandler.close(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return parksSameNameOrNot;
    }

    /**
     * Fetch a parkk by id
     *
     * @param id park id
     * @return return a park
     */
    public Park fetchParkById(int id) {
        String name;
        Coordinates cord;
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where park_id=?;");
            dataHandler.setInt(statement, 1, id);
            ResultSet resultSet = dataHandler.executeQuery(statement);
            if (resultSet == null ) {
                return null;
            }
            name = dataHandler.getString(resultSet, 1);
            cord = new Coordinates(dataHandler.getDouble(resultSet, 2), dataHandler.getDouble(resultSet, 3), dataHandler.getInt(resultSet, 4));
            String description = dataHandler.getString(resultSet, 5);
            float park_input_voltage = dataHandler.getFloat(resultSet, 6);
            float park_input_corrent = dataHandler.getFloat(resultSet, 7);
            return new Park(name, cord, getListOfCapacitys(id), id, description, park_input_voltage, park_input_corrent);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Return the altitude from a points_of_interest
     *
     * @param latitude latitude
     * @param longitude longitude
     * @return
     */
    private Integer getAltitude(double latitude, double longitude) {
        try {
            PreparedStatement statementForAltitude = dataHandler.prepareStatement("Select altitude from points_of_interest where latitude=? and longitude=?;");
            dataHandler.setDouble(statementForAltitude, 1, latitude);
            dataHandler.setDouble(statementForAltitude, 2, longitude);
            ResultSet resultAltitude = dataHandler.executeQuery(statementForAltitude);
            if (resultAltitude == null) {
                return null;
            }
            dataHandler.close(resultAltitude);
            dataHandler.close(statementForAltitude);
            return dataHandler.getInt(resultAltitude, 1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param parkId park id
     * @return return a list of capacitys (of different types)
     */
    private Set<Capacity> getListOfCapacitys(int parkId) {
        VehicleType vehicleType;
        Set<Capacity> capacity = new HashSet<>();
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from park_capacity where park_id=?");
            dataHandler.setDouble(statement, 1, parkId);
            ResultSet resultForCapacity = dataHandler.executeQuery(statement);
            if (resultForCapacity == null) {
                return null;
            }
            while (resultForCapacity.next()) {
                String vehicle_type_name = dataHandler.getString(resultForCapacity, 1);
                int park_capacity = dataHandler.getInt(resultForCapacity, 2);
                int amount_occupied = dataHandler.getInt(resultForCapacity, 3);
                if (vehicle_type_name.trim().equalsIgnoreCase(VehicleType.BICYCLE.toString())) {
                    vehicleType = VehicleType.BICYCLE;
                } else {
                    vehicleType = VehicleType.ELECTRIC_SCOOTER;
                }
                capacity.add(new Capacity(park_capacity, amount_occupied, vehicleType));
            }
            dataHandler.close(resultForCapacity);
            dataHandler.close(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return capacity;
    }

    /**
     * Update a park from database
     *
     * @param name of the park
     * @param coordinates of the park
     * @param vehicleCapacities of the park
     * @param id the park id
     * @param description of the park
     * @param parkInputVoltage the voltage of the park
     * @param parkInputCurrent the current corrent of the park
     * @return return true if successfully removed and false otherwise
     */
    public boolean UpdatePark(String name, Coordinates cord, Set<Capacity> vehicleCapacities, int id, String description, float parkInputVoltage, float parkInputCurrent) {
        try {
            double latitude = cord.getLatitude();
            double longintude = cord.getLongitude();

            PreparedStatement statement = dataHandler.prepareStatement("Update park SET park_id =? ,latitude=? ,longitude= ?,park_name= ?,park_description =?,park_input_voltage= ?,park_input_current =?  FROM park WHERE park_id=?");
            dataHandler.setInt(statement, 1, id);
            dataHandler.setDouble(statement, 2, latitude);
            dataHandler.setDouble(statement, 3, longintude);
            dataHandler.setString(statement, 4, name);
            dataHandler.setString(statement, 5, description);
            dataHandler.setFloat(statement, 6, parkInputVoltage);
            dataHandler.setFloat(statement, 7, parkInputCurrent);
            dataHandler.setInt(statement, 8, id);
            int nLinhas=dataHandler.executeUpdate(statement);
            if (nLinhas==0){
                return false;
            }
            dataHandler.close(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
