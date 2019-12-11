package lapr.project.model.Vehicles;

import java.util.Objects;

import static lapr.project.model.Vehicles.VehicleType.BICYCLE;

/**
 * Represents a bicycle which is a vehicle
 */
public class Bicycle extends Vehicle{
    private final int size;

    public Bicycle(int id, String name, double latitude, double longitude, double altitude, char available, VehicleType type, int size) {
        super(id, name, latitude, longitude, altitude, available, type);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bicycle)) return false;
        Bicycle bicycle = (Bicycle) o;
        return getSize() == bicycle.getSize() && getId() == bicycle.getId() &&
                Double.compare(bicycle.getLatitude(), getLatitude()) == 0 &&
                Double.compare(bicycle.getLongitude(), getLongitude()) == 0 &&
                Double.compare(bicycle.getAltitude(), getAltitude()) == 0 &&
                getAvailable() == bicycle.getAvailable() &&
                Objects.equals(getName(), bicycle.getName()) &&
                getType() == bicycle.getType();
    }
}
