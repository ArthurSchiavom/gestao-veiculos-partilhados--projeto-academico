package lapr.project.ui;

import lapr.project.data.Bootstrap;
import lapr.project.data.Shutdown;

import java.sql.SQLException;

class Main {

    /**
     * Application main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Bootstrap.boot();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Shutdown.shutdown();
    }
}

