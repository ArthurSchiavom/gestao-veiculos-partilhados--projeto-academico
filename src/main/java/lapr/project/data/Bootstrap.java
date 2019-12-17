package lapr.project.data;

import lapr.project.data.registers.Company;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bootstrap {
    private static final Logger LOGGER = Logger.getLogger("BootLog");

    private Bootstrap() {}

    public static void boot() throws SQLException {
        try {
            Properties properties =
                    new Properties(System.getProperties());
            InputStream input = new FileInputStream("target/classes/application.properties");
            properties.load(input);
            input.close();
            System.setProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        DataHandler dataHandler = null;
        try {
            dataHandler = new DataHandler();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        Company.createCompany(dataHandler);
        // Launch UI
    }
}
