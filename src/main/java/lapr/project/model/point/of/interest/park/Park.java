/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.point.of.interest.park;

import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.vehicles.VehicleType;

import java.util.List;


public class Park extends PointOfInterest {
    private final String id;
    private final float parkInputVoltage;
    private final float parkInputCurrent;
    private final List<Capacity> vehicleCapacities;

    public Park(String id, float parkInputVoltage, float parkInputCurrent, List<Capacity> vehicleCapacities, String description, Coordinates coordinates) {
        super(description, coordinates);
        this.id = id;
        this.parkInputVoltage = parkInputVoltage;
        this.parkInputCurrent = parkInputCurrent;
        this.vehicleCapacities = vehicleCapacities;
    }

    public String getId() {
        return id;
    }

    public List<Capacity> getVehicleCapacities() {
        return vehicleCapacities;
    }

    public float getParkInputVoltage() {
        return parkInputVoltage;
    }

    public float getParkInputCurrent() {
        return parkInputCurrent;
    }

    public int getAmountOccupiedByType(VehicleType type){
        for (Capacity parkCapacity : vehicleCapacities){
            if (parkCapacity.getVehicleType().equals(type)){
                return parkCapacity.getAmountOccupied();
            }
        }
        throw new EnumConstantNotPresentException(type.getClass(), "Enum type not registered on the database");
    }
    
    public int getMaxAmountByType(VehicleType type){
        for (Capacity parkCapacity : vehicleCapacities){
            if (parkCapacity.getVehicleType().equals(type)){
               return parkCapacity.getMaxCapacity();
            }
        }
        throw new EnumConstantNotPresentException(type.getClass(), "Enum type not registered on the database");
    }

    public int getAmountSlotsFreeByType(VehicleType type){
        return getMaxAmountByType(type) - getAmountOccupiedByType(type);
    }
}
