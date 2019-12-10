package lapr.project.model.Vehicles;

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
}
