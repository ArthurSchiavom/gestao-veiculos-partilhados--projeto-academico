package lapr.project.model.Users;

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
        super(email, password, ClientType.CLIENT);
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
        super(email, password, ClientType.CLIENT);
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
}
