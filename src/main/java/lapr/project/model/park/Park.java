/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.park;

import lapr.project.model.Coordinates;
import java.util.Set;
import lapr.project.model.Vehicles.VehicleType;


public class Park {
    private Coordinates cord;
    private int id;
    private String description;
    private float parkInputVoltage;
    private float parkInputCurrent;
    private Set<Capacity> vehicleCapacities;

    /**
     * Instantiates a park object
     *
     * @param cord the coordinates of the park
     * @param vehicleCapacities capacity of the park
     * @param parkInputVoltage  the corrent of the park
     * @param parkInputCurrent  the atual corrent of the park
     * @param parkId id of the park
     */
    public Park(Coordinates cord, Set<Capacity> vehicleCapacities,int id,String description,float parkInputVoltage,float parkInputCurrent) {
        this.cord = cord;
        this.vehicleCapacities = vehicleCapacities;
        this.id = id;
        this.description=description;
        this.parkInputVoltage = parkInputVoltage;
        this.parkInputCurrent = parkInputCurrent;
    }

    public String getDescription() {
        return description;
    }
    
    public float getParkInputVoltage() {
        return parkInputVoltage;
    }

    public float getParkInputCurrent() {
        return parkInputCurrent;
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
    
    public int getParkId() {
        return id;
    }
}
