package lapr.project.model.vehicles;

/**
 * Represents a bicycle which is a vehicle
 */
public class Bicycle extends Vehicle {
    private final int size;

    public Bicycle(String description, float aerodynamicCoefficient, float frontalArea,
                   int weight, boolean available, int size) {
        super(description, aerodynamicCoefficient, frontalArea, weight, available,
                VehicleType.BICYCLE);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
