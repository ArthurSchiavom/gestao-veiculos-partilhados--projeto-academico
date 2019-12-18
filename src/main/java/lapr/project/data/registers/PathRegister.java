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
import lapr.project.model.Path;

/**
 * Class that regists paths
 *
 */
public class PathRegister {

    private final DataHandler dataHandler;

    /**
     * Instantiates a pathRegister
     *
     * @param dataHandler
     */
    public PathRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Fetches a path from the data base
     *
     * point (primary keys)
     * @return a path from the data base
     */
    public Path fetchPath(Double startLatitude, Double startLongitude, Double endLatitude, Double endLongitude) {
        PreparedStatement stm = null;
        PreparedStatement stmStartAltitude = null;
        PreparedStatement stmEndAltitude = null;
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
            stmStartAltitude.setDouble(1, startLatitude);
            stmStartAltitude.setDouble(2, startLongitude);
            ResultSet resultSetAltitudeSP = dataHandler.executeQuery(stmStartAltitude);
            if (resultSetAltitudeSP == null || !resultSetAltitudeSP.next()) {
                return null;
            }
            int startAltitude = resultSetAltitudeSP.getInt("altitude_m");

            stmEndAltitude = dataHandler.prepareStatement("SELECT altitude_m FROM points_of_interest WHERE latitude =? and longitude =?");

            stmEndAltitude = dataHandler.prepareStatement("SELECT altitude_m FROM points_of_interest WHERE latitude =? and longitude =?");
            stmEndAltitude.setDouble(1, endLatitude);
            stmEndAltitude.setDouble(2, endLongitude);
            ResultSet resultSetAltitudeEP = dataHandler.executeQuery(stmEndAltitude);
            if (resultSetAltitudeEP == null || !resultSetAltitudeEP.next()) {
                return null;
            }
            int endAltitude = resultSetAltitudeEP.getInt("altitude_m");

            Coordinates startingPoint = new Coordinates(lat1, lon1, startAltitude);
            Coordinates endingPoint = new Coordinates(lat2, lon2, endAltitude);
            return new Path(startingPoint,endingPoint,kinetic,windDirectionDegrees,windSpeed);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            dataHandler.closeQueuedAutoCloseables();
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
     * @return
     */
    public boolean insertPath(Double latitude1, Double longitude1, Double latitude2, Double longitude2, Double kineticCoefficient, Integer windDirectionDegree, Double windSpeed) {
        PreparedStatement stm = null;

        try {
            if (kineticCoefficient == null) {
                kineticCoefficient = 0.0;
            }
            if (windDirectionDegree == null) {
                windDirectionDegree = 0;
            }
            if (windSpeed == null) {
                windSpeed = 0.0;
            }
            stm = dataHandler.prepareStatement("INSERT INTO paths(latitudeA, longitudeA, latitudeB, longitudeB, kinetic_coefficient, wind_direction_degrees, wind_speed) " + "VALUES(?,?,?,?,?,?,?)");
            stm.setDouble(1, latitude1);
            stm.setDouble(2, longitude1);
            stm.setDouble(3, latitude2);
            stm.setDouble(4, longitude2);
            stm.setDouble(5, kineticCoefficient);
            stm.setInt(6, windDirectionDegree);
            stm.setDouble(7, windSpeed);
            int nrLines = dataHandler.executeUpdate(stm);
            if (nrLines == 0) {
                return false;
            }

            dataHandler.commitTransaction();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            dataHandler.closeQueuedAutoCloseables();
        }
        return true;
    }
}
