/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.point.of.interest;

import lapr.project.model.Coordinates;

import java.util.Objects;

/**
 * Class that represents a point of interest
 *
 */
public class PointOfInterest {
    
    private final String description;
    private final Coordinates coordinates;

    /**
     * Constructor that instantiates a trip.
     * @param description - the description (can be the name) of the point of interest
     * @param coordinates - the coordinates of the point of interest
     */
    public PointOfInterest(String description, Coordinates coordinates) {
        if (description == null || coordinates == null) {
            throw new IllegalArgumentException("Null elements are not allowed");
        }
        this.description = description;
        this.coordinates = coordinates;
    }
    
    /**
     * Returns the description of the point of interest.
     * @return description
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Returns the coordinates of the point of interest.
     * @return the coordinates of the point of interest
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointOfInterest)) {
            return false;
        }
        PointOfInterest that = (PointOfInterest) o;
        return getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description);
    }
}
