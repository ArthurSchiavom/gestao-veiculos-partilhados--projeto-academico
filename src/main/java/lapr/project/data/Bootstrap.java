package lapr.project.data;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;

import java.sql.SQLException;

public class Bootstrap {
    private Bootstrap() {}

    /**
     * O URL da BD. nota: sonarqube não permite nomes que começem por letra maiúscula
     */
    private static final String jdbcurl = "jdbc:oracle:thin:@vsrvbd1.dei.isep.ipp.pt:1521/pdborcl";

    /**
     * O nome de utilizador da BD. nota: sonarqube não permite nomes que começem por letra maiúscula
     */
    private static final String username ="LAPR3_2019_G029";

    /**
     * A password de utilizador da BD. nota: sonarqube não permite nomes que começem por letra maiúscula
     */
    private static final String wordpass = "melhorgrupoole";

    public static void boot() throws SQLException {
        DataHandler dataHandler = null;
        try {
            dataHandler = new DataHandler(jdbcurl, username, wordpass);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to the database");
        }
        Company.createCompany(dataHandler);
        // Launch UI
    }
}
