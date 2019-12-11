package lapr.project.model;

import java.sql.Connection;

/**
 * Represents a company
 */
public class Company {
    private static Company instance = null;
    private Connection connection;
    /**
     * Represents a Singleton of the class Company
     */
    private Company(Connection connection) {
        this.connection = connection;
    }

    /**
     * Represents a Singleton of the class Company
     * @return the company
     */
    public static Company createCompany(Connection connection){
        if(instance != null){
            instance = new Company(connection);
        }
        return instance;
    }

    public static Company getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
