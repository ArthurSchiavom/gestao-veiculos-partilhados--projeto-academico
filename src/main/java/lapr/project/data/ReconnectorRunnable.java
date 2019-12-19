package lapr.project.data;

import lapr.project.utils.Updateable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReconnectorRunnable implements Runnable {
    private static final Logger LOGGER = Logger.getLogger("ReconnectorRunnableLog");

    // SonarQube complains if non-static variables are upper-case
    private final Updateable<Boolean> attemptingToReconnect;
    private final DataHandler dataHandler;
    private final int maxReconnectionAttempts;
    private final int reconnectionIntevalMillis;

    /**
     * Lock running variable (keep synchronized) when running this runnable;
     *
     * @param attemptingToReconnect                  variable that points out if the DataHandler is currently trying to reconnect. Will be set to false when this thread exits.
     * @param dataHandler                   dataHandler where connection should be reestablished
     * @param maxReconnectionAttempts       maximum of reconnection attempts
     * @param reconnectionIntevalMillis     interval between each reconnection attempt, in milliseconds
     */
    public ReconnectorRunnable(Updateable<Boolean> attemptingToReconnect, DataHandler dataHandler, int maxReconnectionAttempts, int reconnectionIntevalMillis) {
        this.attemptingToReconnect = attemptingToReconnect;
        this.dataHandler = dataHandler;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.reconnectionIntevalMillis = reconnectionIntevalMillis;
    }

    @Override
    public void run() {
        int nAttempt = 1;
        boolean connected = false;
        while (!connected && nAttempt <= maxReconnectionAttempts && !Shutdown.wasShutdownIssued()) {
            nAttempt++;
            connected = true;
            try {
                dataHandler.openConnection();
            } catch (Exception e) {
                connected = false;
            }

            try {
                synchronized (this) {
                    this.wait(reconnectionIntevalMillis);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Failed to make thread wait, skipping wait period. (attempt " + nAttempt + "/" + maxReconnectionAttempts + ")");
            }
        }
        attemptingToReconnect.setValue(false);
    }
}
