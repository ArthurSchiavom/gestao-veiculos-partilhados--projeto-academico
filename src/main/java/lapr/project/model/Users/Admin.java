package lapr.project.model.Users;

/**
 * Represents an admin
 */
public class Admin extends User{
    public Admin(String email, String password) {
        super(email, password, ClientType.ADMIN);
    }
}