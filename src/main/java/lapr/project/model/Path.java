/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Objects;

/**
 * Class that represents a path
 * 
 */
public class Path {
    
    private Double latitudeA;
    private Double longitudeA;
    private Double latitudeB;
    private Double longitudeB;
    private Double kineticCoefficient;
    private int windDirectionDegrees;
    private Double windSpeed;
    
    
   /**
    * Constructor that instantiates a path.
    * @param latitudeA - latitude of start point
    * @param longitudeA - longitude of start point
    * @param latitudeB - latitude of end point
    * @param longitudeB - longitude of end point
    * @param kineticCoefficient - kinetic Coefficient
    * @param windDirectionDegrees - wind direction in degrees
    * @param windSpeed - wind speed
    */
    public Path(Double latitudeA, Double longitudeA, Double latitudeB, Double longitudeB, Double kineticCoefficient, int windDirectionDegrees, Double windSpeed) {
        this.latitudeA = latitudeA;
        this.longitudeA = longitudeA;
        this.latitudeB = latitudeB;
        this.longitudeB = longitudeB;
        this.kineticCoefficient = kineticCoefficient;
        this.windDirectionDegrees = windDirectionDegrees;
        this.windSpeed = windSpeed;
    }
    
    /**
     * Returns the latitude of the starting point of the path
     * @return the latitude of the starting point of the path
     */
    public Double getLatA() {
        return this.latitudeA;
    }
    
    /**
     * Returns the longitude of the starting point of the path
     * @return  the longitude of the starting point of the path
     */
    public Double getLonA() {
        return this.longitudeA;
    }
    
      /**
     * Returns the latitude of the starting point of the path
     * @return the latitude of the starting point of the path
     */
    public Double getLatB() {
        return this.latitudeB;
    }
    
    /**
     * Returns the longitude of the starting point of the path
     * @return  the longitude of the starting point of the path
     */
    public Double getLonB() {
        return this.longitudeB;
    }
    
    /**
     * Returns the kinetic coefficient in that path
     * @return the kinetic coefficient in that path
     */
    public Double getKineticCoefficient(){
        return this.kineticCoefficient;
    }
    
    /**
     * Returns the wind direction in degrees in the path
     * @return the wind direction in degrees in the path
     */
    public int getWindDirectionDegrees() {
        return this.windDirectionDegrees;
    }
    
    /**
     * Returns the wind speed in the path
     * @return the wind speed in the path
     */
    public Double getWindSpeed() {
        return this.windSpeed;
    }
    
     @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Double.compare(path.latitudeA, latitudeA) == 0 && Double.compare(path.longitudeA, longitudeA) == 0 && Double.compare(path.latitudeB, latitudeB) == 0 && Double.compare(path.longitudeB, longitudeB) == 0;
    }
    
        @Override
    public int hashCode() {
        return Objects.hash(latitudeA, latitudeA, latitudeB, longitudeB, kineticCoefficient, windDirectionDegrees, windSpeed);
    }
}
