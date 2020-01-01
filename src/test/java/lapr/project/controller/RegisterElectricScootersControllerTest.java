package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
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


        // The controller is using the mock DataHandler, which will return the mock CallableStatement
        try {
            assertEquals(2, controller.registerElectricScooters("testFiles/scooters.txt"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            // Check that all these methods have been called

            // #all
            verify(mockPS1, times(2)).setString(2, VehicleType.ELECTRIC_SCOOTER.getSQLName());
            verify(mockPS1, times(2)).setInt(3, 1);

            // #1
            verify(mockPS1).setString(1, "Cool scooter");
            verify(mockPS1).setInt(4, 40);
            verify(mockPS1).setFloat(5, 1.34f);
            verify(mockPS1).setFloat(6, 10.3f);
            // #2
            verify(mockPS1).setString(1, "Kakkoii scooter");
            verify(mockPS1).setInt(4, 50);
            verify(mockPS1).setFloat(5, 1.44f);
            verify(mockPS1).setFloat(6, 10.5f);


            // #1
            verify(mockPS3).setString(1, "Cool scooter");
            verify(mockPS3).setString(2, ElectricScooterType.URBAN.getSQLName());
            verify(mockPS3).setFloat(3, 1.4f);
            verify(mockPS3).setInt(4, 55);
            verify(mockPS3).setInt(5, 10000);

            // #2
            verify(mockPS3).setString(1, "Kakkoii scooter");
            verify(mockPS3).setString(2, ElectricScooterType.OFFROAD.getSQLName());
            verify(mockPS3).setFloat(3, 1.5f);
            verify(mockPS3).setInt(4, 56);
            verify(mockPS3).setInt(5, 10050);


            // #1
            verify(mockPS3).setDouble(1, -80.222);
            verify(mockPS3).setDouble(2, 172.12);

            // #2
            verify(mockPS3).setDouble(1, -20.222);
            verify(mockPS3).setDouble(2, 122.12);


            // #all
            verify(mockPS3, times(2)).setString(1, "pls");
            verify(mockPS3).setString(2, "Cool scooter");
            verify(mockPS3).setString(2, "Kakkoii scooter");
        } catch (Exception e) {
            fail();
        }

        testExceptionCase("testFiles/MissingColumnScooterFile.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/MissingColumnScooterFile2.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueScootersFile.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueScootersFile2.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/WrongValueScootersFile3.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/scootersBadHeader.txt", InvalidFileDataException.class);
        testExceptionCase("testFiles/fail.txt", FileNotFoundException.class);
        // Can't test the SQL Exception case because the database is a mock object, so no methods depending on it will fail
    }

    private <T extends Exception> void testExceptionCase(String filePath, Class<T> exceptionClass) {
        try {
            controller.registerElectricScooters(filePath);
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }
}
