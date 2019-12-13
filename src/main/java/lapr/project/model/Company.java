package lapr.project.model;

import lapr.project.data.DataHandler;
import lapr.project.model.register.ParkRegister;
import lapr.project.model.register.TripRegister;
import lapr.project.model.register.UsersRegister;
//import lapr.project.model.register.ParkRegister;
//import lapr.project.model.register.TripRegister;
//import lapr.project.model.register.UsersRegister;

/**
 * Represents a company
 */
public class Company {
    private static Company instance = null;
    private DataHandler dataHandler;

    private ParkRegister parkRegister;
    private UsersRegister usersRegister;
    private TripRegister tripRegister;

    /**
     * Represents a Singleton of the class Company
     */
    private Company(DataHandler dataHandler) {
        instance = this;
        this.dataHandler = dataHandler;

        this.parkRegister =new ParkRegister(dataHandler);
        this.usersRegister = new UsersRegister(dataHandler);
        this.tripRegister = new TripRegister(dataHandler);
    }

    /**
     * Represents a Singleton of the class Company
     * @return the company
     */
    public static Company createCompany(DataHandler dataHandler){
        if(instance == null){
            new Company(dataHandler);
        }
        return instance;
    }

    public static Company getInstance() {
        return instance;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public ParkRegister getParkRegister() {
        return parkRegister;
    }

    public UsersRegister getUsersRegister() {
        return usersRegister;
    }

    public TripRegister getTripRegister() {
        return tripRegister;
    }
}
