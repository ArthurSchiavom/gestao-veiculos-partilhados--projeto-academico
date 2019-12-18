package lapr.project.data;

import lapr.project.data.registers.Company;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shutdown {
    private Shutdown() {}

    public static void shutdown() {
        DataHandler dh = Company.getInstance().getDataHandler();
        dh.closeAll();
    }
}
