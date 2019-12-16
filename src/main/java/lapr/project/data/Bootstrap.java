package lapr.project.data;

import lapr.project.data.registers.Company;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bootstrap {
    private static final Logger LOGGER = Logger.getLogger("BootLog");

    private Bootstrap() {}

    /**
     * O URL da BD. nota: sonarqube não permite nomes que começem por letra maiúscula
     */
    private static final String JDBC_URL = "jdbc:oracle:thin:@vsrvbd1.dei.isep.ipp.pt:1521/pdborcl";

    /**
     * O nome de utilizador da BD. nota: sonarqube não permite nomes que começem por letra maiúscula
     */
    private static final String USERNAME ="LAPR3_2019_G029";

    /**
     * A password de utilizador da BD. nota: sonarqube não permite nomes que começem por letra maiúscula
     */
    private static final String WORDPASS = "melhorgrupoole";

    public static void boot() throws SQLException {
        DataHandler dataHandler = null;
        try {
            dataHandler = new DataHandler(JDBC_URL, USERNAME, WORDPASS);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        Company.createCompany(dataHandler);
        // Launch UI
    }
}
