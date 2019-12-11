package lapr.project.model.Vehicles;

import java.util.Objects;

import static lapr.project.model.Vehicles.VehicleType.ELECTRIC_SCOOTER;

/**
 * Represents an eletric scooter
 */
public class ElectricScooter extends Vehicle{

    private final float batteryLevel;
    private final ScooterType scooterType;

    public ElectricScooter(int id, String name, double latitude, double longitude, double altitude, char available, VehicleType type, float batteryLevel, ScooterType scooterType) {
        super(id, name, latitude, longitude, altitude, available, type);
        this.batteryLevel = batteryLevel;
        this.scooterType = scooterType;
    }

    public float getBatteryLevel() {
        return batteryLevel;
    }

    public ScooterType getScooterType() {
        return scooterType;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElectricScooter)) return false;
        if (!super.equals(o)) return false;
        ElectricScooter that = (ElectricScooter) o;
        return Float.compare(that.getBatteryLevel(), getBatteryLevel()) == 0 &&
                getScooterType() == that.getScooterType() && getId() == that.getId() &&
                Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Double.compare(that.getAltitude(), getAltitude()) == 0 &&
                getAvailable() == that.getAvailable() &&
                Objects.equals(getName(), that.getName()) &&
                getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBatteryLevel(), getScooterType());
    }
}
