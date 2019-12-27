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

class RegisterUserControllerTest {

    private static DataHandler dh;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static RegisterUserController controller;

    @BeforeAll
    static void prepare() {
        dh = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new RegisterUserController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareStatement(any(String.class))).thenReturn(preparedStatement);
            when(dh.executeUpdate(preparedStatement)).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addUsers() {
        List<String[]> parsedData = null;
        try {
            parsedData = Utils.parseDataFile("testFiles/user.txt", ";", "#");
        } catch (FileNotFoundException e) {fail("test file not present");}
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock PreparedStatement
        try {
            int result = controller.registerClients(parsedData, "testFiles/user.txt");
            assertEquals(2, result,"should be 2 but was: "+result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        try {
            // Check that all these methods have been called once
            verify(preparedStatement).setString(9, "diogo");
            verify(preparedStatement).setString(1, "diog@g.g");
            verify(preparedStatement).setInt(4, 301);
            verify(preparedStatement).setInt(5, 75);
            verify(preparedStatement).setFloat(7,98);
            verify(preparedStatement).setString(3, "1234567812345678");
            verify(preparedStatement).setString(6, "M");
            verify(preparedStatement).setString(8, "passe");

            verify(preparedStatement).setString(9, "josefina");
            verify(preparedStatement).setString(1, "ola@g.g");
            verify(preparedStatement).setInt(4, 160);
            verify(preparedStatement).setInt(5, 76);
            verify(preparedStatement).setFloat(7,99);
            verify(preparedStatement).setString(3, "1234567812345670");
            verify(preparedStatement).setString(6, "F");
            verify(preparedStatement).setString(8, "passe1");

            verify(preparedStatement, times(2)).setFloat(2, 0);

            verify(preparedStatement, times(4)).setInt(anyInt(), anyInt());
            verify(preparedStatement, times(4)).setFloat(anyInt(), anyFloat());
            verify(preparedStatement, times(10)).setString(anyInt(), anyString());
        } catch (Exception e) {
            fail();
        }
    }
}