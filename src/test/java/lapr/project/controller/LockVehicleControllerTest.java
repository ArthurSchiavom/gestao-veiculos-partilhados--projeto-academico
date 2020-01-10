package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Park;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class LockVehicleControllerTest {
    private DataHandler dataHandler;
    private Company company;
    private LockVehicleController controller;

    @BeforeAll
    static void loadProperties() {
        try {
            Properties properties =
                    new Properties(System.getProperties());
            InputStream input = new FileInputStream("target/classes/application.properties");
            properties.load(input);
            input.close();
            System.setProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new LockVehicleController(company);
    }

    @Test
    void lockVehicleTest() {
        String userEmail = "1180842@isep.ipp.pt";
        String parkId = "wellDonePark";
        String vehicleDescription = "vehicleDesc";

        PreparedStatement preparedStatement1 = mock(PreparedStatement.class);
        CallableStatement callableStatement1 = mock(CallableStatement.class);

        try {
            ResultSet resultSet = mock(ResultSet.class);
            when(dataHandler.prepareStatement(anyString())).thenReturn(preparedStatement1);
            when(dataHandler.executeUpdate(preparedStatement1)).thenReturn(1);

            when(dataHandler.executeQuery(anyObject())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getString(anyString())).thenReturn("true");
            when(resultSet.getTimestamp(anyString())).thenReturn(new Timestamp(1000))
                    .thenReturn(new Timestamp(120000));


            when(dataHandler.prepareCall(anyString())).thenReturn(callableStatement1);
            when(callableStatement1.getString(1)).thenReturn(userEmail);

            controller.lockVehicle(parkId, vehicleDescription, true);

            verify(dataHandler).prepareStatement("Insert into park_vehicle(park_id, vehicle_description) values (?,?)");
            verify(preparedStatement1).setString(1, parkId);
            verify(preparedStatement1).setString(2, vehicleDescription);
            verify(dataHandler).executeUpdate(preparedStatement1);

            verify(callableStatement1).setString(2, vehicleDescription);
            verify(dataHandler).executeUpdate(callableStatement1);

            verify(preparedStatement1, times(2)).close();
            verify(callableStatement1).close();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void lockVehicleWithParkCoordinatesTest() {
        String userEmail = "1180842@isep.ipp.pt";
        String parkId = "wellDonePark";
        String vehicleDescription = "vehicleDesc";
        double latitude = 10;
        double longitude = 50;

        PreparedStatement preparedStatement1 = mock(PreparedStatement.class);
        ResultSet resultSet1 = mock(ResultSet.class);
        CallableStatement callableStatement1 = mock(CallableStatement.class);

        try {
            when(dataHandler.prepareStatement(anyString())).thenReturn(preparedStatement1);
            when(dataHandler.executeQuery(preparedStatement1)).thenReturn(resultSet1);
            when(resultSet1.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);
            when(dataHandler.executeUpdate(preparedStatement1)).thenReturn(1);

            when(resultSet1.getString(anyString())).thenReturn("true");
            when(resultSet1.getTimestamp(anyString())).thenReturn(new Timestamp(0))
                    .thenReturn(new Timestamp(120000));

            when(dataHandler.prepareCall(anyString())).thenReturn(callableStatement1);
            when(callableStatement1.getString(1)).thenReturn(userEmail);

            when(resultSet1.getString("park_id")).thenReturn(parkId);
            when(resultSet1.getFloat("park_input_voltage")).thenReturn(10f);
            when(resultSet1.getFloat("park_input_current")).thenReturn(30f);
            when(resultSet1.getString("poi_description")).thenReturn("poi desc");
            when(resultSet1.getInt("altitude_m")).thenReturn(50);
            when(resultSet1.getString("vehicle_type_name")).thenReturn("bicycle");
            when(resultSet1.getInt("park_capacity")).thenReturn(10);
            when(resultSet1.getInt("amount_occupied")).thenReturn(2);

            controller.lockVehicle(latitude, longitude, vehicleDescription, false);
            // Ignore TripAPI.lockVehicle verifies since it is verified in the other test

            verify(preparedStatement1).setDouble(1, latitude);
            verify(preparedStatement1).setDouble(2, longitude);

            verify(preparedStatement1, times(4)).close();
            verify(resultSet1, times(3)).close();

            verify(preparedStatement1, times(2)).setString(1, parkId);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
