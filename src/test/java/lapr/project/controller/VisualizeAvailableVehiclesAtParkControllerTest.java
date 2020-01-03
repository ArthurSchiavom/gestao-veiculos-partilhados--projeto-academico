package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class VisualizeAvailableVehiclesAtParkControllerTest {

    private DataHandler dataHandler;
    private Company company;
    private VisualizeAvailableVehiclesAtParkController controller;
    private PreparedStatement stm1 = mock(PreparedStatement.class);
    private PreparedStatement stm2 = mock(PreparedStatement.class);
    private PreparedStatement stm3 = mock(PreparedStatement.class);
    private ResultSet rs1 = mock(ResultSet.class);
    private ResultSet rs2 = mock(ResultSet.class);
    private ResultSet rs3 = mock(ResultSet.class);

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new VisualizeAvailableVehiclesAtParkController(company);
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
            when(rs2.getBoolean(anyString())).thenReturn(true);

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

    @Test
    void fetchAvailableVehiclesAtParkTest() {
        String parkId = "parkID";
        String vehicleId = "IDd";
        String vehicleDescription = "Descc";

        List<Vehicle> result;
        prepareGetVehicles(vehicleDescription, VehicleType.BICYCLE);
        try {
            result = controller.fetchAvailableVehiclesAtPark(parkId);
            verifyGetVehicles(parkId, vehicleDescription);
            assertEquals(2, result.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        prepareGetVehicles(vehicleDescription, VehicleType.ELECTRIC_SCOOTER);
        try {
            when(rs2.getBoolean(anyString())).thenReturn(true).thenReturn(false);
            result = controller.fetchAvailableVehiclesAtPark(parkId);
            verifyGetVehicles(parkId, vehicleDescription);
            assertEquals(1, result.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        prepareGetVehicles(vehicleDescription, VehicleType.ELECTRIC_SCOOTER);
        try {
            when(rs2.getBoolean(anyString())).thenReturn(false);
            result = controller.fetchAvailableVehiclesAtPark(parkId);
            verifyGetVehicles(parkId, vehicleDescription);
            assertEquals(0, result.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }
}
