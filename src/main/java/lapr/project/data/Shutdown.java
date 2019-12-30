package lapr.project.data;

import lapr.project.data.registers.Company;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Shutdown {
    private static final Logger LOGGER = Logger.getLogger("ShutdownLog");
    private Shutdown() {}

    public static synchronized void shutdown() {
        if (!Bootstrap.isAppBootedUp())
            return;
        Company.getInstance().getDataHandler().closeAll();
        Bootstrap.setIsAppBootedUp(false);
        LOGGER.log(Level.INFO, "Shutdown complete");
    }
}
