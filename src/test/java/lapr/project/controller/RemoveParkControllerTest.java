package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RemoveParkControllerTest {

    private static DataHandler dataHandler;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static RemoveParkController controller;

    @BeforeAll
    static void prepare() {
        dataHandler = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new RemoveParkController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dataHandler.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void removeParkTest() {
        try {
            controller.removePark("test");

            verify(preparedStatement).setInt(1, 0);
            verify(preparedStatement).setString(2, "test");
        } catch (SQLException e) {
            fail();
        }
    }
}
