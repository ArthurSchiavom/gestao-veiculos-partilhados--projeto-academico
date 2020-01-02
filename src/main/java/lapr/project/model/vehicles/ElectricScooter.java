package lapr.project.model.vehicles;

import lapr.project.model.Path;
import lapr.project.utils.physics.calculations.PhysicsMethods;

import java.util.List;

/**
 * Represents an eletric scooter
 */
public class ElectricScooter extends Vehicle {

    private final ElectricScooterType electricScooterType;
    private final int actualBatteryCapacity;
    private final float maxBatteryCapacity;
    private final int enginePower;

    /**
     * Instatiates an eletric scooter object
     *
     * @param description the description of the eletric scooter
     * @param aerodynamicCoefficient the aerodynamic coefficient of the scooter
     * @param frontalArea the frontal area of the scooter
     * @param weight the weight in kgs of the eletric scooter
     * @param available if the scooter is available right now or not
     * @param electricScooterType the type of the eletric scooter(city or
     * offroad)
     * @param actualBatteryCapacity the current battery capacity
     * @param maxBatteryCapacity the maximum battery capacity
     * @param enginePower the power of the engine
     */
    public ElectricScooter(int uniqueNumber, String description, float aerodynamicCoefficient,
            float frontalArea, int weight, boolean available,
            ElectricScooterType electricScooterType,
            int actualBatteryCapacity, float maxBatteryCapacity, int enginePower) {
        super(uniqueNumber, description, aerodynamicCoefficient, frontalArea, weight, available,
                VehicleType.ELECTRIC_SCOOTER);
        if (electricScooterType == null) {
            throw new IllegalArgumentException("Null elements are not allowed");
        }
        this.electricScooterType = electricScooterType;
        this.actualBatteryCapacity = actualBatteryCapacity;
        this.maxBatteryCapacity = maxBatteryCapacity;
        this.enginePower = enginePower;
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

    public int getEnginePower() {
        return enginePower;
    }

    public Double getScooterAutonomy() {
        return PhysicsMethods.calculateScooterAutonomyKM((double) maxBatteryCapacity, actualBatteryCapacity, enginePower);
    }

    /**
     * Returns true if a scooter has the necessary autonomy to make a trip
     *
     * @param trip trip
     * @return true if a scooter has the necessary autonomy to make a trip
     */
    public boolean hasAutonomy(List<Path> trip) {
        double distance = 0;
        for (Path path : trip) {
            distance += path.getStartingPoint().distance(path.getEndingPoint());
        }
        if (getScooterAutonomy() > (distance + distance * 0.10)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if a scooter has the necessary autonomy to make a trip of X
     * km over a flat road
     *
     * @param trip - the trip
     * @return true if a scooter has the necessary autonomy to make a trim of X
     * km over a flat road
     */
    public boolean hasAutonomyFlat(int km) {
        return Math.round(getScooterAutonomy()) >= km;
    }
}
