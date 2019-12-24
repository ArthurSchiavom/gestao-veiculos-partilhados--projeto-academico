/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.Path;

/**
 * Class that registers paths
 *
 */
public class PathRegister {

    private final DataHandler dataHandler;

    /**
     * Instantiates a pathRegister
     */
    public PathRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    private static final Logger LOGGER = Logger.getLogger("pathRegisterLogger");

    /**
     * Inserts a list of clients
     */
    public int insertPaths(List<Double> latA, List<Double> lonA, List<Double> latB, List<Double> lonB, List<Double> kineticCoef, List<Integer> windDirection, List<Double> windSpeed) throws SQLException {
        if(!(latA.size()==lonA.size() && lonA.size() == latB.size() && latB.size() == lonB.size() && lonB.size()== kineticCoef.size() && kineticCoef.size() == windDirection.size() && windDirection.size() == windSpeed.size())){
            throw new IllegalArgumentException("Lists have different sizes.");
        }
        int i;
        for( i = 0 ; i < latA.size(); i++){
            try {
                insertPath(latA.get(i), lonA.get(i), latB.get(i), lonB.get(i), kineticCoef.get(i),  windDirection.get(i), windSpeed.get(i));
            } catch (Exception e) {
                try {dataHandler.rollbackTransaction();} catch (Exception e2) {}
                throw e;
            }
        }
        dataHandler.commitTransaction(); // commits all the clients at once contained in the current transaction
        return i;
    }

    /**
     * Fetches a path from the data base
     *
     * point (primary keys)
     * @return a path from the data base
     */
    public Path fetchPath(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        PreparedStatement stm = null;
        PreparedStatement stmStartAltitude = null;
        PreparedStatement stmEndAltitude = null;
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM paths where latitudeA=? AND longitudeA =? AND latitudeB=? AND longitudeB =?");
            stm.setDouble(1, startLatitude);
            stm.setDouble(2, startLongitude);
            stm.setDouble(3, endLatitude);
            stm.setDouble(4, endLongitude);
            ResultSet resultSet = dataHandler.executeQuery(stm);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            double lon1 = resultSet.getDouble("longitudeA");
            double lat1 = resultSet.getDouble("latitudeA");
            double lat2 = resultSet.getDouble("latitudeB");
            double lon2 = resultSet.getDouble("longitudeB");
            double kinetic = resultSet.getDouble("kinetic_coefficient");
            int windDirectionDegrees = resultSet.getInt("wind_direction_degrees");
            double windSpeed = resultSet.getDouble("wind_speed");

            stmStartAltitude = dataHandler.prepareStatement("SELECT altitude_m FROM points_of_interest WHERE latitude =? and longitude =?");
            closeableManager.addAutoCloseable(stmStartAltitude);
            stmStartAltitude.setDouble(1, startLatitude);
            stmStartAltitude.setDouble(2, startLongitude);
            ResultSet resultSetAltitudeSP = dataHandler.executeQuery(stmStartAltitude);
            closeableManager.addAutoCloseable(resultSetAltitudeSP);
            if (resultSetAltitudeSP == null || !resultSetAltitudeSP.next()) {
                return null;
            }
            int startAltitude = resultSetAltitudeSP.getInt("altitude_m");

            stmEndAltitude = dataHandler.prepareStatement("SELECT altitude_m FROM points_of_interest WHERE latitude =? and longitude =?");
            closeableManager.addAutoCloseable(stmEndAltitude);
            stmEndAltitude.setDouble(1, endLatitude);
            stmEndAltitude.setDouble(2, endLongitude);
            ResultSet resultSetAltitudeEP = dataHandler.executeQuery(stmEndAltitude);
            closeableManager.addAutoCloseable(resultSetAltitudeEP);
            if (resultSetAltitudeEP == null || !resultSetAltitudeEP.next()) {
                return null;
            }
            int endAltitude = resultSetAltitudeEP.getInt("altitude_m");

            Coordinates startingPoint = new Coordinates(lat1, lon1, startAltitude);
            Coordinates endingPoint = new Coordinates(lat2, lon2, endAltitude);
            return new Path(startingPoint,endingPoint,kinetic,windDirectionDegrees,windSpeed);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } finally {
            closeableManager.closeAutoCloseables();
        }
        return null;
    }

    /**
     * Inserts a path on the data base
     *
     * @param latitude1 - latitude of the starting point
     * @param longitude1 - longitude of the starting point
     * @param latitude2 - latitude of the ending point
     * @param longitude2 - longitude of the ending point
     * @param kineticCoefficient - kinetic coefficient of the path
     * @param windSpeed - wind speed in the path
     */
    private void insertPath(double latitude1, double longitude1, double latitude2, double longitude2, double kineticCoefficient, int windDirectionDegree, double windSpeed) throws SQLException {
        PreparedStatement stm = null;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();

        try {
            stm = dataHandler.prepareStatement("INSERT INTO paths(latitudeA, longitudeA, latitudeB, longitudeB, kinetic_coefficient, wind_direction_degrees, wind_speed) VALUES(?,?,?,?,?,?,?)");
            autoCloseableManager.addAutoCloseable(stm);
            stm.setDouble(1, latitude1);
            stm.setDouble(2, longitude1);
            stm.setDouble(3, latitude2);
            stm.setDouble(4, longitude2);
            stm.setDouble(5, kineticCoefficient);
            stm.setInt(6, windDirectionDegree);
            stm.setDouble(7, windSpeed);
            dataHandler.executeUpdate(stm);
        } catch (SQLException e) {
            throw e;
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }
}
