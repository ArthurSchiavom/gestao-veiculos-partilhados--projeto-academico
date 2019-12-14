package lapr.project.model.vehicles;

/**
 * Represents the types of existent vehicles
 */
public enum VehicleType {
    BICYCLE("bicycle"),
    ELECTRIC_SCOOTER("electric_scooter");

    private final String SQLName;
    VehicleType(String SQLName) {
        this.SQLName = SQLName;
    }

    public String getSQLName() {
        return SQLName;
    }

    public static VehicleType parseVehicleType(String str) {
        for (VehicleType value : VehicleType.values()) {
            if (value.getSQLName().equalsIgnoreCase(str))
                return value;
        }
        return null;
    }
}