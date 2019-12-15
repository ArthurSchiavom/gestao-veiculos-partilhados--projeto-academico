package lapr.project.shutdown;

import lapr.project.bootstrap.Bootstrap;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static lapr.project.shutdown.Shutdown.shutdown;
import static org.junit.jupiter.api.Assertions.fail;

public class ShutdownTest {

    @BeforeAll
    static void before() throws SQLException {
        Bootstrap.boot();
    }

    @Test
    void testShutdown() {
        shutdown();
        DataHandler dh = Company.getInstance().getDataHandler();
        try {
            dh.executeQuery(dh.prepareStatement("select * from vehicles"));
            fail();
        } catch (SQLException e) {}
    }
}
