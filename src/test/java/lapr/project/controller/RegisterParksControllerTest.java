package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RegisterParksControllerTest {
    // Set up mock objects and those that use the mock objects
    private static DataHandler dh;
    private static CallableStatement callableStatement;
    private static Company company;
    private static RegisterParksController controller;

    @BeforeAll
    static void prepare() {
        dh = mock(DataHandler.class);
        callableStatement = mock(CallableStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new RegisterParksController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareCall(any(String.class))).thenReturn(callableStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void registerParksTest() {
        List<String[]> parsedData = null;
        try {
            parsedData = Utils.parseDataFile("testFiles/parks.txt", ";", "#");
        } catch (FileNotFoundException e) {
            fail("test file not present: testFiles/parks.txt");
        }
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            assertEquals(2, controller.registerParks(parsedData, "testFiles/parks.txt"));
        } catch (Exception e) {
            fail();
        }

        try {
            // Check that all these methods have been called once
            // park #1
            Coordinates coordinates = new Coordinates(50d, 180d, 0);
            verify(callableStatement).setString(1, "p1");
            verify(callableStatement).setDouble(2, coordinates.getLatitude());
            verify(callableStatement).setDouble(3, coordinates.getLongitude());
            verify(callableStatement).setInt(4, coordinates.getAltitude());
            verify(callableStatement).setString(5, "Some desc");
            verify(callableStatement).setFloat(6, 17.4f);
            verify(callableStatement).setFloat(7, 19.9f);
            verify(callableStatement).setInt(8, 15);
            verify(callableStatement).setInt(9, 12);

            // park #2
            coordinates = new Coordinates(90d, 150.56d, 30);
            verify(callableStatement).setString(1, "p2");
            verify(callableStatement).setDouble(2, coordinates.getLatitude());
            verify(callableStatement).setDouble(3, coordinates.getLongitude());
            verify(callableStatement).setInt(4, coordinates.getAltitude());
            verify(callableStatement).setString(5, "Some desc2");
            verify(callableStatement).setFloat(6, 18.4f);
            verify(callableStatement).setFloat(7, 19.8f);
            verify(callableStatement).setInt(8, 16);
            verify(callableStatement).setInt(9, 13);

            // Verify that only the Statement.setX() above were called
            verify(callableStatement, times(6)).setInt(anyInt(), anyInt());
            verify(callableStatement, times(4)).setFloat(anyInt(), anyFloat());
            verify(callableStatement, times(4)).setString(anyInt(), anyString());
            verify(callableStatement, times(4)).setDouble(anyInt(), anyDouble());
        } catch (Exception e) {
            fail();
        }

        // Testing files with invalid data, should throw exceptions
        testInvalidFileDataExceptionCase("testFiles/MissingColumnBicyclesFile.txt");
        testInvalidFileDataExceptionCase("testFiles/MissingColumnBicyclesFile2.txt");
        testInvalidFileDataExceptionCase("testFiles/WrongValueBicyclesFile.txt");
        testInvalidFileDataExceptionCase("testFiles/WrongValueBicyclesFile2.txt");
        testInvalidFileDataExceptionCase("testFiles/WrongValueBicyclesFile3.txt");
        // Can't test the SQL Exception case because the database is a mock object, so no methods depending on it will fail
    }

    private void testInvalidFileDataExceptionCase(String filePath) {
        List<String[]> parsedData = null;

        try {
            parsedData = Utils.parseDataFile(filePath, ";", "#");
        } catch (FileNotFoundException e) {
            fail("test file not present");
        }
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            controller.registerParks(parsedData, filePath);
            fail();
        } catch (InvalidFileDataException e) {
            //pass
        } catch (SQLException e) {
            fail();
        }
    }
}
