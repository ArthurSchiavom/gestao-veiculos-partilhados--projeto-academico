package lapr.project.shutdown;

import lapr.project.data.registers.Company;

public class Shutdown {
    public static void shutdown() {
        Company.getInstance().getDataHandler().closeAll();
    }
}
