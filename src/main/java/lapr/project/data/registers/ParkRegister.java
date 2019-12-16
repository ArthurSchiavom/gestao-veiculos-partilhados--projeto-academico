/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
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
     * @param parkInputVoltage Park input voltage
     * @param parkInputCorrent Park input corrent
     * @return true if added with success the park, false otherwise
     */
    public boolean addPark( Coordinates cord, float parkInputVoltage, float parkInputCorrent) {
        double latitude;
        double longitude;
        int nrLines;
        try {
            latitude = cord.getLatitude();
            longitude = cord.getLongitude();
            PreparedStatement statement = dataHandler.prepareStatement("INSERT INTO PARKS(latitude, longitude,park_input_voltage,park_input_current) " + "VALUES(?,?,?,?)");
            statement.setDouble( 1, latitude);
            statement.setDouble( 2, longitude);
            statement.setFloat( 3, parkInputVoltage);
            statement.setFloat( 4, parkInputCorrent);
            nrLines = dataHandler.executeUpdate(statement);
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

//    /**
//     * Fetch a parks with the description given by parameter
//     *
//     * @param description Name of park
//     * @return Returns a list of parks with the same name (or just one)
//     */
//    public List<Park> fetchParkByDescription(String description) {
//        int parkId;
//        double latitude;
//        double longitude;
//        String park_description;
//        float parkInputVoltage;
//        float parkInputCorrent;
//        int altitude;
//        String parkDescription = description.toLowerCase().trim();
//        List<Park> parksSameNameOrNot = new ArrayList<>();
//        try {
//            PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where lower(park_description)=?");
//            statement.setString( 1, parkDescription);
//            ResultSet result = dataHandler.executeQuery(statement);
//            if (result == null || result.next()==false) {
//                return null;
//            }
//            while (result.next()) {
//                parkId = result.getInt( 1);
//                latitude = result.getDouble( 2);
//                longitude = result.getDouble( 3);
//                park_description = result.getString( 4);
//                parkInputVoltage = result.getFloat( 5);
//                parkInputCorrent = result.getFloat( 6);
//                altitude = getAltitude(latitude, longitude);
//                Set<Capacity> listOfCapacitys = getListOfCapacitys(parkId);
//                parksSameNameOrNot.add(new Park(new Coordinates(latitude, longitude, altitude), listOfCapacitys, parkId, park_description, parkInputVoltage, parkInputCorrent));
//            }
//            result.close();
//            statement.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//        return parksSameNameOrNot;
//    }

    /**
     * Fetch a parkk by id
     *
     * @param id park id
     * @return return a park
     */
    public Park fetchParkById(int id) {
        Coordinates cord;
        double latitude;
        double longitude;
        float parkInputVoltage;
        float parkInputCorrent;
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where park_id=?");
            statement.setInt(1, id);
            ResultSet resultSet = dataHandler.executeQuery(statement);
            if (resultSet == null || resultSet.next()==false) {
                return null;
            }
            latitude=resultSet.getDouble(2);
            longitude=resultSet.getDouble(3);
            cord = new Coordinates(latitude,longitude, getAltitude(latitude, longitude));
            parkInputVoltage = resultSet.getFloat( 4);
            parkInputCorrent = resultSet.getFloat( 5);
            return new Park(cord, getListOfCapacitys(id), id, parkInputVoltage, parkInputCorrent);
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
            PreparedStatement statementForAltitude = dataHandler.prepareStatement("Select altitude_m from points_of_interest where latitude=? and longitude=?");
            statementForAltitude.setDouble(1,latitude);
            statementForAltitude.setDouble(2,longitude);
            ResultSet resultAltitude = dataHandler.executeQuery(statementForAltitude);
            if (resultAltitude == null || resultAltitude.next()==false) {
                return null;
            }
            int altitude=resultAltitude.getInt(1);
            resultAltitude.close();
            statementForAltitude.close();
            return altitude;
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
        int parkCapacity;
        int amountOccupied;
        try {
            PreparedStatement statement = dataHandler.prepareStatement("Select * from park_capacity where park_id=?");
            statement.setDouble( 1, parkId);
            ResultSet resultForCapacity = dataHandler.executeQuery(statement);
            if (resultForCapacity == null) {
                return null;
            }
            while (resultForCapacity.next()) {
                String vehicle_type_name = resultForCapacity.getString( 1);
                parkCapacity = resultForCapacity.getInt( 2);
                amountOccupied = resultForCapacity.getInt( 3);
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
     * @param name of the park
     * @param cord of the park
     * @param vehicleCapacities of the park
     * @param id the park id
     * @param parkInputVoltage the voltage of the park
     * @param parkInputCurrent the current corrent of the park
     * @return return true if successfully removed and false otherwise
     */
    public boolean updatePark(Coordinates cord, Set<Capacity> vehicleCapacities, int id, float parkInputVoltage, float parkInputCurrent) {
        try {
            double latitude = cord.getLatitude();
            double longintude = cord.getLongitude();

            PreparedStatement statement = dataHandler.prepareStatement("Update park SET park_id =? ,latitude=? ,longitude= ?,park_input_voltage= ?,park_input_current =?  FROM park WHERE park_id=?");
            statement.setInt( 1, id);
            statement.setDouble( 2, latitude);
            statement.setDouble( 3, longintude);
            statement.setFloat( 4, parkInputVoltage);
            statement.setFloat( 5, parkInputCurrent);
            statement.setInt( 6, id);
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

    /**
     * Returns the number of bicycles at a certain park
     * @param id id of the park
     * @return number of current bicycles at a park, or null if the park does not exist
     */
    public int getNumberOfBicyclesAtPark(int id) throws IllegalArgumentException{
        for(Capacity cap : getListOfCapacitys(id)){
            if(cap.getVehicleType().equals(VehicleType.BICYCLE)){
                return cap.getAmountOccupied();
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Gets the nearest parks from a certain coordinate and returns their availability
     * @param coords
     * @param radius
     * @return
     */
    public HashMap<Park,Set<Capacity>> getNearestParksAndAvailability(Coordinates coords, Double radius){
        HashMap<Park,Set<Capacity>> nearestParksAvailability = new HashMap<>();


        return nearestParksAvailability;
    }
}
