package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.Utils;
import oracle.jdbc.proxy.annotation.Pre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RegisterElectricScootersControllerTest {
    // Set up mock objects and those that use the mock objects
    private static DataHandler dataHandler;
    private static Company company;
    private static RegisterElectricScootersController controller;

    // It's beforeEach because registerElectricScootersTest will edit them
    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new RegisterElectricScootersController(company);
    }

    @Test
    void registerElectricScootersTest() {
        PreparedStatement mockPS1 = mock(PreparedStatement.class);
        PreparedStatement mockPS2 = mock(PreparedStatement.class);
        PreparedStatement mockPS3 = mock(PreparedStatement.class);
        ResultSet mockRS1 = mock(ResultSet.class);
        ResultSet mockRS2 = mock(ResultSet.class);
        ResultSet mockRS3 = mock(ResultSet.class);
        try {
            when(mockRS1.getFloat("park_input_voltage")).thenReturn(1f);
            when(mockRS1.getFloat("park_input_current")).thenReturn(1f);
            when(mockRS1.getString("poi_description")).thenReturn("des");
            when(mockRS1.getInt("altitude_m")).thenReturn(2);

            when(mockRS2.getString("vehicle_type_name")).thenReturn("bicycle");
            when(mockRS2.getInt("park_capacity")).thenReturn(5);
            when(mockRS2.getInt("amount_occupied")).thenReturn(2);

            when(mockRS3.getFloat("park_input_voltage")).thenReturn(1f);
            when(mockRS3.getFloat("park_input_current")).thenReturn(1f);
            when(mockRS3.getString("poi_description")).thenReturn("asd");
            when(mockRS3.getInt("altitude_m")).thenReturn(1);
            when(mockRS3.getString("park_id")).thenReturn("pls");

            when(dataHandler.prepareStatement("INSERT INTO vehicles(description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area) VALUES (?, ?, ?, ?, ?, ?)"))
                    .thenReturn(mockPS1);
            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?"))
                    .thenReturn(mockPS2);
            when(dataHandler.prepareStatement("insert into electric_scooters(vehicle_description, electric_scooter_type_name, max_battery_capacity, " +
                    "actual_battery_capacity, engine_power) " +
                    "values(?, ?, ?, ?, ?)")).thenReturn(mockPS3);
            when(dataHandler.prepareStatement("select * from PARKS p, points_of_interest poi where p.LATITUDE = ? AND p.LONGITUDE = ? " +
                    "AND p.latitude = poi.latitude AND p.longitude = poi.longitude")).thenReturn(mockPS3);
            when(dataHandler.prepareStatement("Insert into park_vehicle(park_id, vehicle_description) values (?,?)")).thenReturn(mockPS3);
            when(dataHandler.executeQuery(mockPS1)).thenReturn(mockRS1);
            when(dataHandler.executeQuery(mockPS2)).thenReturn(mockRS2);
            when(dataHandler.executeQuery(mockPS3)).thenReturn(mockRS3);
        } catch (SQLException e) {
            fail();
        }

        List<String[]> parsedData = null;
        System.out.println(Company.getInstance());
        try {
            parsedData = Utils.parseDataFile("testFiles/scooters.txt", ";", "#");
        } catch (FileNotFoundException e) {fail("test file not present: testFiles/scooters.txt");}
        assertNotNull(parsedData);
        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            assertEquals(2, controller.registerElectricScooters(parsedData, "testFiles/scooters.txt"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            // Check that all these methods have been called once

            FIX BELOW ACCORDING TO NEW VALUES
//            vehiclesInsert.setString(1, description);
//            vehiclesInsert.setString(2, VehicleType.ELECTRIC_SCOOTER.getSQLName());
//            vehiclesInsert.setInt(3, 1);
//            vehiclesInsert.setInt(4, weight);
//            vehiclesInsert.setFloat(5, aerodynamicCoefficient);
//            vehiclesInsert.setFloat(6, frontalArea);
            verify(mockPS1).setInt(1, 40);
            verify(mockPS1).setFloat(2, 1.34f);
            verify(mockPS1).setFloat(3, 10.3f);
            verify(mockPS1).setString(4, ElectricScooterType.URBAN.getSQLName());
            verify(mockPS1).setString(5, "Cool scooter");
            verify(mockPS1).setFloat(6, 1.4f);
            verify(mockPS1).setInt(7, 55);
            verify(mockPS1).setInt(8, 10000);
            verify(mockPS1).setDouble(9, -80.222);
            verify(mockPS1).setDouble(10, 172.12);

            verify(mockPS1).setInt(1, 50);
            verify(mockPS1).setFloat(2, 1.44f);
            verify(mockPS1).setFloat(3, 10.5f);
            verify(mockPS1).setString(4, ElectricScooterType.OFFROAD.getSQLName());
            verify(mockPS1).setString(5, "Kakkoii scooter");
            verify(mockPS1).setFloat(6, 1.5f);
            verify(mockPS1).setInt(7, 56);
            verify(mockPS1).setInt(8, 10050);
            verify(mockPS1).setDouble(9, -20.222);
            verify(mockPS1).setDouble(10, 122.12);

            verify(mockPS1, times(6)).setInt(anyInt(), anyInt());
            verify(mockPS1, times(6)).setFloat(anyInt(), anyFloat());
            verify(mockPS1, times(4)).setString(anyInt(), anyString());
            verify(mockPS1, times(4)).setDouble(anyInt(), anyDouble());
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
