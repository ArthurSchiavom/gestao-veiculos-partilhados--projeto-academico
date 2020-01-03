package lapr.project.model.users;

/**
 * Represents a client of the application, which is an user
 */
public class Client extends User {
    public static final char GENDER_MALE = 'M';
    public static final char GENDER_FEMALE = 'F';

    private final int points;
    private final int height;
    private final int weight;
    private final char gender; // M or F, to simplify
    private final float cyclingAverageSpeed;
    private final boolean isRiding;
    private final CreditCard creditCard;

    /**
     * Instantiates a client object
     *  @param email    the client email
     * @param password the client password
     * @param height   the height (in cms) of the client
     * @param weight   the weight (in kgs) of the client
     * @param gender   the physical gender of the client
     * @param isRiding
     */
    public Client(String email, String username, String password, int height, int weight, char gender, float cyclingAverageSpeed, boolean isRiding, CreditCard creditCard) {
        super(email, username, password, UserType.CLIENT);
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.isRiding = isRiding;
        this.points = 0;
        this.creditCard = creditCard;
        this.cyclingAverageSpeed = cyclingAverageSpeed;
    }

    /**
     * Instantiates an already existing client
     *  @param email               the email of the client
     * @param username            the username of the client
     * @param password            the password of the client
     * @param points              the points of the client
     * @param height              the height in cms of the client
     * @param weight              the weight in cms of the client
     * @param gender              the gender(just a char either m or f)
     * @param cyclingAverageSpeed the average cycling speed in kms/h
     * @param isRiding
     * @param creditCard          the credit card info of the client
     */
    public Client(String email, String username, String password, int points, int height, int weight, char gender, float cyclingAverageSpeed, boolean isRiding, CreditCard creditCard) {
        super(email, username, password, UserType.CLIENT);
        this.points = points;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.isRiding = isRiding;
        this.creditCard = creditCard;
        this.cyclingAverageSpeed = cyclingAverageSpeed;
    }

    public int getPoints() {
        return points;
    }

    public float getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public char getGender() {
        return gender;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public float getCyclingAverageSpeed() {
        return cyclingAverageSpeed;
    }

    public boolean isRiding() {
        return isRiding;
    }

    @Override
    public String toString() {
        return "Client{" +
                "points=" + points +
                ", height=" + height +
                ", weight=" + weight +
                ", gender=" + gender +
                ", cyclingAverageSpeed=" + cyclingAverageSpeed +
                ", cc=" + creditCard.toString() +
                '}';
    }
}
