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
     *
     * Adds a park to the database
     *
     * @param cord Park coordinates
     * @param park_description Description of the park
     * @param park_input_voltage Park input voltage
     * @param park_input_corrent Park input corrent
     * @return true if added with success the park, false otherwise
     */
    public boolean addPark( Coordinates cord, String park_description, float park_input_voltage, float park_input_corrent) {
        try {
            double latitude = cord.getLatitude();
            double longitude = cord.getLongitude();
            PreparedStatement statement = dataHandler.prepareStatement("INSERT INTO PARKS(latitude, longitude,park_description,park_input_voltage,park_input_current) " + "VALUES(?,?,?,?,?)");
            statement.setDouble( 1, latitude);
            statement.setDouble( 2, longitude);
            statement.setString( 3, park_description);
            statement.setFloat( 4, park_input_voltage);
            statement.setFloat( 5, park_input_corrent);
            int nrLines = dataHandler.executeUpdate(statement);
            if (nrLines == 0) {
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
     * Removes a park from database
     *
     * @param id a given id
     * @return return true if successfully removed and false otherwise
     */
    public boolean removeParkById(int id) {
        try {
            PreparedStatement statement = dataHandler.prepareStatement("DELETE FROM parks WHERE park_id=?;");
            statement.setInt( 1, id);
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
     * Fetch a parks with the name given by parameter
     *
     * @param description Name of park
     * @return Returns a list of parks with the same name (or just one)
     */
    public List<Park> fetchParkByName(String description) {
        int parkId;
        double latitude;
        double longitude;
        String park_description;
        float park_input_voltage;
        float park_input_corrent;
        int altitude;
        String parkDescription = description.toLowerCase().trim();
        List<Park> parksSameNameOrNot = new ArrayList<>();
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where lower(park_name)=?;");
            statement.setString( 1, parkDescription);
            ResultSet result = dataHandler.executeQuery(statement);
            if (result == null) {
                return null;
            }
            while (result.next()) {
                parkId = result.getInt( 1);
                latitude = result.getDouble( 2);
                longitude = result.getDouble( 3);
                park_description = result.getString( 4);
                park_input_voltage = result.getFloat( 5);
                park_input_corrent = result.getFloat( 6);
                altitude = getAltitude(latitude, longitude);
                Set<Capacity> listOfCapacitys = getListOfCapacitys(parkId);
                parksSameNameOrNot.add(new Park(new Coordinates(latitude, longitude, altitude), listOfCapacitys, parkId, park_description, park_input_voltage, park_input_corrent));
            }
            result.close();
            statement.close();
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
        double latitude;
        double longitude;
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where park_id=?;");
            statement.setInt( 1, id);
            ResultSet resultSet = dataHandler.executeQuery(statement);
            if (resultSet == null ) {
                return null;
            }
            latitude=resultSet.getDouble( 1);
            longitude=resultSet.getDouble( 2);
            cord = new Coordinates(latitude,longitude, getAltitude(latitude, longitude));
            String description = resultSet.getString( 3);
            float park_input_voltage = resultSet.getFloat( 4);
            float park_input_corrent = resultSet.getFloat( 5);
            return new Park(cord, getListOfCapacitys(id), id, description, park_input_voltage, park_input_corrent);
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
            statementForAltitude.setDouble( 1, latitude);
            statementForAltitude.setDouble( 2, longitude);
            ResultSet resultAltitude = dataHandler.executeQuery(statementForAltitude);
            if (resultAltitude == null) {
                return null;
            }
            resultAltitude.close();
            statementForAltitude.close();
            return resultAltitude.getInt( 1);
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
            statement.setDouble( 1, parkId);
            ResultSet resultForCapacity = dataHandler.executeQuery(statement);
            if (resultForCapacity == null) {
                return null;
            }
            while (resultForCapacity.next()) {
                String vehicle_type_name = resultForCapacity.getString( 1);
                int park_capacity = resultForCapacity.getInt( 2);
                int amount_occupied = resultForCapacity.getInt( 3);
                
                //Aqui caso se adiciona outros tipos ..... faz se else if
                if (vehicle_type_name.trim().equalsIgnoreCase(VehicleType.BICYCLE.toString())) {
                    vehicleType = VehicleType.BICYCLE;
                } else {
                    vehicleType = VehicleType.ELECTRIC_SCOOTER;
                }
                capacity.add(new Capacity(park_capacity, amount_occupied, vehicleType));
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
     * @param name of the park
     * @param cord of the park
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

            PreparedStatement statement = dataHandler.prepareStatement("Update park SET park_id =? ,latitude=? ,longitude= ?,park_description =?,park_input_voltage= ?,park_input_current =?  FROM park WHERE park_id=?;");
            statement.setInt( 1, id);
            statement.setDouble( 2, latitude);
            statement.setDouble( 3, longintude);
            statement.setString( 4, description);
            statement.setFloat( 5, parkInputVoltage);
            statement.setFloat( 6, parkInputCurrent);
            statement.setInt( 7, id);
            int nLinhas=dataHandler.executeUpdate(statement);
            if (nLinhas==0){
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
