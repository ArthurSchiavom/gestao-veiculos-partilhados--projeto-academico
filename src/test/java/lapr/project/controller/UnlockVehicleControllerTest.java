package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UnlockVehicleControllerTest {
    private static DataHandler dataHandler;
    private static Company company;
    private static UnlockVehicleController controller;

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new UnlockVehicleController(company);
    }

    @Test
    void startTripTest() {
        String username = "username";
        String vehicleDescription = "vDesc";
        String userEmail = "email@";
        String parkId = "parkId@";

        PreparedStatement preparedStatement1 = mock(PreparedStatement.class);
        ResultSet resultSet1 = mock(ResultSet.class);
        try {
            when(dataHandler.prepareStatement(anyString())).thenReturn(preparedStatement1);
            when(dataHandler.executeQuery(preparedStatement1)).thenReturn(resultSet1);
            when(resultSet1.next()).thenReturn(true);

            when(resultSet1.getString("park_id")).thenReturn(parkId);

            when(resultSet1.getString("user_email")).thenReturn(userEmail);
            when(resultSet1.getString("user_password")).thenReturn("userP4ssword");
            when(resultSet1.getInt("points")).thenReturn(10);
            when(resultSet1.getString("visa")).thenReturn("visaNum");
            when(resultSet1.getInt("height_cm")).thenReturn(11);
            when(resultSet1.getInt("weight_kg")).thenReturn(12);
            when(resultSet1.getString("gender")).thenReturn("female");
            when(resultSet1.getFloat("cycling_average_speed")).thenReturn(13.0f);
            when(resultSet1.getBoolean("is_riding")).thenReturn(true);

            when(dataHandler.executeUpdate(preparedStatement1)).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            controller.startTrip(username, vehicleDescription);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        try {
            verify(preparedStatement1, times(3)).setString(1, userEmail);
            verify(preparedStatement1).setString(2, userEmail);
            verify(preparedStatement1, times(2)).setString(1, vehicleDescription);
            verify(preparedStatement1).setString(2, vehicleDescription);

            verify(dataHandler).prepareStatement("select PARK_ID from PARK_VEHICLE where VEHICLE_DESCRIPTION = ?");

            verify(dataHandler, times(2)).prepareStatement("SELECT * FROM registered_users where USER_NAME = ?");
            verify(preparedStatement1, times(2)).setString(1, username);
            verify(dataHandler, times(2)).prepareStatement("SELECT * FROM clients where USER_EMAIL like ?");

            verify(dataHandler).prepareStatement("DELETE FROM PARK_VEHICLE WHERE VEHICLE_DESCRIPTION = ?");

            verify(dataHandler).prepareStatement("INSERT INTO TRIPS(start_time, user_email, vehicle_description, start_park_id)" +
                    "VALUES(current_timestamp, ?, ?, ?)");
            verify(preparedStatement1).setString(3, parkId);

            verify(dataHandler).prepareStatement("update clients set is_riding = ? where user_email = ?");
            verify(preparedStatement1).setInt(1, 1);
            verify(preparedStatement1).setString(2, userEmail);
            verify(preparedStatement1, times(6)).close();
            verify(resultSet1, times(3)).close();
        } catch (Exception e) {
            fail();
        }


        try {
            when(resultSet1.next()).thenReturn(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        testExceptionCase(username, vehicleDescription, SQLException.class);
    }

    private <T extends Exception> void testExceptionCase(String username, String vehicleDescription, Class<T> exceptionClass) {
        try {
            controller.startTrip(username, vehicleDescription);
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }
}
