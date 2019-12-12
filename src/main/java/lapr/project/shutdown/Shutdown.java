package lapr.project.shutdown;

import lapr.project.model.Company;

public class Shutdown {
    public static void shutdown() {
        Company.getInstance().getDataHandler().closeAll();
    }
}
