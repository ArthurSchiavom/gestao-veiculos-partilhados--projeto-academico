package lapr.project.model.vehicles;

/**
 * Represents an eletric scooter
 */
public class ElectricScooter extends Vehicle {

    private final ScooterType scooterType;
    private final int actual_battery_capacity;
    private final float max_battery_capacity;
    private final String description;

    public ElectricScooter(int id, double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, VehicleType type, ScooterType scooterType, int actual_battery_capacity, float max_battery_capacity, String description) {
        super(id, latitude, longitude, altitude, aerodynamic_coefficient, frontal_area, weight, available, type);
        this.scooterType = scooterType;
        this.actual_battery_capacity = actual_battery_capacity;
        this.max_battery_capacity = max_battery_capacity;
        this.description = description;
    }

    public ScooterType getScooterType() {
        return scooterType;
    }

    public int getActual_battery_capacity() {
        return actual_battery_capacity;
    }

    public float getMax_battery_capacity() {
        return max_battery_capacity;
    }

    public String getEletric_scooter_description() {
        return description;
    }
}
