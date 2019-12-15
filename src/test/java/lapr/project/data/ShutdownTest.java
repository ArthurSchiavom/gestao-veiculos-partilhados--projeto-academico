package lapr.project.data;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lapr.project.data.Bootstrap;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static lapr.project.data.Shutdown.shutdown;
import static org.junit.jupiter.api.Assertions.fail;

public class ShutdownTest {

    @BeforeAll
    static void before() throws SQLException {
        Bootstrap.boot();
    }

    void testShutdown() {
        shutdown();
        DataHandler dh = Company.getInstance().getDataHandler();
        try {
            dh.executeQuery(dh.prepareStatement("select * from vehicles"));
            fail();
        } catch (SQLException e) {}
    }
}
