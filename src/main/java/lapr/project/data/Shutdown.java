package lapr.project.data;

import lapr.project.data.registers.Company;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shutdown {
    private static final Logger LOGGER = Logger.getLogger("ShutdownLog");

    private Shutdown() {}

    public static void shutdown() {
        DataHandler dh = Company.getInstance().getDataHandler();
        try {
            dh.rollbackTransaction();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "failed to rollback transaction");
        }
        dh.closeAll();
    }
}
