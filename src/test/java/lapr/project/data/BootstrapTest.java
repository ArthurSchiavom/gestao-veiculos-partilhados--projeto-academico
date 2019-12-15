package lapr.project.data;

import lapr.project.data.Bootstrap;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.data.Shutdown;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class BootstrapTest {

    @AfterAll
    static void after() {
        Shutdown.shutdown();
    }

    
    void bootTest() {
        try {
            Bootstrap.boot();
        } catch (SQLException e) {
            fail();
        }
        Company company = Company.getInstance();
        assertNotNull(company);
        try {
            DataHandler dh = company.getDataHandler();
            dh.executeQuery(dh.prepareStatement("select * from vehicles"));
        } catch (SQLException e) {
            fail();
        }
    }
}