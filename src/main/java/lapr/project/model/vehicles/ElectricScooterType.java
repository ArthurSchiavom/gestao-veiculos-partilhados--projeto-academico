package lapr.project.model.vehicles;

/**
 * Represents the types of existent scooters
 */
public enum ElectricScooterType {
    URBAN("urban"),
    OFFROAD("offroad");

    public final String SQLName;
    ElectricScooterType(String SQLName) {
        this.SQLName = SQLName;
    }

    public String getSQLName() {
        return SQLName;
    }

    public static ElectricScooterType parseScooterType(String str) {
        for (ElectricScooterType value : ElectricScooterType.values()) {
            if (value.getSQLName().equalsIgnoreCase(str))
                return value;
        }
        return null;
    }
}
