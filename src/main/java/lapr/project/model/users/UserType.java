package lapr.project.model.users;

/**
 * Represents the type of client types
 */
public enum UserType {
    ADMIN("administrator"),
    CLIENT("client");

    private final String SQLName;
    UserType(String SQLName) {
        this.SQLName = SQLName;
    }

    public String getSQLName() {
        return SQLName;
    }

    public static UserType parseUserType(String str) {
        for (UserType value : UserType.values()) {
            if (value.getSQLName().equalsIgnoreCase(str))
                return value;
        }
        return null;
    }
}
