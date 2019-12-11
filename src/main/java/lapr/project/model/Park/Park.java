/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.Park;

import java.util.List;
import lapr.project.model.Coordinates;
import java.util.Set;
import lapr.project.model.Vehicles.VehicleType;

/**
 *
 * @author kevin //
 */
public class Park {
    private String name;
    private Coordinates cord;
    private int id;
    private Set<Capacity> vehicleCapacities;

    /**
     * Instantiates a park object
     *
     * @param name the name of the park
     * @param cord the coordinates of the park
     * @param vehicleCapacities capacity of the park
     * @param parkId id of the park
     */
    public Park(String name, Coordinates cord, Set<Capacity> vehicleCapacities,int id) {
        this.name = name;
        this.cord = cord;
        this.vehicleCapacities = vehicleCapacities;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoords() {
        return cord;
    }
    public int getAmountOccupiedByType(VehicleType tipo){
        for (Capacity capacity : vehicleCapacities){
            if (capacity.getVehicleType().compareTo(tipo) == 0){
               return  capacity.getAmountOccupied();
            }
        }
        return -1;
    }
    
    public int getMaxAmountByType(VehicleType tipo){
        for (Capacity capacity : vehicleCapacities){
            if (capacity.getVehicleType().compareTo(tipo) == 0){
               return  capacity.getMaxCapacity();
            }
        }
        return -2;
    }
}
