package lapr.project.model.users;

import java.util.Objects;

/**
 * Class that represents an user
 */
public abstract class User {
    private final String email, password, username;
    private final UserType type;

    public User(String email, String username, String password, UserType type) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.username = username;
    }

    public UserType getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
