package lapr.project.model.users;

/**
 * Represents a client of the application, which is an user
 */
public class Client extends User {

    private final int points, age, height, weight;
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
        super(email, username, password, UserType.CLIENT);
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.points = 0;
        this.cc = creditCard;
        this.cyclingAverageSpeed = cyclingAverageSpeed;
    }

    /**
     * Instantiates an already existing client
     *
     * @param email               the email of the client
     * @param username            the username of the client
     * @param password            the password of the client
     * @param points              the points of the client
     * @param age                 the age in years of the client
     * @param height              the height in cms of the client
     * @param weight              the weight in cms of the client
     * @param gender              the gender(just a char either m or f)
     * @param cyclingAverageSpeed the average cycling speed in kms/h
     * @param creditCard          the credit card info of the client
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

    @Override
    public String toString() {
        return "Client{" +
                "points=" + points +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", gender=" + gender +
                ", cyclingAverageSpeed=" + cyclingAverageSpeed +
                ", cc=" + cc.toString() +
                '}';
    }
}
