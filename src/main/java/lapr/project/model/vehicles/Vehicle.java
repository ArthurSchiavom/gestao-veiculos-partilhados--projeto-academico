package lapr.project.model.vehicles;

import java.util.Objects;

/**
 * Represents vehicle
 */
public abstract class Vehicle {

    private final int id;
    private final double latitude, longitude;
    private final float aerodynamic_coefficient, frontal_area;
    private final int altitude, weight;
    private final boolean available;
    private final VehicleType type;

    public Vehicle(int id, double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, VehicleType type) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.aerodynamic_coefficient = aerodynamic_coefficient;
        this.frontal_area = frontal_area;
        this.altitude = altitude;
        this.weight = weight;
        this.available = available;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAerodynamic_coefficient() {
        return aerodynamic_coefficient;
    }

    public float getFrontal_area() {
        return frontal_area;
    }

    public int getAltitude() {
        return altitude;
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
        return id == vehicle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
