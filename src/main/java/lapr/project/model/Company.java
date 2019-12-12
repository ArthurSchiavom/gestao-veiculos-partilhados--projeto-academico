package lapr.project.model;

import lapr.project.data.DataHandler;

import java.sql.Connection;

/**
 * Represents a company
 */
public class Company {
    private static Company instance = null;
    private DataHandler dataHandler;
    /**
     * Represents a Singleton of the class Company
     */
    private Company(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Represents a Singleton of the class Company
     * @return the company
     */
    public static Company createCompany(DataHandler dataHandler){
        if(instance != null){
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
}
