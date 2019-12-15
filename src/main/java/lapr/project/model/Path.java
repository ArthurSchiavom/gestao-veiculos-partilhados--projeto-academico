/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Objects;
import lapr.project.model.PointOfInterest;

/**
 * Class that represents a path
 * 
 */
public class Path {
    
    private PointOfInterest pointA;
    private PointOfInterest pointB;
    private Double kineticCoefficient;
    private int windDirectionDegrees;
    private Double windSpeed;
    
    
    /**
     * Constructor that instantiates a path.
     * @param pointA - start point
     * @param pointB - end point
     * @param kineticCoefficient - kinetic coefficient
     * @param windDirectionDegrees - wind direction in degrees
     * @param windSpeed - wind speed
     */
    public Path(PointOfInterest pointA, PointOfInterest pointB, Double kineticCoefficient, int windDirectionDegrees, Double windSpeed) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.kineticCoefficient = kineticCoefficient;
        this.windDirectionDegrees = windDirectionDegrees;
        this.windSpeed = windSpeed;
    }
    
    /**
     * Returns the starting point of the trip
     * @return the starting point of the trip
     */
    public PointOfInterest getPointA() {
        return this.pointA;
    }
    
    /**
     * Returns the ending point of the path
     * @return the ending point of the path
     */
    public PointOfInterest getPointB() {
        return this.pointB;
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
        return (pointA.equals(path.pointA) && pointB.equals(path.pointB));
    }
    
        @Override
    public int hashCode() {
        return Objects.hash(pointA, pointB, kineticCoefficient, windDirectionDegrees, windSpeed);
    }
}
