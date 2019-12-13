package lapr.project.model.users;

/**
 * Represents an admin
 */
public class Admin extends User{
    public Admin(String email,String username, String password) {
        super(email,username, password, UserType.ADMIN);
    }
}
