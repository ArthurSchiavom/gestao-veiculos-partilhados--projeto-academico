/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.park;

import lapr.project.model.Vehicles.VehicleType;


public class Capacity {
    private int maxCapacity;
    private int amountOccupied = 0;
    private VehicleType vehicleType;
    
    /**
     * 
     * @param capacity
     * @param amountOccupied
     * @param vehicleType 
     */
    public Capacity(int capacity,int amountOccupied,VehicleType vehicleType){
        this.maxCapacity = capacity;
        this.amountOccupied = amountOccupied;
        this.vehicleType = vehicleType;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getAmountOccupied() {
        return amountOccupied;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
}
