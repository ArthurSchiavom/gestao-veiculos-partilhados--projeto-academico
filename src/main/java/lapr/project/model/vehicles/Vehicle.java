package lapr.project.model.vehicles;

import java.util.Objects;

/**
 * Represents vehicle
 */
public abstract class Vehicle {

    private final int uniqueNumber;
    private final String description;
    private final float aerodynamicCoefficient;
    private final float frontalArea;
    private final int weight;
    private final boolean available;
    private final VehicleType type;

    public Vehicle(int uniqueNumber, String description, float aerodynamicCoefficient, float frontalArea, int weight, boolean available, VehicleType type) {
        if (description == null || type == null)
            throw new IllegalArgumentException("Null elements are not allowed");
        this.uniqueNumber = uniqueNumber;
        this.description = description;
        this.aerodynamicCoefficient = aerodynamicCoefficient;
        this.frontalArea = frontalArea;
        this.weight = weight;
        this.available = available;
        this.type = type;
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    public String getDescription() {
        return description;
    }

    public float getAerodynamicCoefficient() {
        return aerodynamicCoefficient;
    }

    public float getFrontalArea() {
        return frontalArea;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isAvailable() {
        return available;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return description.equals(vehicle.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description);
    }
}
