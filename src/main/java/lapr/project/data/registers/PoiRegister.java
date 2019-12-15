/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.PointOfInterest;

/**
 *
 * 
 */
public class PoiRegister {

    DataHandler dataHandler;

    /**
     * Instantiates a poi register
     * @param dataHandler 
     */
    public PoiRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Fetches a Poi object from the oraqle sql table
     *
     * @param latitude - latitude of the poi
     * @param longitude- longitude of the poi
     * @return the point of interest from the oraqle sql table
     */
    public PointOfInterest fetchPoi(Double latitude, Double longitude) {
        PreparedStatement stm = null;
        PointOfInterest poi;
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?");
            stm.setDouble(1, latitude);
            stm.setDouble(2, longitude);
            ResultSet resultSet = dataHandler.executeQuery(stm);
            if (resultSet == null || resultSet.next() == false) { //resultSet is empty
                resultSet.close();
                stm.close();
                return null;
            }
            double lat = resultSet.getDouble(1);
            double lon = resultSet.getDouble(2);
            int alt = resultSet.getInt(3);
            String desc = resultSet.getString(4);

            Coordinates coordinates = new Coordinates(lat, lon, alt);
            return new PointOfInterest(desc, coordinates);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a point of interest on the data base
     *
     * @param description - the description of the point of interest
     * @param latitude - the latitude of the point of interest
     * @param longitude - the longitude of the point of interest
     * @param altitude - the altitude of the point of interest
     * @return
     */
    public boolean insertPoi(String description, Double latitude, Double longitude, Integer altitude) {
        //create statement to be executed later
        PreparedStatement stm = null;
        try {
            if (altitude == null) {
                altitude = 0;
            }
            stm = dataHandler.prepareStatement("Insert into points_of_interest(latitude, longitude, altitude_m, poi_description) values (?,?,?,?)");

            stm.setDouble(1, latitude);
            stm.setDouble(2, longitude);
            stm.setInt(3, altitude);
            stm.setString(4, description);

            dataHandler.executeUpdate(stm);

            stm.close(); // closes statement

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns a new point of interest
     *
     * @param latitude - the latitude of the poi
     * @param longitude - the longitude of the poi
     * @param altitude - the altitude of the poi
     * @param description - the description of the poi
     * @return
     */
    public PointOfInterest newPointOfInterest(Double latitude, Double longitude, int altitude, String description) {
        return new PointOfInterest(description, new Coordinates(latitude, longitude, altitude));
    }
}
