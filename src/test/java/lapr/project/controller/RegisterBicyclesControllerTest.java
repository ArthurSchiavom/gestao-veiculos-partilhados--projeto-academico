package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.data.registers.VehicleRegister;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for the RegisterBicyclesController class.
 * <br><b>Note to team: Use this as example for your tests</b>                                                                                                              <br><br>
 * Mock classes override the methods returns, returning "empty" values for any method:                                                                                      <br>
 *      null for objects, 0 for numbers, false for boolean, empty collections for collections, void keeps void,...                                                          <br><br>
 * These default values can be replaced by values we specify, for different set of parameters.                                                                              <br>
 *      For example, we could make DataHandler.execute(cs1) return 3 and DataHandler.execute(cs2) return 5.                                                                 <br>
 *      So if some of the methods we use depend on these functions, we can make them return the results we want without needing the database:                               <br>
 *      * when(mockClass.method(parameters)).thenReturn(whatToReturn);                                                                                                      <br><br>
 *      * when(mockClass.method(parameters)).thenThrow(new IllegalArgumentException(...));                                                                                  <br>
 *  Furthermore, we can verify if certain methods did indeed run, and if they did with certain values:                                                                      <br>
 *      * verify(mockClass).method(parameters);                                                                                                                             <br>
 *      * verify(mockClass).method(12, "hello", someObject);                                                                                                                <br>
 *      * verify(mockClass).method(anyInt(), any(String.class));                                                                                                            <br><br>
 *  Above, you check if the method has been called **ONCE**. You need to use verify(mockObject, VerificationMode)                                                           <br>
 *       to check if it ran more or less than 1 time:                                                                                                                       <br>
 *       * verify(test, never()).someMethod("never called");                                                                                                                <br>
 *       * verify(test, atLeastOnce()).someMethod("called at least once");                                                                                                  <br>
 *       * verify(test, atLeast(2)).someMethod("called at least twice");                                                                                                    <br>
 *       * verify(test, times(5)).someMethod("called five times");                                                                                                          <br>
 *       * verify(test, atMost(3)).someMethod("called at most 3 times");
 */
public class RegisterBicyclesControllerTest {
    // Set up mock objects and those that use the mock objects
    private static DataHandler dh = mock(DataHandler.class);
    private static CallableStatement callableStatement = mock(CallableStatement.class);
    private static Company company = Company.createCompany(dh);
    private static RegisterBicyclesController controller = new RegisterBicyclesController(company);

    @BeforeAll
    static void prepare() {
        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareCall(any(String.class))).thenReturn(callableStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void registerBicyclesTest() {
        List<String[]> parsedData = null;
        try {
            parsedData = Utils.parseDataFile("b.txt", ";", "#");
        } catch (FileNotFoundException e) {fail("test file not present");}
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            assertEquals(2, controller.registerBicycles(parsedData, "b.txt"));
        } catch (Exception e) {
            fail();
        }

        try {
            // Check that all these methods have been called once
            verify(callableStatement).setInt(1, 20);
            verify(callableStatement).setFloat(2, 5.33f);
            verify(callableStatement).setFloat(3, 4.4f);
            verify(callableStatement).setInt(4, 88);
            verify(callableStatement).setString(5, "dog bike");
            verify(callableStatement).setDouble(6, -20.222);
            verify(callableStatement).setDouble(7, 122.12);

            verify(callableStatement).setInt(1, 10);
            verify(callableStatement).setFloat(2, 2.32f);
            verify(callableStatement).setFloat(3, 1.4f);
            verify(callableStatement).setInt(4, 99);
            verify(callableStatement).setString(5, "cat bike");
            verify(callableStatement).setDouble(6, -80.222);
            verify(callableStatement).setDouble(7, 172.12);
        } catch (Exception e) {
            fail();
        }

        testInvalidFileDataExceptionCase("testFiles\\MissingColumnBicyclesFile.txt");
        testInvalidFileDataExceptionCase("testFiles\\MissingColumnBicyclesFile2.txt");
        testInvalidFileDataExceptionCase("testFiles\\WrongValueBicyclesFile.txt");
        testInvalidFileDataExceptionCase("testFiles\\WrongValueBicyclesFile2.txt");
        testInvalidFileDataExceptionCase("testFiles\\WrongValueBicyclesFile3.txt");
        // Can't test the SQL Exception case because the database is a mock object, so no methods depending on it will fail
    }

    private void testInvalidFileDataExceptionCase(String fileName) {
        List<String[]> parsedData = null;

        try {
            parsedData = Utils.parseDataFile(fileName, ";", "#");
        } catch (FileNotFoundException e) {fail("test file not present");}
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            controller.registerBicycles(parsedData, "b.txt");
            fail();
        } catch (InvalidFileDataException e) {
            //pass
        } catch (SQLException e) {
            fail();
        }
    }
}
