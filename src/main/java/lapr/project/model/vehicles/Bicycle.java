package lapr.project.model.vehicles;

/**
 * Represents a bicycle which is a vehicle
 */
public class Bicycle extends Vehicle {
    private final int size;
    private final String description;

    public Bicycle(int id, double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, VehicleType type, int size, String description) {
        super(id, latitude, longitude, altitude, aerodynamic_coefficient, frontal_area, weight, available, type);
        this.size = size;
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }
}
