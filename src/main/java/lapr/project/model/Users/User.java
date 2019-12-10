package lapr.project.model.Users;

/**
 * Class that represents an user
 */
public abstract class User {
    private final String email, password;
    private final ClientType type;

    public User(String email, String password, ClientType type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public ClientType getType() {
        return type;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
}
