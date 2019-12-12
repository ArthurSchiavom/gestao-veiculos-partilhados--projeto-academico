package lapr.project.model.Users;

import java.util.Objects;

/**
 * Represents a client of the application, which is an user
 */
public class Client extends User {

    private final int points, creditCardSecret, age;
    private final float height, weight;
    private final char gender; // M or F, to simplify
    private final String creditCardNumber, creditCardExpiration; // expiration is in the format DD-MM-YYYY

    /**
     *Constructor for new clients (so the points are 0)
     */
    public Client(String email, String password, int creditCardSecret, int age, float height, float weight, char gender, String creditCardNumber, String creditCardExpiration) {
        super(email, password, UserType.CLIENT);
        this.creditCardSecret = creditCardSecret;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiration = creditCardExpiration;
        this.points = 0;
    }

    /**
     *Constructor for already existent Clients
     */
    public Client(String email, String password, int points, int creditCardSecret, int age, float height, float weight, char gender, String creditCardNumber, String creditCardExpiration) {
        super(email, password, UserType.CLIENT);
        this.points = points;
        this.creditCardSecret = creditCardSecret;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiration = creditCardExpiration;
    }

    public int getPoints() {
        return points;
    }

    public int getCreditCardSecret() {
        return creditCardSecret;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public char getGender() {
        return gender;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCreditCardExpiration() {
        return creditCardExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return getPoints() == client.getPoints() && getEmail().equalsIgnoreCase(client.getEmail()) &&
                getPassword().equals(client.getPassword()) &&
                getType() == client.getType() &&
                getCreditCardSecret() == client.getCreditCardSecret() &&
                getAge() == client.getAge() &&
                Float.compare(client.getHeight(), getHeight()) == 0 &&
                Float.compare(client.getWeight(), getWeight()) == 0 &&
                getGender() == client.getGender() &&
                getCreditCardNumber().equals(client.getCreditCardNumber()) &&
                getCreditCardExpiration().equals(client.getCreditCardExpiration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPoints(), getCreditCardSecret(), getAge(), getHeight(), getWeight(), getGender(), getCreditCardNumber(), getCreditCardExpiration());
    }
}
