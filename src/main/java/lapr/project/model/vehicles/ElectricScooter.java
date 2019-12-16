package lapr.project.model.vehicles;

/**
 * Represents an eletric scooter
 */
public class ElectricScooter extends Vehicle {

    private final ElectricScooterType electricScooterType;
    private final int actualBatteryCapacity;
    private final float maxBatteryCapacity;
    private final String description;

    public ElectricScooter(int id, double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, VehicleType type, ElectricScooterType electricScooterType, int actual_battery_capacity, float max_battery_capacity, String description) {
        super(id, latitude, longitude, altitude, aerodynamic_coefficient, frontal_area, weight, available, type);
        this.electricScooterType = electricScooterType;
        this.actualBatteryCapacity = actual_battery_capacity;
        this.maxBatteryCapacity = max_battery_capacity;
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

    public String getEletricScooterDescription() {
        return description;
    }
}
