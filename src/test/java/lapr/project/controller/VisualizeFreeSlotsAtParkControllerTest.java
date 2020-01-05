package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.UnregisteredDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class VisualizeFreeSlotsAtParkControllerTest {
    private DataHandler dataHandler;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Company company;
    private VisualizeFreeSlotsAtParkController controller;

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new VisualizeFreeSlotsAtParkController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dataHandler.prepareStatement(any(String.class))).thenReturn(preparedStatement);
            when(dataHandler.executeQuery(preparedStatement)).thenReturn(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareFetchFreeSlotsAtParkByType() {
        try {
            when(resultSet.getDouble("latitude")).thenReturn(1.0);
            when(resultSet.getDouble("longitude")).thenReturn(1.0);
            when(resultSet.getInt("altitude_m")).thenReturn(1);
            when(resultSet.getFloat("park_input_voltage")).thenReturn(1.0f);
            when(resultSet.getFloat("park_input_current")).thenReturn(1.0f);
            when(resultSet.getString("poi_description")).thenReturn("dd");
            when(resultSet.getString("vehicle_type_name")).thenReturn("bicycle");
            when(resultSet.getInt("park_capacity")).thenReturn(10);
            when(resultSet.getInt("amount_occupied")).thenReturn(3);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getFreeSlotsByType method, of class GetFreeSlotsByTypeController.
     */
    @Test
    public void fetchFreeSlotsAtParkByTypeTest() {
        String parkId = "00";
        VehicleType vehicleType = VehicleType.BICYCLE;
        int expected = 7;

        try {
            prepareFetchFreeSlotsAtParkByType();
            when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
            assertEquals(expected, controller.fetchFreeSlotsAtParkByType(parkId, vehicleType));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            prepare();
            when(resultSet.next()).thenReturn(false);
            testPrepareFetchFreeSlotsAtParkException(expected, parkId, vehicleType, UnregisteredDataException.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    private <T extends Exception> void testPrepareFetchFreeSlotsAtParkException(int expected, String parkId, VehicleType vehicleType, Class<T> exceptionClass) {
        prepareFetchFreeSlotsAtParkByType();
        try {
            assertEquals(expected, controller.fetchFreeSlotsAtParkByType(parkId, vehicleType));
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }












    private void prepareFetchFreeSlotsAtPark(int maxSlots, int nOccupiedSlots) {
        try {
            when(resultSet.getString(anyString())).thenReturn("mockString");
            when(resultSet.getFloat(anyString())).thenReturn(1f);
            when(resultSet.getBoolean(anyString())).thenReturn(true);
            when(resultSet.getInt(anyString())).thenReturn(2);
            when(resultSet.getTimestamp(anyString())).thenReturn(new Timestamp(Calendar.getInstance().getTimeInMillis()));

            when(resultSet.getString("vehicle_type_name")).thenReturn("bicycle");

            // ResultSet #2
            PreparedStatement preparedStatement2 = mock(PreparedStatement.class);
            ResultSet resultSet2 = mock(ResultSet.class);
            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?")).thenReturn(preparedStatement2);
            when(dataHandler.executeQuery(preparedStatement2)).thenReturn(resultSet2);

            when(resultSet2.getString(anyString())).thenReturn("mockString");
            when(resultSet2.getFloat(anyString())).thenReturn(1f);
            when(resultSet2.getBoolean(anyString())).thenReturn(true);
            when(resultSet2.getInt(anyString())).thenReturn(2);

            when(resultSet2.getString("vehicle_type_name")).thenReturn("bicycle");
            when(resultSet2.getInt("park_capacity")).thenReturn(maxSlots);
            when(resultSet2.getInt("amount_occupied")).thenReturn(nOccupiedSlots);
            when(resultSet2.next()).thenReturn(true).thenReturn(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void fetchFreeSlotsAtParkTest() {
        int maxSlots = 10;
        int nOccupiedSlots = 7;
        int expected = maxSlots - nOccupiedSlots;

        try {
            prepare();
            prepareFetchFreeSlotsAtPark(maxSlots, nOccupiedSlots);
            when(resultSet.next()).thenReturn(true);
            assertEquals(expected, controller.fetchFreeSlotsAtPark("any", "any"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            prepare();
            prepareFetchFreeSlotsAtPark(maxSlots, nOccupiedSlots);
            when(resultSet.next()).thenReturn(false);
            testFetchFreeSlotsAtParkTestException(maxSlots, nOccupiedSlots, UnregisteredDataException.class);

            prepare();
            prepareFetchFreeSlotsAtPark(maxSlots, nOccupiedSlots);
            when(resultSet.next()).thenReturn(true, true, false);
            testFetchFreeSlotsAtParkTestException(maxSlots, nOccupiedSlots, UnregisteredDataException.class);

            prepare();
            prepareFetchFreeSlotsAtPark(maxSlots, nOccupiedSlots);
            when(resultSet.next()).thenReturn(true, true, true, false);
            testFetchFreeSlotsAtParkTestException(maxSlots, nOccupiedSlots, IllegalStateException.class);
        } catch (SQLException e) {
            fail();
        }
    }

    private <T extends Exception> void testFetchFreeSlotsAtParkTestException(int maxSlots, int nOccupiedSlots, Class<T> exceptionClass) {
        int expected = maxSlots - nOccupiedSlots;
        prepareFetchFreeSlotsAtPark(maxSlots, nOccupiedSlots);

        try {
            assertEquals(expected, controller.fetchFreeSlotsAtPark("any", "any"));
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }
}