package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.point.of.interest.park.Park;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class FindParksNearbyControllerTest {
    private static DataHandler dataHandler;
    private static Company company;
    private static FindParksNearbyController controller;
    private static PreparedStatement preparedStatement1;
    private static ResultSet resultSet1;
    private static PreparedStatement preparedStatement2;
    private static ResultSet resultSet2;

    @BeforeAll
    static void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new FindParksNearbyController(company);
        preparedStatement1 = mock(PreparedStatement.class);
        resultSet1 = mock(ResultSet.class);
        preparedStatement2 = mock(PreparedStatement.class);
        resultSet2 = mock(ResultSet.class);
    }

    private void prepareFindParksNearbyTest() {
        reset(dataHandler);
        reset(preparedStatement1);
        reset(resultSet1);
        reset(preparedStatement2);
        reset(resultSet2);

        try {
            when(dataHandler.prepareStatement("Select p.park_id, p.latitude, p.longitude, p.park_input_voltage, p.park_input_current, p.available, poi.altitude_m, poi.poi_description from parks p, points_of_interest poi WHERE p.latitude = poi.latitude AND p.longitude = poi.longitude"))
                    .thenReturn(preparedStatement1);
            when(dataHandler.executeQuery(preparedStatement1)).thenReturn(resultSet1);


            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?"))
                    .thenReturn(preparedStatement2);
            when(dataHandler.executeQuery(preparedStatement2)).thenReturn(resultSet2);
            when(resultSet2.getString("vehicle_type_name")).thenReturn("n");
            when(resultSet2.getInt("park_capacity")).thenReturn(2);
            when(resultSet2.getInt("amount_occupied")).thenReturn(2);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void findParksNearbyTest() {
        prepareFindParksNearbyTest();

        List<Park> result;
        try {
            when(resultSet1.next()).thenReturn(true, true, true, true, false);
            when(resultSet1.getString("park_id")).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4");
            when(resultSet1.getDouble("latitude")).thenReturn(5.001).thenReturn(5.0).thenReturn(5.001).thenReturn(5.01);
            when(resultSet1.getDouble("longitude")).thenReturn(6.0).thenReturn(7.0).thenReturn(6.005).thenReturn(6.0);
            when(resultSet1.getInt("altitude_m")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4);
            when(resultSet1.getFloat("park_input_voltage")).thenReturn(5f).thenReturn(6f).thenReturn(7f).thenReturn(8f);
            when(resultSet1.getFloat("park_input_current")).thenReturn(9f).thenReturn(10f).thenReturn(11f).thenReturn(12f);
            when(resultSet1.getString("poi_description")).thenReturn("poiDesc1").thenReturn("poiDesc2").thenReturn("poiDesc3").thenReturn("poiDesc4");

            result = controller.findParksNearby(5, 6, 0);
            assertEquals(2, result.size());
            assertEquals("1", result.get(0).getId());
            assertEquals("3", result.get(1).getId());
            assertEquals("1", result.get(0).getId());
            assertEquals("3", result.get(1).getId());
            assertEquals(5.001, result.get(0).getCoordinates().getLatitude());
            assertEquals(6.0, result.get(0).getCoordinates().getLongitude());
            assertEquals(5.001, result.get(1).getCoordinates().getLatitude());
            assertEquals(6.005, result.get(1).getCoordinates().getLongitude());

            verify(preparedStatement1).close();
            verify(preparedStatement2, times(4)).close(); // For each park
            verify(resultSet1).close();
            verify(resultSet2, times(4)).close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }


        prepareFindParksNearbyTest();
        try {
            when(resultSet1.next()).thenReturn(false);

            result = controller.findParksNearby(8, 8, 2);
            assertEquals(0, result.size());

            verify(preparedStatement1).close();
            verify(resultSet1).close();
            verify(resultSet2, never()).close(); // For each park
            verify(preparedStatement2, never()).close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }
}
