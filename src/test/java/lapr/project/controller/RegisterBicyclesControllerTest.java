package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for the RegisterBicyclesController class.
 * <br><b>Note to team: Use this as example for your tests</b>                                                                                                              <br><br>
 * Mock classes override the methods returns, returning "empty" values for any method:                                                                                      <br>
 * null for objects, 0 for numbers, false for boolean, empty collections for collections, void keeps void,...                                                          <br><br>
 * These default values can be replaced by values we specify, for different set of parameters.                                                                              <br>
 * For example, we could make DataHandler.execute(cs1) return 3 and DataHandler.execute(cs2) return 5.                                                                 <br>
 * So if some of the methods we use depend on these functions, we can make them return the results we want without needing the database:                               <br>
 * * when(mockClass.method(parameters)).thenReturn(whatToReturn);                                                                                                      <br><br>
 * * when(mockClass.method(parameters)).thenThrow(new IllegalArgumentException(...));                                                                                  <br>
 * Furthermore, we can verify if certain methods did indeed run, and if they did with certain values:                                                                      <br>
 * * verify(mockClass).method(parameters);                                                                                                                             <br>
 * * verify(mockClass).method(12, "hello", someObject);                                                                                                                <br>
 * * verify(mockClass).method(anyInt(), any(String.class));                                                                                                            <br><br>
 * Above, you check if the method has been called **ONCE**. You need to use verify(mockObject, VerificationMode)                                                           <br>
 * to check if it ran more or less than 1 time:                                                                                                                       <br>
 * * verify(test, never()).someMethod("never called");                                                                                                                <br>
 * * verify(test, atLeastOnce()).someMethod("called at least once");                                                                                                  <br>
 * * verify(test, atLeast(2)).someMethod("called at least twice");                                                                                                    <br>
 * * verify(test, times(5)).someMethod("called five times");                                                                                                          <br>
 * * verify(test, atMost(3)).someMethod("called at most 3 times");
 */
public class RegisterBicyclesControllerTest {
    // Set up mock objects and those that use the mock objects
    private static DataHandler dh;
    private static CallableStatement callableStatement;
    private static Company company;
    private static RegisterBicyclesController controller;

    @BeforeAll
    static void prepare() {
        dh = mock(DataHandler.class);
        callableStatement = mock(CallableStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new RegisterBicyclesController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareCall(any(String.class))).thenReturn(callableStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void registerBicyclesTest() {
        try {
            assertEquals(2, controller.registerBicycles("testFiles/bicycles.txt"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            // Check that all these methods have been called once
            // bike #1
            verify(callableStatement).setInt(1, 10);
            verify(callableStatement).setFloat(2, 2.32f);
            verify(callableStatement).setFloat(3, 1.4f);
            verify(callableStatement).setInt(4, 99);
            verify(callableStatement).setString(5, "cat bike");
            verify(callableStatement).setDouble(6, -80.222);
            verify(callableStatement).setDouble(7, 172.12);

            // bike #2
            verify(callableStatement).setInt(1, 20);
            verify(callableStatement).setFloat(2, 5.33f);
            verify(callableStatement).setFloat(3, 4.4f);
            verify(callableStatement).setInt(4, 88);
            verify(callableStatement).setString(5, "dog bike");
            verify(callableStatement).setDouble(6, -20.222);
            verify(callableStatement).setDouble(7, 122.12);

            // Verify that only the Statement.setX() above were called
            verify(callableStatement, times(4)).setInt(anyInt(), anyInt());
            verify(callableStatement, times(4)).setFloat(anyInt(), anyFloat());
            verify(callableStatement, times(2)).setString(anyInt(), anyString());
            verify(callableStatement, times(4)).setDouble(anyInt(), anyDouble());
        } catch (Exception e) {
            fail();
        }

        // Testing files with invalid data, should throw exceptions
        testExceptionCase("testFiles/MissingColumnBicyclesFile.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/MissingColumnBicyclesFile2.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueBicyclesFile.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueBicyclesFile2.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueBicyclesFile3.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/bicyclesBadHeader.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/Inexistent.txt", FileNotFoundException.class);
        // Can't test the SQL Exception case because the database is a mock object, so no methods depending on it will fail
    }

    private <T extends Exception> void testExceptionCase(String filePath, Class<T> exceptionClass) {
        try {
            controller.registerBicycles(filePath);
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }
}
