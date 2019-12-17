package lapr.project.model.vehicles;

/**
 * Represents a bicycle which is a vehicle
 */
public class Bicycle extends Vehicle {
    private final int size;
    private final String description;

    public Bicycle(int id, float aerodynamicCoefficient, float frontalArea,
                   int weight, boolean available, int size,
                   String description) {
        super(id, aerodynamicCoefficient, frontalArea, weight, available,
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
