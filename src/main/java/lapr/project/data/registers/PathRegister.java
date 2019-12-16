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

    DataHandler dataHandler;

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
        Path path;
        try {
            stm = dataHandler.prepareStatement("SELECT * FROM paths where latitudeA=? AND longitudeA =? AND latitudeB=? AND longitudeB =?");
            stm.setDouble(1, latitude1);
            stm.setDouble(2, longitude1);
            stm.setDouble(3, latitude2);
            stm.setDouble(4, longitude2);
            ResultSet resultSet = dataHandler.executeQuery(stm);
            if (resultSet == null) {
                stm.close();
                return null;
            }
            else if (!resultSet.next()) {
                resultSet.close();
                stm.close();
                return null;
            }
            double lon1 = resultSet.getDouble(2);
            double lat1 = resultSet.getDouble(1);
            double lat2 = resultSet.getDouble(3);
            double lon2 = resultSet.getDouble(4);
            double kinetic = resultSet.getDouble(5);
            int windDirectionDegrees = resultSet.getInt(6);
            double windSpeed = resultSet.getDouble(7);
            return new Path(lat1, lon1, lat2, lon2, kinetic, windDirectionDegrees, windSpeed);
        } catch (SQLException e) {
            e.printStackTrace();
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
     * @param windDirectionDegrees - wind direction in degrees in the path
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
                stm.close();
         
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
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
