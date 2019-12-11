package lapr.project.model.Users;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getEmail().equals(user.getEmail()) &&
                getPassword().equals(user.getPassword()) &&
                getType() == user.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPassword(), getType());
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                '}';
    }
}
