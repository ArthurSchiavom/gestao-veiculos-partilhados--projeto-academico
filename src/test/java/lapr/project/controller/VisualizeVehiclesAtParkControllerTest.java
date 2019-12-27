package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VisualizeVehiclesAtParkControllerTest {
    // Set up mock objects and those that use the mock objects
    private DataHandler dataHandler;
    private Company company;
    private VisualizeVehiclesAtParkController controller;
    private PreparedStatement stm1 = mock(PreparedStatement.class);
    private PreparedStatement stm2 = mock(PreparedStatement.class);
    private PreparedStatement stm3 = mock(PreparedStatement.class);
    private PreparedStatement stm4 = mock(PreparedStatement.class);
    private PreparedStatement stm5 = mock(PreparedStatement.class);
    private ResultSet rs1 = mock(ResultSet.class);
    private ResultSet rs2 = mock(ResultSet.class);
    private ResultSet rs3 = mock(ResultSet.class);
    private ResultSet rs4 = mock(ResultSet.class);
    private ResultSet rs5 = mock(ResultSet.class);

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new VisualizeVehiclesAtParkController(company);
    }

    private void prepareGetVehicles(String vehicleDescription, VehicleType vehicleType) {
        reset(stm1);
        reset(stm2);
        reset(stm3);
        reset(rs1);
        reset(rs2);
        reset(rs3);

        try {
            when(dataHandler.prepareStatement("Select * from PARK_VEHICLE where PARK_ID = ?")).thenReturn(stm1);
            when(dataHandler.executeQuery(stm1)).thenReturn(rs1);
            when(dataHandler.prepareStatement("select * from vehicles where description = ?")).thenReturn(stm2);
            when(dataHandler.executeQuery(stm2)).thenReturn(rs2);
            when(dataHandler.prepareStatement("select * from " + "bicycles" + " where vehicle_description = ?")).thenReturn(stm3);
            when(dataHandler.prepareStatement("select * from " + "electric_scooters" + " where vehicle_description = ?")).thenReturn(stm3);
            when(dataHandler.executeQuery(stm3)).thenReturn(rs3);

            when(rs1.getString("vehicle_description")).thenReturn(vehicleDescription);
            when(rs1.next()).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rs2.next()).thenReturn(true);
            when(rs3.next()).thenReturn(true);
            when(rs2.getString(anyString())).thenReturn(vehicleType.getSQLName());
            when(rs2.getInt(anyString())).thenReturn(1);
            when(rs2.getFloat(anyString())).thenReturn(2f);
            when(rs3.getInt(anyString())).thenReturn(1);
            when(rs3.getFloat(anyString())).thenReturn(2f);
            when(rs3.getString(anyString())).thenReturn(ElectricScooterType.URBAN.getSQLName());
            when(rs3.getBoolean(anyString())).thenReturn(true);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void verifyGetVehicles(String parkId, String vehicleDescription) {
        try {
            verify(stm1).setString(1, parkId);
            verify(stm2, times(2)).setString(1, vehicleDescription);
            verify(stm3, times(2)).setString(1, vehicleDescription);

            verify(stm1).close();
            verify(stm2, times(2)).close();
            verify(stm3, times(2)).close();
            verify(rs1).close();
            verify(rs2, times(2)).close();
            verify(rs3, times(2)).close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void prepareGetVehiclesWithParkCoordinates(String vehicleDescription, String parkId, VehicleType vehicleType) {
        prepareGetVehicles(vehicleDescription, vehicleType);
        reset(stm4);
        reset(stm5);
        reset(rs4);
        reset(rs5);

        try {
            when(dataHandler.prepareStatement("select * from PARKS p, points_of_interest poi where p.LATITUDE = ? AND p.LONGITUDE = ? " +
                    "AND p.latitude = poi.latitude AND p.longitude = poi.longitude")).thenReturn(stm4);
            when(dataHandler.executeQuery(stm4)).thenReturn(rs4);
            when(rs4.getString(anyString())).thenReturn(parkId);
            when(rs4.getFloat(anyString())).thenReturn(1f);
            when(rs4.getInt(anyString())).thenReturn(2);

            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?")).thenReturn(stm5);
            when(dataHandler.executeQuery(stm5)).thenReturn(rs5);
            when(rs5.next()).thenReturn(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void verifyGetVehiclesWithParkCoordinates(String parkId, String vehicleDescription, double lat, double lon) {
        verifyGetVehicles(parkId, vehicleDescription);

        try {
            verify(stm4).setDouble(1, lat);
            verify(stm4).setDouble(2, lon);

            verify(stm5).setString(1, parkId);

            verify(stm4).close();
            verify(stm5).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getVehiclesAtParkIdTest() {
        List<Bicycle> bicycles = null;
        List<ElectricScooter> electricScooters = null;
        String parkId = "parkId";
        String vehicleDescription = "vehicleDesc";

        prepareGetVehicles(vehicleDescription, VehicleType.BICYCLE);
        try {
            bicycles = controller.getVehiclesAtPark(parkId, Bicycle.class);
            verifyGetVehicles(parkId, vehicleDescription);
            assertEquals(2, bicycles.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        try {
            when(rs1.next()).thenReturn(false);
            bicycles = controller.getVehiclesAtPark(parkId, Bicycle.class);
            assertEquals(0, bicycles.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        prepareGetVehicles(vehicleDescription, VehicleType.ELECTRIC_SCOOTER);
        try {
            electricScooters = controller.getVehiclesAtPark(parkId, ElectricScooter.class);
            verifyGetVehicles(parkId, vehicleDescription);
            assertEquals(2, electricScooters.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        try {
            when(rs1.next()).thenReturn(false);
            electricScooters = controller.getVehiclesAtPark(parkId, ElectricScooter.class);
            assertEquals(0, electricScooters.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getVehiclesAtParkCoordinatesTest() {
        List<Bicycle> bicycles = null;
        String parkId = "parkId";
        String vehicleDescription = "vehicleDesc";
        double lat = 123;
        double lon = 124;

        prepareGetVehiclesWithParkCoordinates(vehicleDescription, parkId, VehicleType.BICYCLE);
        try {
            bicycles = controller.getVehiclesAtPark(lat, lon, Bicycle.class);
            assertEquals(2, bicycles.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        verifyGetVehiclesWithParkCoordinates(parkId, vehicleDescription, lat, lon);
    }


    @Test
    void writeOutputFileTest() {
        String filePath = "testFiles/temp/VisualizeVehiclesAtParkControllerTest.writeOutputFileTest.output";
        String header = "bicycle description";
        String desc1 = "abc";
        String desc2 = "aac";
        String desc3 = "zyw";
        String desc4 = "xxy";
        List<Bicycle> bicycles = new ArrayList<>();

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add(header);

        try {
            controller.writeOutputFile(bicycles, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        verifyOutputFile(expectedResult, filePath);

        bicycles.add(new Bicycle(1, desc1, 2f, 2f, 2, true, 10));
        bicycles.add(new Bicycle(1, desc2, 2f, 2f, 2, true, 10));
        bicycles.add(new Bicycle(1, desc3, 2f, 2f, 2, true, 10));
        bicycles.add(new Bicycle(1, desc4, 2f, 2f, 2, true, 10));
        expectedResult.add(desc2);
        expectedResult.add(desc1);
        expectedResult.add(desc4);
        expectedResult.add(desc3);
        try {
            controller.writeOutputFile(bicycles, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        verifyOutputFile(expectedResult, filePath);
    }

    void verifyOutputFile(List<String> expectedContent, String filePath) {
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            int count = 0;
            while (scanner.hasNext()) {
                assertEquals(expectedContent.get(count), scanner.nextLine());
                count++;
            }
            if (count != expectedContent.size())
                fail();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            fail();
        }
    }
}
