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
import lapr.project.model.point.of.interest.PointOfInterest;

/**
 *
 * 
 */
public class PoiRegister {
    private final DataHandler dataHandler;

    /**
     * Instantiates a poi register
     * @param dataHandler 
     */
    public PoiRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Fetches a Poi object from the oracle sql table
     *
     * @param latitude - latitude of the poi
     * @param longitude- longitude of the poi
     * @return the point of interest from the oracle sql table
     */
    public PointOfInterest fetchPoi(Double latitude, Double longitude) {
        PreparedStatement stm = null;
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?");
            stm.setDouble(1, latitude);
            stm.setDouble(2, longitude);
            ResultSet resultSet = dataHandler.executeQuery(stm);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            double lat = resultSet.getDouble("latitude");
            double lon = resultSet.getDouble("longitude");
            int alt = resultSet.getInt("altitude_m");
            String desc = resultSet.getString("poi_description");

            Coordinates coordinates = new Coordinates(lat, lon, alt);
            return new PointOfInterest(desc, coordinates);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            dataHandler.closeQueuedAutoCloseables();
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

            dataHandler.commitTransaction();
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
