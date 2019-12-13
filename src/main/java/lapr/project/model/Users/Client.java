package lapr.project.model.Users;

/**
 * Represents a client of the application, which is an user
 */
public class Client extends User {

    private final int points, age, height,weight;
    private final char gender; // M or F, to simplify
    private final float cyclingAverageSpeed;
    private final CreditCard cc;

    /**
     * Instantiates a client object
     *
     * @param email    the client email
     * @param password the client password
     * @param age      the age of the client in years
     * @param height   the height (in cms) of the client
     * @param weight   the weight (in kgs) of the client
     * @param gender   the physical gender of the client
     */
    public Client(String email, String username, String password, int age, int height, int weight, char gender, float cyclingAverageSpeed, CreditCard creditCard) {
        super(email,username, password, UserType.CLIENT);
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.points = 0;
        this.cc = creditCard;
        this.cyclingAverageSpeed = cyclingAverageSpeed;
    }

    /**
     * Constructor for already existent Clients
     */
    public Client(String email, String username, String password, int points, int age, int height, int weight, char gender, float cyclingAverageSpeed, CreditCard creditCard) {
        super(email, username, password, UserType.CLIENT);
        this.points = points;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.cc = creditCard;
        this.cyclingAverageSpeed = cyclingAverageSpeed;
    }

    public int getPoints() {
        return points;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public CreditCard getCc() {
        return cc;
    }
    
    public int getWeight() {
        return weight;
    }

    public char getGender() {
        return gender;
    }

    public CreditCard getCreditCard() {
        return cc;
    }

    public float getCyclingAverageSpeed() {
        return cyclingAverageSpeed;
    }
}
