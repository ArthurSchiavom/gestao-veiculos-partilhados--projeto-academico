package lapr.project.data;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoCloseableManager {
    private static final Logger LOGGER = Logger.getLogger("AutoCloseableCloserLog");

    private Set<AutoCloseable> autoCloseableList = new HashSet<>();

    public void addAutoCloseable(AutoCloseable closeable) {
        autoCloseableList.add(closeable);
    }

    public void closeAutoCloseables() {
        for (AutoCloseable obj : autoCloseableList) {
            if (obj != null) {
                try {
                    obj.close();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Failed to close an open AutoCloseable object: \n" + ex.getMessage());
                }
            }
        }
        autoCloseableList.clear();
    }
}
