package lapr.project.model.vehicles;

/**
 * Represents an eletric scooter
 */
public class ElectricScooter extends Vehicle {

    private final ElectricScooterType electricScooterType;
    private final int actualBatteryCapacity;
    private final float maxBatteryCapacity;
    private final String description;

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
}
