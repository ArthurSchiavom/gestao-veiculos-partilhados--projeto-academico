/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Objects;

/**
 *
 * @author kevin //
 */
public class Park {

    private String name;
    private Coordinates cord;
    private ParkCapacity bikeCapacity;
    private ParkCapacity scooterOffroadCapacity;
    private ParkCapacity scooterUrbanCapacity;

    /**
     * Instantiates a park object
     *
     * @param name the name of the park
     * @param cord the coordinates of the park
     */
    public Park(String name, Coordinates cord) {
        this.name = name;
        this.cord = cord;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCord() {
        return cord;
    }

    public ParkCapacity getBikeCapacity() {
        return bikeCapacity;
    }

    public ParkCapacity getScooterOffroadCapacity() {
        return scooterOffroadCapacity;
    }

    public ParkCapacity getScooterUrbanCapacity() {
        return scooterUrbanCapacity;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Park other = (Park) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.cord, other.cord)) {
            return false;
        }
        if (!Objects.equals(this.bikeCapacity, other.bikeCapacity)) {
            return false;
        }
        if (!Objects.equals(this.scooterOffroadCapacity, other.scooterOffroadCapacity)) {
            return false;
        }
        if (!Objects.equals(this.scooterUrbanCapacity, other.scooterUrbanCapacity)) {
            return false;
        }
        return true;
    }
}
