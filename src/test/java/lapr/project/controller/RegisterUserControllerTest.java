package lapr.project.controller;

import lapr.project.model.users.Client;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Matchers;

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

    @BeforeEach
    void prepare() {
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
        try {
            int result = controller.registerClients("testFiles/user.txt");
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
            e.printStackTrace();
            fail();
        }

        testExceptionCase("testFiles/userBadHeader.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/userMissingColumn.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/userWrongValues.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/inexistent.txt", FileNotFoundException.class);
    }

    @Test
    void registerClientTest() {
        String username = "userNaMe";
        String email = "email@";
        String password = "pwd";
        String visa = "1234567890";
        int heightCm = 180;
        int weightKg = 60;
        String gender = Character.toString(Client.GENDER_FEMALE);
        float cyclingAverageSpeed = 10;

        try {
            controller.registerClient(username, email, password, visa, heightCm, weightKg, gender, cyclingAverageSpeed);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            // Check that all these methods have been called once
            verify(preparedStatement).setString(9, "1000100");
            verify(preparedStatement).setString(1, email);
            verify(preparedStatement).setInt(4, heightCm);
            verify(preparedStatement).setInt(5, weightKg);
            verify(preparedStatement).setFloat(7,1f);
            verify(preparedStatement).setString(3, visa);
            verify(preparedStatement).setString(6, gender);
            verify(preparedStatement).setString(8, password);

            verify(preparedStatement).setFloat(2, 0);

            verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
            verify(preparedStatement, times(2)).setFloat(anyInt(), anyFloat());
            verify(preparedStatement, times(5)).setString(anyInt(), anyString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            gender = Character.toString(Client.GENDER_MALE);
            controller.registerClient(username, email, password, visa, heightCm, weightKg, gender, cyclingAverageSpeed);
            gender = "f";
            controller.registerClient(username, email, password, visa, heightCm, weightKg, gender, cyclingAverageSpeed);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private <T extends Exception> void testExceptionCase(String filePath, Class<T> exceptionClass) {
        try {
            controller.registerClients(filePath);
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }
}