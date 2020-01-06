/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.model.point.of.interest.PointOfInterest;

import java.util.Objects;

/**
 * Class that represents a path
 */
public class Path {

    private final PointOfInterest startPoint;
    private final PointOfInterest endPoint;
    private final Double kineticCoefficient;
    private final int windDirectionDegrees;
    private final Double windSpeed;

    /**
     * Instantiates a path
     *
     * @param startPoint           - start point of the path
     * @param endPoint             - end point of the path
     * @param windDirectionDegrees - wind direction in degrees
     * @param windSpeed            - wind speed
     */
    public Path(PointOfInterest startPoint, PointOfInterest endPoint, Double kineticCoefficient, int windDirectionDegrees, Double windSpeed) {
        if (startPoint == null || endPoint == null || kineticCoefficient == null || windSpeed == null)
            throw new IllegalArgumentException("Null elements are not allowed");
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.kineticCoefficient = kineticCoefficient;
        this.windDirectionDegrees = windDirectionDegrees;
        this.windSpeed = windSpeed;
    }

    /**
     * Returns the starting point of the path
     *
     * @return the starting point of the path
     */
    public PointOfInterest getStartingPoint() {
        return this.startPoint;
    }

    /**
     * Returns the ending point of the path
     *
     * @return the ending point of the path
     */
    public PointOfInterest getEndingPoint() {
        return this.endPoint;
    }

    /**
     * Returns the kinetic coefficient in that path
     *
     * @return the kinetic coefficient in that path
     */
    public Double getKineticCoefficient() {
        return this.kineticCoefficient;
    }

    /**
     * Returns the wind direction in degrees in the path
     *
     * @return the wind direction in degrees in the path
     */
    public int getWindDirectionDegrees() {
        return this.windDirectionDegrees;
    }

    /**
     * Returns the wind speed in the path
     *
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
        return startPoint.equals(path.getStartingPoint()) && endPoint.equals(path.getEndingPoint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPoint, endPoint, kineticCoefficient, windDirectionDegrees, windSpeed);
    }
}
