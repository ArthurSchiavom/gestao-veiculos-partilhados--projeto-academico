package lapr.project.model.Vehicles;

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
}
