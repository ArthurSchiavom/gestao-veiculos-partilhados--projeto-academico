/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.PointOfInterest;

/**
 *
 * 
 */
public class PoiAPI {
    private final DataHandler dataHandler;
    private static final Logger LOGGER = Logger.getLogger("poiRegisterLogger");

    /**
     * Instantiates a poi register
     */
    public PoiAPI(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Inserts a list of points of interest
     */
    public int insertPOIs(List<Double> lat, List<Double> lon, List<Integer> elev, List<String> desc) throws SQLException {
        if(!(lat.size()==lon.size() && lon.size() == elev.size() && elev.size() == desc.size())){
            throw new IllegalArgumentException("Lists have different sizes.");
        }
        int i;
        for( i = 0 ; i < lat.size(); i++){
            try {
                insertPoi(desc.get(i),lat.get(i),lon.get(i),elev.get(i));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
                dataHandler.rollbackTransaction();
                return 0;
            }
        }
        dataHandler.commitTransaction(); // commits all the clients at once contained in the current transaction
        return i;
    }

    /**
     * Fetches all Poi objects from the oracle sql table
     * @return list containing all poi's
     */
    public List<PointOfInterest> fetchAllPois(){
        PreparedStatement stm = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        List<PointOfInterest> pois = new ArrayList<>();
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM points_of_interest");
            autoCloseableManager.addAutoCloseable(stm);
            ResultSet resultSet = dataHandler.executeQuery(stm);
            autoCloseableManager.addAutoCloseable(resultSet);
            int alt;
            double lon;
            String desc;
            double lat;
            Coordinates coordinates;
            while(resultSet.next()){
                alt = resultSet.getInt("altitude_m");
                lon = resultSet.getDouble("longitude");
                desc = resultSet.getString("poi_description");
                lat = resultSet.getDouble("latitude");
                coordinates = new Coordinates(lat, lon, alt);
                pois.add(new PointOfInterest(desc, coordinates));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return pois;
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
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?");
            autoCloseableManager.addAutoCloseable(stm);
            stm.setDouble(1, latitude);
            stm.setDouble(2, longitude);
            ResultSet resultSet = dataHandler.executeQuery(stm);
            autoCloseableManager.addAutoCloseable(resultSet);
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
            LOGGER.log(Level.SEVERE, e.getMessage());
        } finally {
            autoCloseableManager.closeAutoCloseables();
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
    private void insertPoi(String description, Double latitude, Double longitude, Integer altitude) throws SQLException {
        //create statement to be executed later
        PreparedStatement stm = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            if (altitude == null) {
                altitude = 0;
            }
            stm = dataHandler.prepareStatement("Insert into points_of_interest(latitude, longitude, altitude_m, poi_description) values (?,?,?,?)");
            autoCloseableManager.addAutoCloseable(stm);

            stm.setDouble(1, latitude);
            stm.setDouble(2, longitude);
            stm.setInt(3, altitude);
            stm.setString(4, description);

            dataHandler.executeUpdate(stm);

        } catch (SQLException e) {
            throw e;
        }finally {
            autoCloseableManager.closeAutoCloseables();
        }
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
