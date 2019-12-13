package lapr.project.model.users;

/**
 * Represents an admin
 */
public class Admin extends User{
    public Admin(String email, String password) {
        super(email, password, UserType.ADMIN);
    }
}
