package lapr.project.model.vehicles;

/**
 * Represents a bicycle which is a vehicle
 */
public class Bicycle extends Vehicle {
    private final int size;
    private final String description;

    public Bicycle(String description, float aerodynamicCoefficient, float frontalArea,
                   int weight, boolean available, int size) {
        super(description, aerodynamicCoefficient, frontalArea, weight, available,
                VehicleType.BICYCLE);
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
