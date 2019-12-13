package lapr.project.model;

import lapr.project.data.DataHandler;
import lapr.project.register.TripRegister;
import lapr.project.register.UsersRegister;

import java.sql.Connection;

/**
 * Represents a company
 */
public class Company {
    private static Company instance = null;
    private DataHandler dataHandler;

    private UsersRegister usersRegister;
    private TripRegister tripRegister;

    /**
     * Represents a Singleton of the class Company
     */
    private Company(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        
        this.usersRegister = new UsersRegister(dataHandler);
        this.tripRegister = new TripRegister(dataHandler);
    }

    /**
     * Represents a Singleton of the class Company
     * @return the company
     */
    public static Company createCompany(DataHandler dataHandler){
        if(instance == null){
            instance = new Company(dataHandler);
        }
        return instance;
    }

    public static Company getInstance() {
        return instance;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public UsersRegister getUsersRegister() {
        return usersRegister;
    }

    public TripRegister getTripRegister() {
        return tripRegister;
    }
}
