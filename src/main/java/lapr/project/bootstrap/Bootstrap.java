package lapr.project.bootstrap;

import lapr.project.data.DataHandler;
import lapr.project.model.Company;

import java.sql.SQLException;

public class Bootstrap {
    /**
     * O URL da BD.
     */
    private final String JDBCURL = "jdbc:oracle:thin:@vsrvbd1.dei.isep.ipp.pt:1521/pdborcl";

    /**
     * O nome de utilizador da BD.
     */
    private final String USERNAME ="LAPR3_2019_G029";

    /**
     * A password de utilizador da BD.
     */
    private final String password = "melhorgrupoole";

    public void boot(){
        DataHandler dataHandler = null;
        try {
            dataHandler = new DataHandler(JDBCURL, USERNAME,password);
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            return;
        }
        Company.createCompany(dataHandler);
        // Launch UI
    }
}
