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


class RegisterPathControllerTest {

    private static DataHandler dh;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static RegisterPathController controller;

    @BeforeAll
    static void prepare() {
        dh = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new RegisterPathController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareStatement(any(String.class))).thenReturn(preparedStatement);
            when(dh.executeUpdate(preparedStatement)).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void registerPathsTest() {
        try {
            int result = controller.registerPaths("testFiles/path.txt");
            assertEquals(3, result,"should be 2 but was: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        try {
            // Check that all these methods have been called once
            verify(preparedStatement).setDouble(1, 30);
            verify(preparedStatement).setDouble(2, 33);
            verify(preparedStatement).setDouble(3, 25);
            verify(preparedStatement).setDouble(4, 26);
            verify(preparedStatement).setDouble(5, 7);
            verify(preparedStatement).setInt(6, 0);
            verify(preparedStatement).setDouble(7, 4);


            verify(preparedStatement).setDouble(1, 31);
            verify(preparedStatement).setDouble(2, 32);
            verify(preparedStatement).setDouble(3, 24);
            verify(preparedStatement).setDouble(4, 27);
            verify(preparedStatement).setDouble(5, 1);
            verify(preparedStatement).setInt(6, 2);
            verify(preparedStatement).setDouble(7, 0);

            verify(preparedStatement).setDouble(1, 15);
            verify(preparedStatement).setDouble(2, 16);
            verify(preparedStatement).setDouble(3, 17);
            verify(preparedStatement).setDouble(4, 18);
            verify(preparedStatement).setDouble(5, 0);
            verify(preparedStatement).setInt(6, 19);
            verify(preparedStatement).setDouble(7, 20);


            verify(preparedStatement, times(3)).setInt(anyInt(), anyInt());
            verify(preparedStatement, times(18)).setDouble(anyInt(), anyDouble());
        } catch (Exception e) {
            fail();
        }
    }
}