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
import lapr.project.model.Path;

 /**
 * Class that regists paths
 *
 */


public class PathRegister {
    private final DataHandler dataHandler;

    /**
     * Instantiates a pathRegister
     * @param dataHandler 
     */
    public PathRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Fetchs a path from the data base
     * @param latitude1 - primary key
     * @param longitude1 - primary key
     * @param latitude2 - primary key
     * @param longitude2 - primary key
     * @return 
     */
    public Path fetchPath(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        PreparedStatement stm = null;
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM paths where latitudeA=? AND longitudeA =? AND latitudeB=? AND longitudeB =?");
            stm.setDouble(1, latitude1);
            stm.setDouble(2, longitude1);
            stm.setDouble(3, latitude2);
            stm.setDouble(4, longitude2);
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
            return new Path(lat1, lon1, lat2, lon2, kinetic, windDirectionDegrees, windSpeed);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            dataHandler.closeQueuedAutoCloseables();
        }
        return null;
    }

    /**
     * Inserts a path on the data base
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


    /**
     * Returns a new path
     * @param latitude1 - latitude of the starting point
     * @param longitude1 - longitude of the starting point
     * @param latitude2 - latitude of the ending point
     * @param longitude2 - longitude of the ending point
     * @param kineticCoefficient - kinetic coefficient of the path
     * @param windDirectionDegrees - wind direction in degrees in the path
     * @param windSpeed - wind speed in the path
     * @return a new path
     */
    public Path newPath(Double latitude1, Double longitude1, Double latitude2, Double longitude2, Double kineticCoefficient, int windDirectionDegrees, Double windSpeed) {
        return new Path(latitude1, longitude1, latitude2, longitude2, kineticCoefficient, windDirectionDegrees, windSpeed);
    }
}
