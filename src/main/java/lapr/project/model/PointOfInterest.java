/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Objects;

/**
 * Class that represents a point of interest
 *
 */
public class PointOfInterest {
    
    private String description;
    private Coordinates coordinates;
    
    /**
     * Constructor that instantiates a trip.
     * @param description - the description (can be the name) of the point of interest
     * @param coordinates - the coordinates of the point of interest
     */
    public PointOfInterest(String description, Coordinates coordinates) {
        if (description == null || coordinates == null) {
            throw new IllegalArgumentException("Invalid Parameters");
        }
        this.description = description;
        this.coordinates = coordinates;
    }
    
    /**
     * Returns the description of the point of interest.
     * @return 
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointOfInterest poi = (PointOfInterest) o;
        return coordinates.equals(poi.coordinates);
    }
    
        @Override
    public int hashCode() {
        return Objects.hash(description, coordinates);
    }
}
