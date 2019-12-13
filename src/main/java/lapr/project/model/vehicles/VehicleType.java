package lapr.project.model.vehicles;

/**
 * Represents the types of existent vehicles
 */
public enum VehicleType {
    BICYCLE,
    ELECTRIC_SCOOTER;

    public static VehicleType parseVehicleType(String str) {
        for (VehicleType value : VehicleType.values()) {
            if (value.toString().equalsIgnoreCase(str))
                return value;
        }
        return null;
    }
}