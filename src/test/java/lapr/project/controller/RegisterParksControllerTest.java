package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void prepare() {
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
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            assertEquals(2, controller.registerParks("testFiles/parks.txt"));
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
        testExceptionCase("testFiles/MissingColumnBicyclesFile.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/MissingColumnBicyclesFile2.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueBicyclesFile.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueBicyclesFile2.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueBicyclesFile3.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/parksBadHeader.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/inv.txt", FileNotFoundException.class);
        // Can't test the SQL Exception case because the database is a mock object, so no methods depending on it will fail
    }
    @Test
    void registerParkTest() {
        String id = "id";
        double latitude = 12;
        double longitude = 13;
        int altitude = 14;
        int maxBikes = 15;
        int maxScooters = 16;
        float inputVoltage = 17f;
        float inputCurrent = 18f;
        String description = "desc";

        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            controller.registerPark(id, latitude, longitude, altitude, maxBikes, maxScooters, inputVoltage, inputCurrent, description);
        } catch (Exception e) {
            fail();
        }

        try {
            // Check that all these methods have been called once
            Coordinates coordinates = new Coordinates(latitude,longitude,altitude);
            verify(callableStatement).setString(1, id);
            verify(callableStatement).setDouble(2, coordinates.getLatitude());
            verify(callableStatement).setDouble(3, coordinates.getLongitude());
            verify(callableStatement).setInt(4, coordinates.getAltitude());
            verify(callableStatement).setString(5, description);
            verify(callableStatement).setFloat(6, inputVoltage);
            verify(callableStatement).setFloat(7, inputCurrent);
            verify(callableStatement).setInt(8, maxScooters);
            verify(callableStatement).setInt(9, maxBikes);

            // Verify that only the Statement.setX() above were called
            verify(callableStatement, times(3)).setInt(anyInt(), anyInt());
            verify(callableStatement, times(2)).setFloat(anyInt(), anyFloat());
            verify(callableStatement, times(2)).setString(anyInt(), anyString());
            verify(callableStatement, times(2)).setDouble(anyInt(), anyDouble());
        } catch (Exception e) {
            fail();
        }

    }



    private <T extends Exception> void testExceptionCase(String filePath, Class<T> exceptionClass) {
        try {
            controller.registerParks(filePath);
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }
}
