package lapr.project.data;

import lapr.project.data.registers.Company;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bootstrap {
    private static final Logger LOGGER = Logger.getLogger("BootLog");
    private static boolean isAppBootedUp = false;

    private Bootstrap() {}

    public synchronized static void boot() throws SQLException {
        if (isAppBootedUp)
            return;
        isAppBootedUp = true;
        Locale.setDefault(Locale.US);
        Company.reset();

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
            dataHandler = DataHandler.createDataHandler();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        Company.createCompany(dataHandler);
        LOGGER.log(Level.INFO, "Boot complete");
        // Launch UI
    }

    public static boolean isAppBootedUp() {
        return isAppBootedUp;
    }

    static void setIsAppBootedUp(boolean isAppBootedUp) {
        Bootstrap.isAppBootedUp = isAppBootedUp;
    }

    public static void resetDatabase() throws IOException, SQLException {
        DataHandler dataHandler = Company.getInstance().getDataHandler();
        dataHandler.runSQLScriptNoCommit("Modelo Relacional/Script/Reset.sql");
        dataHandler.commitTransaction();
    }
}
