package lapr.project.controller;

import lapr.project.utils.Utils;
import org.junit.jupiter.api.Test;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import org.junit.jupiter.api.BeforeAll;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class RegisterPOIControllerTest {

    private static DataHandler dh;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static RegisterPOIController controller;

    @BeforeAll
    static void prepare() {
        dh = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new RegisterPOIController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareStatement(any(String.class))).thenReturn(preparedStatement);
            when(dh.executeUpdate(preparedStatement)).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addPoisTest() {
        try {
            int result = controller.registerPOIs("testFiles/poi.txt");
            assertEquals(2, result,"should be 2 but was: "+result);
        } catch (Exception e) {
            fail();
        }
        try {
            // Check that all these methods have been called once
            verify(preparedStatement).setDouble(1, 3.31);
            verify(preparedStatement).setDouble(2, 3.14);
            verify(preparedStatement).setInt(3, 18);
            verify(preparedStatement).setString(4, "description1");


            verify(preparedStatement).setDouble(1, 85.13);
            verify(preparedStatement).setDouble(2, -15.14);
            verify(preparedStatement).setInt(3, 0);
            verify(preparedStatement).setString(4, "description2");


            verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
            verify(preparedStatement, times(4)).setDouble(anyInt(), anyDouble());
            verify(preparedStatement, times(2)).setString(anyInt(), anyString());
        } catch (Exception e) {
            fail();
        }
    }
}