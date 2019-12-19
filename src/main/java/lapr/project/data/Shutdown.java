package lapr.project.data;

import lapr.project.data.registers.Company;

public class Shutdown {
    private Shutdown() {}
    private static boolean wasShutdownIssued = false;

    public static void shutdown() {
        wasShutdownIssued = true;
        DataHandler dh = Company.getInstance().getDataHandler();
        dh.closeAll();
    }

    public static boolean wasShutdownIssued() {
        return wasShutdownIssued;
    }
}
