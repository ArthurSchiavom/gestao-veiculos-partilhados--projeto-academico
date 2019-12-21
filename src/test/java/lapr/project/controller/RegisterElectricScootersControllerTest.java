package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RegisterElectricScootersControllerTest {
    // Set up mock objects and those that use the mock objects
    private static DataHandler dh = mock(DataHandler.class);
    private static CallableStatement callableStatement = mock(CallableStatement.class);
    private static Company company = Company.createCompany(dh);
    private static RegisterElectricScootersController controller = new RegisterElectricScootersController(company);

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
    void registerElectricScootersTest() {
        List<String[]> parsedData = null;
        try {
            parsedData = Utils.parseDataFile("s.txt", ";", "#");
        } catch (FileNotFoundException e) {fail("test file not present: s.txt");}
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            assertEquals(2, controller.registerElectricScooters(parsedData, "s.txt"));
        } catch (Exception e) {
            fail();
        }

        try {
            // Check that all these methods have been called once
            // These methods fail when I run all tests together but pass when I run the test individually
//            verify(callableStatement).setInt(1, 40);
//            verify(callableStatement).setFloat(2, 1.34f);
//            verify(callableStatement).setFloat(3, 10.3f);
//            verify(callableStatement).setString(4, ElectricScooterType.URBAN.getSQLName());
//            verify(callableStatement).setString(5, "Cool scooter");
//            verify(callableStatement).setFloat(6, 1.4f);
//            verify(callableStatement).setInt(7, 55);
//            verify(callableStatement).setInt(8, 10000);
//            verify(callableStatement).setDouble(9, -80.222);
//            verify(callableStatement).setDouble(10, 172.12);
//
//            verify(callableStatement).setInt(1, 50);
//            verify(callableStatement).setFloat(2, 1.44f);
//            verify(callableStatement).setFloat(3, 10.5f);
//            verify(callableStatement).setString(4, ElectricScooterType.OFFROAD.getSQLName());
//            verify(callableStatement).setString(5, "Kakkoii scooter");
//            verify(callableStatement).setFloat(6, 1.5f);
//            verify(callableStatement).setInt(7, 56);
//            verify(callableStatement).setInt(8, 10050);
//            verify(callableStatement).setDouble(9, -20.222);
//            verify(callableStatement).setDouble(10, 122.12);
//
//            verify(callableStatement, times(6)).setInt(anyInt(), anyInt());
//            verify(callableStatement, times(6)).setFloat(anyInt(), anyFloat());
//            verify(callableStatement, times(4)).setString(anyInt(), anyString());
//            verify(callableStatement, times(4)).setDouble(anyInt(), anyDouble());
        } catch (Exception e) {
            fail();
        }

        testInvalidFileDataExceptionCase("testFiles/MissingColumnScooterFile.txt");
        testInvalidFileDataExceptionCase("testFiles/MissingColumnScooterFile2.txt");
        testInvalidFileDataExceptionCase("testFiles/WrongValueScootersFile.txt");
        testInvalidFileDataExceptionCase("testFiles/WrongValueScootersFile2.txt");
        testInvalidFileDataExceptionCase("testFiles/WrongValueScootersFile3.txt");
        // Can't test the SQL Exception case because the database is a mock object, so no methods depending on it will fail
    }

    private void testInvalidFileDataExceptionCase(String filePath) {
        List<String[]> parsedData = null;

        try {
            parsedData = Utils.parseDataFile(filePath, ";", "#");
        } catch (FileNotFoundException e) {fail("test file not present: "+ filePath);}
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            controller.registerElectricScooters(parsedData, filePath);
            fail();
        } catch (InvalidFileDataException e) {
            //pass
        } catch (SQLException e) {
            fail();
        }
    }
}
