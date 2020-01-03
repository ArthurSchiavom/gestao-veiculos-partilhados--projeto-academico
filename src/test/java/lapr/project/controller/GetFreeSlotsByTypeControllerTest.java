package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.VehicleType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class GetFreeSlotsByTypeControllerTest {
    private static DataHandler dataHandler;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static GetFreeSlotsByTypeController controller;

    @BeforeAll
    static void prepare() {
        dataHandler = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new GetFreeSlotsByTypeController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dataHandler.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test of getFreeSlotsByType method, of class GetFreeSlotsByTypeController.
     */
    @Test
    public void testGetFreeSlotsByType() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        //Metodo fetchParkById()
        when(dataHandler.executeQuery(preparedStatement)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getDouble("latitude")).thenReturn(1.0);
        when(resultSet.getDouble("longitude")).thenReturn(1.0);
        when(resultSet.getInt("altitude_m")).thenReturn(1);
        when(resultSet.getFloat("park_input_voltage")).thenReturn(1.0f);
        when(resultSet.getFloat("park_input_current")).thenReturn(1.0f);
        when(resultSet.getString("poi_description")).thenReturn("dd");
        when(resultSet.getString("vehicle_type_name")).thenReturn("bicycle");
        controller.getFreeSlotsByType("00", "teste", VehicleType.BICYCLE);
    }
}