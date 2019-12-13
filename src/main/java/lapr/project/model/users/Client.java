package lapr.project.model.users;

/**
 * Represents a client of the application, which is an user
 */
public class Client extends User {

    private final int points, age, height;
    private final float weight;
    private final char gender; // M or F, to simplify
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
    public Client(String email, String password, int age, int height, float weight, char gender, CreditCard creditCard) {
        super(email, password, UserType.CLIENT);
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.points = 0;
        this.cc = creditCard;
    }

    /**
     * Constructor for already existent Clients
     */
    public Client(String email, String password, int points, int age, int height, float weight, char gender, CreditCard creditCard) {
        super(email, password, UserType.CLIENT);
        this.points = points;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.cc = creditCard;
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
    
    public float getWeight() {
        return weight;
    }

    public char getGender() {
        return gender;
    }

    public CreditCard getCreditCard() {
        return cc;
    }
}
