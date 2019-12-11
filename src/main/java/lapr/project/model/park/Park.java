/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.park;

import java.util.List;
import lapr.project.model.Coordinates;
import java.util.Set;

/**
 *
 * @author kevin //
 */
public class Park {
    private String name;
    private Coordinates cord;
    private Set<Capacity> vehicleCapacities;

    /**
     * Instantiates a park object
     *
     * @param name the name of the park
     * @param cord the coordinates of the park
     */
    public Park(String name, Coordinates cord, Set<Capacity> vehicleCapacities) {
        this.name = name;
        this.cord = cord;
        this.vehicleCapacities = vehicleCapacities;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoords() {
        return cord;
    }
}
