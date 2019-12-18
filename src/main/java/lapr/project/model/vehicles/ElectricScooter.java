package lapr.project.model.vehicles;

import lapr.project.model.point.of.interest.PointOfInterest;

import java.util.List;

/**
 * Represents an eletric scooter
 */
public class ElectricScooter extends Vehicle {

    private final ElectricScooterType electricScooterType;
    private final int actualBatteryCapacity;
    private final float maxBatteryCapacity;
    private final String description;

    /**
     * Instatiates an eletric scooter object
     *
     * @param id                     the id of the eletric scooter
     * @param aerodynamicCoefficient the aerodynamic coefficient of the scooter
     * @param frontalArea            the frontal area of the scooter
     * @param weight                 the weight in kgs of the eletric scooter
     * @param available              if the scooter is available right now or not
     * @param electricScooterType    the type of the eletric scooter(city or offroad)
     * @param actualBatteryCapacity  the current battery capacity
     * @param maxBatteryCapacity     the maximum battery capacity
     * @param description            the description of the eletric scooter
     */
    public ElectricScooter(int id, float aerodynamicCoefficient,
                           float frontalArea, int weight, boolean available,
                           ElectricScooterType electricScooterType,
                           int actualBatteryCapacity, float maxBatteryCapacity,
                           String description) {
        super(id, aerodynamicCoefficient, frontalArea, weight, available,
                VehicleType.ELECTRIC_SCOOTER);
        this.electricScooterType = electricScooterType;
        this.actualBatteryCapacity = actualBatteryCapacity;
        this.maxBatteryCapacity = maxBatteryCapacity;
        this.description = description;
    }

    public ElectricScooterType getElectricScooterType() {
        return electricScooterType;
    }

    public int getActualBatteryCapacity() {
        return actualBatteryCapacity;
    }

    public float getMaxBatteryCapacity() {
        return maxBatteryCapacity;
    }

    public String getElectricScooterDescription() {
        return description;
    }

    /**
     * Calculates the required battery for a trip with a preset destination
     *
     * @param path the path that the user has planned to take
     * @return the required battery for the total trip
     */
    public static int calculateNecessaryBattery(List<PointOfInterest> path) {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates the battery required between two points
     *
     * @param startPoint the starting point of interest
     * @param endPoint   the ending point of interest
     * @return the required battery for traveling between two points
     */
    private static int calculateNecessaryBattery(PointOfInterest startPoint,
                                                 PointOfInterest endPoint) {
        throw new UnsupportedOperationException();
    }
}
