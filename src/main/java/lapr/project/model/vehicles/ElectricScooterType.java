package lapr.project.model.vehicles;

/**
 * Represents the types of existent scooters
 */
public enum ElectricScooterType {
    URBAN("urban", "city"),
    OFFROAD("offroad", "off-road");

    public final String SQLName;
    public final String fileName;
    ElectricScooterType(String SQLName, String fileName) {
        this.SQLName = SQLName;
        this.fileName = fileName;
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

    public String getFileName() {
        return fileName;
    }
}
