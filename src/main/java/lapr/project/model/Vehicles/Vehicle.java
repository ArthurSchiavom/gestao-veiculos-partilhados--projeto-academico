package lapr.project.model.Vehicles;

import java.util.Objects;

/**
 * Represents vehicle
 */
public abstract class Vehicle {

    private final int id;
    private final String name;
    private final double latitude, longitude, altitude;
    private final boolean available;
    private final VehicleType type;

    public Vehicle(int id, String name, double latitude, double longitude, double altitude, boolean available, VehicleType type) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.available = available;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public VehicleType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public boolean isAvailable() {
        return available;
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
