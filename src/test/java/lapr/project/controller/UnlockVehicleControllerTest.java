package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UnlockVehicleControllerTest {
    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String UNIQUE_NUMBER_FIELD_NAME = "unique_number";
    private static final String VEHICLE_TYPE_FIELD_NAME = "vehicle_type_name";
    private static final String AVAILABLE_FIELD_NAME = "available";
    private static final String WEIGHT_FIELD_NAME = "weight";
    private static final String AERO_COEFFICIENT_FIELD_NAME = "aerodynamic_coefficient";
    private static final String FRONTAL_AREA_FIELD_NAME = "frontal_area";

    private static final String ESCOOTER_TYPE_FIELD_NAME = "electric_scooter_type_name";
    private static final String ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME = "actual_battery_capacity";
    private static final String ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME = "max_battery_capacity";
    private static final String ESCOOTER_ENGINE_POWER_FIELD_NAME = "engine_power";

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

    @Test
    public void testStartTripPark() {
        final String OUTPUT_FILE_PATH = "testFiles/temp/UnlockVehicleControllerHighestBattery.output";
        PreparedStatement stm1 = mock(PreparedStatement.class);
        ResultSet rs1 = mock(ResultSet.class);
        try {
            when(dataHandler.prepareStatement(
                    "SELECT es.VEHICLE_DESCRIPTION, es.electric_scooter_type_name, es.actual_battery_capacity, es.max_battery_capacity, es.engine_power FROM electric_scooters es, (SELECT v.DESCRIPTION v_id FROM vehicles v INNER JOIN park_vehicle pv ON pv.VEHICLE_DESCRIPTION=v.DESCRIPTION AND pv.park_id like ? WHERE v.vehicle_type_name LIKE 'electric_scooter') pes WHERE pes.v_id=es.VEHICLE_DESCRIPTION ORDER BY es.actual_battery_capacity*es.max_battery_capacity DESC")).thenReturn(stm1);
            when(dataHandler.executeQuery(stm1)).thenReturn(rs1);
            //Important to add these when making more tests
            when(rs1.next()).thenReturn(false).thenReturn(true).thenReturn(true);

            //This test can't find a scooter in the park
            Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
            controller.startTripPark("ivo1", "Trindade", OUTPUT_FILE_PATH);
            try(Scanner scanner = new Scanner(new FileReader(OUTPUT_FILE_PATH))) {
                if(scanner.hasNext() && scanner.nextLine().equalsIgnoreCase("Could not find any available scooter at desired park.")) {
                    assertTrue(true, "Found the correct null message");
                } else {
                    fail("Should have found a \"Did not find available scooter\" but didn't, check if the message has not been changed");
                }
            } catch (IOException e) {
                fail("IOException on no scooters at park");
            }

            //This test can't find a scooter object but can find a description
            PreparedStatement stm2 = mock(PreparedStatement.class);
            ResultSet rs2 = mock(ResultSet.class);

            when(rs1.getString("vehicle_description")).thenReturn("PT001");
            when(dataHandler.prepareStatement("select * from vehicles where description like ?")).thenReturn(stm2);
            when(dataHandler.executeQuery(stm2)).thenReturn(rs2);
            //Important to add these
            when(rs2.next()).thenReturn(false).thenReturn(true);

            Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
            controller.startTripPark("ivo1", "Trindade", OUTPUT_FILE_PATH);
            try(Scanner scanner = new Scanner(new FileReader(OUTPUT_FILE_PATH))) {
                if(scanner.hasNext() && scanner.nextLine().equalsIgnoreCase("Could not find any available scooter at desired park.")) {
                    assertTrue(true, "Found the correct null message");
                } else {
                    fail("Should have found a \"Did not find available scooter\" but didn't, check if the message has not been changed");
                }
            } catch (IOException e) {
                fail("IOException on no scooters at park");
            }

            //Finds the scooter
            PreparedStatement stm2b = mock(PreparedStatement.class);
            ResultSet rs2b = mock(ResultSet.class);

            when(rs2.getString(VEHICLE_TYPE_FIELD_NAME)).thenReturn("electric_scooter");
            when(dataHandler.prepareStatement("select * from " + "electric_scooters" + " where vehicle_description like ?")).thenReturn(stm2b);
            when(rs2b.next()).thenReturn(true);
            when(dataHandler.executeQuery(stm2b)).thenReturn(rs2b);

            when(rs2.getInt(UNIQUE_NUMBER_FIELD_NAME)).thenReturn(1);
            when(rs2.getString(DESCRIPTION_FIELD_NAME)).thenReturn("PT001");
            when(rs2.getFloat(AERO_COEFFICIENT_FIELD_NAME)).thenReturn(1f);
            when(rs2.getFloat(FRONTAL_AREA_FIELD_NAME)).thenReturn(1f);
            when(rs2.getInt(WEIGHT_FIELD_NAME)).thenReturn(10);
            when(rs2.getBoolean(AVAILABLE_FIELD_NAME)).thenReturn(true);
            when(rs2b.getString(ESCOOTER_TYPE_FIELD_NAME)).thenReturn("urban");
            when(rs2b.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(75);
            when(rs2b.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(1f);
            when(rs2b.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME)).thenReturn(200);

            PreparedStatement stm3 = mock(PreparedStatement.class);
            ResultSet rs3 = mock(ResultSet.class);
            PreparedStatement stm4 = mock(PreparedStatement.class);
            ResultSet rs4 = mock(ResultSet.class);
            PreparedStatement stm5 = mock(PreparedStatement.class);
            ResultSet rs5 = mock(ResultSet.class);
            PreparedStatement stm6 = mock(PreparedStatement.class);
            PreparedStatement stm7 = mock(PreparedStatement.class);

            when(dataHandler.prepareStatement("select PARK_ID from PARK_VEHICLE where VEHICLE_DESCRIPTION = ?")).thenReturn(stm3);
            when(dataHandler.executeQuery(stm3)).thenReturn(rs3);
            when(rs3.next()).thenReturn(true);
            when(rs3.getString("park_id")).thenReturn("Trindade");
            when(dataHandler.prepareStatement("SELECT * FROM registered_users where USER_NAME = ?")).thenReturn(stm4);
            when(dataHandler.executeQuery(stm4)).thenReturn(rs4);
            when(rs4.next()).thenReturn(true);
            when(rs4.getString( "user_password")).thenReturn("333");
            when(rs4.getString("user_email")).thenReturn("c@c.c");
            when(dataHandler.prepareStatement("SELECT * FROM clients where USER_EMAIL like ?")).thenReturn(stm5);
            when(dataHandler.executeQuery(stm5)).thenReturn(rs5);
            when(rs5.next()).thenReturn(true);
            when(rs5.getInt("points")).thenReturn(300);
            when(rs5.getString("visa")).thenReturn("3333333333333333");
            when(rs5.getInt( "height_cm")).thenReturn(2);
            when(rs5.getInt( "weight_kg")).thenReturn(120);
            when(rs5.getString( "gender")).thenReturn("M");
            when(rs5.getFloat( "cycling_average_speed")).thenReturn(14.3f);
            when(rs5.getBoolean("is_riding")).thenReturn(false);

            when(dataHandler.prepareStatement("DELETE FROM PARK_VEHICLE WHERE VEHICLE_DESCRIPTION = ?")).thenReturn(stm5);
            when(dataHandler.executeUpdate(stm5)).thenReturn(1);
            when(dataHandler.prepareStatement("INSERT INTO TRIPS(start_time, user_email, vehicle_description, start_park_id)" +
                            "VALUES(current_timestamp, ?, ?, ?)")).thenReturn(stm6);
            when(dataHandler.executeUpdate(stm6)).thenReturn(1);
            when(dataHandler.prepareStatement("update clients set is_riding = ? where user_email = ?")).thenReturn(stm7);

            Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
            controller.startTripPark("ivo1", "Trindade", OUTPUT_FILE_PATH);
            verify(dataHandler, atLeastOnce()).commitTransaction();

            try(Scanner scanner = new Scanner(new FileReader(OUTPUT_FILE_PATH))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("escooter description;type;actual battery capacity")) {
                    if(scanner.hasNextLine() && scanner.nextLine().equalsIgnoreCase("PT001;city;75")) {
                        assertTrue(true, "Correctly found the scooter in the file");
                        Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
                    } else {
                        fail("The scooter was not correctly exported. Check if the variables match");
                    }
                } else {
                    fail("The header portion of the file was not exported correctly. Check if the headers match");
                }
            } catch (IOException e) {
                fail("IOException thrown when searching for file with a scooter");
            }
        } catch (SQLException e) {
            fail("SQLException thrown\n" + e.getMessage() + e.getStackTrace());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    void testStartTripParkDest() {
        final String OUTPUT_FILE_PATH = "testFiles/temp/UnlockVehicleControllerDestinationBattery.output";
        try {
            PreparedStatement stmFetchUser = mock(PreparedStatement.class);
            ResultSet rsFetchUser = mock(ResultSet.class);
            PreparedStatement stmFetchClient = mock(PreparedStatement.class);
            ResultSet rsFetchClient = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM registered_users where USER_NAME = ?")).thenReturn(stmFetchUser);
            when(dataHandler.executeQuery(stmFetchUser)).thenReturn(rsFetchUser);
            when(rsFetchUser.next()).thenReturn(false).thenReturn(true);

            PreparedStatement stmFetchParkID = mock(PreparedStatement.class);
            ResultSet rsFetchParkID = mock(ResultSet.class);
            PreparedStatement stmFetchParkCapacities = mock(PreparedStatement.class);
            ResultSet rsFetchParkCapacities = mock(ResultSet.class);

            when(dataHandler.prepareStatement("Select * from parks p, points_of_interest poi where park_id=? " +
                    "AND p.latitude = poi.latitude AND p.longitude = poi.longitude")).thenReturn(stmFetchParkID);
            when(dataHandler.executeQuery(stmFetchParkID)).thenReturn(rsFetchParkID);
            when(rsFetchParkID.next()).thenReturn(true).thenReturn(true);
            when(rsFetchParkID.getDouble("latitude")).thenReturn(41.140630);
            when(rsFetchParkID.getDouble("longitude")).thenReturn(-8.611180);
            when(rsFetchParkID.getInt("altitude_m")).thenReturn(25);
            when(rsFetchParkID.getFloat("park_input_voltage")).thenReturn(220f);
            when(rsFetchParkID.getFloat("park_input_current")).thenReturn(16f);
            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?")).thenReturn(stmFetchParkCapacities);
            when(dataHandler.executeQuery(stmFetchParkCapacities)).thenReturn(rsFetchParkCapacities);
            when(rsFetchParkCapacities.getString("vehicle_type_name")).thenReturn("electric_scooter");
            when(rsFetchParkCapacities.getInt("park_capacity")).thenReturn(10);
            when(rsFetchParkCapacities.getInt("amount_occupied")).thenReturn(2);
            when(rsFetchParkID.getString("poi_description")).thenReturn("Ribeira");

            PreparedStatement stmFetchParkCoords = mock(PreparedStatement.class);
            ResultSet rsFetchParkCoords = mock(ResultSet.class);

            when(dataHandler.prepareStatement("select * from PARKS p, points_of_interest poi where p.LATITUDE = ? AND p.LONGITUDE = ? " +
                    "AND p.latitude = poi.latitude AND p.longitude = poi.longitude")).thenReturn(stmFetchParkCoords);
            when(dataHandler.executeQuery(stmFetchParkCoords)).thenReturn(rsFetchParkCoords);
            when(rsFetchParkCoords.next()).thenReturn(true).thenReturn(true);
            when(rsFetchParkCoords.getString("park_id")).thenReturn("CasteloQueijo");
            when(rsFetchParkCoords.getFloat("park_input_voltage")).thenReturn(220f);
            when(rsFetchParkCoords.getFloat("park_input_current")).thenReturn(16f);
            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?")).thenReturn(stmFetchParkCapacities);
            when(dataHandler.executeQuery(stmFetchParkCapacities)).thenReturn(rsFetchParkCapacities);
            when(rsFetchParkCapacities.getString("vehicle_type_name")).thenReturn("electric_scooter");
            when(rsFetchParkCapacities.getInt("park_capacity")).thenReturn(10);
            when(rsFetchParkCapacities.getInt("amount_occupied")).thenReturn(0);
            when(rsFetchParkID.getString("poi_description")).thenReturn("CasteloQueijo");
            when(rsFetchParkCoords.getString("poi_description")).thenReturn("Castelo do Queijo");
            when(rsFetchParkCoords.getInt("altitude_m")).thenReturn(4);

            //This one won't find any electric scooters
            Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
            controller.startTripParkDest("Ribeira", null,41.168750, -8.689950, OUTPUT_FILE_PATH);
            try(Scanner scanner = new Scanner(new FileReader(OUTPUT_FILE_PATH))) {
                if(scanner.hasNext() && scanner.nextLine().equalsIgnoreCase("Could not find any scooter with enough energy in the park.")) {
                    assertTrue(true, "Found the correct null message");
                } else {
                    fail("Should have found a \"Did not find available scooter\" but didn't, check if the message has not been changed");
                }
            } catch (IOException e) {
                fail("IOException on no scooters at park");
            }

            when(rsFetchUser.getString( "user_password")).thenReturn("333");
            when(rsFetchUser.getString("user_email")).thenReturn("c@c.c");
            when(dataHandler.prepareStatement("SELECT * FROM clients where USER_EMAIL like ?")).thenReturn(stmFetchClient);
            when(dataHandler.executeQuery(stmFetchClient)).thenReturn(rsFetchClient);
            when(rsFetchClient.next()).thenReturn(true);
            when(rsFetchClient.getInt("points")).thenReturn(300);
            when(rsFetchClient.getString("visa")).thenReturn("3333333333333333");
            when(rsFetchClient.getInt( "height_cm")).thenReturn(2);
            when(rsFetchClient.getInt( "weight_kg")).thenReturn(120);
            when(rsFetchClient.getString( "gender")).thenReturn("M");
            when(rsFetchClient.getFloat( "cycling_average_speed")).thenReturn(14.3f);
            when(rsFetchClient.getBoolean("is_riding")).thenReturn(false);
            when(dataHandler.prepareStatement("Select * from parks p, points_of_interest poi where park_id=? " +
                    "AND p.latitude = poi.latitude AND p.longitude = poi.longitude")).thenReturn(stmFetchParkID);
            when(dataHandler.executeQuery(stmFetchParkID)).thenReturn(rsFetchParkID);
            when(rsFetchParkID.next()).thenReturn(true).thenReturn(true);
            when(rsFetchParkID.getDouble("latitude")).thenReturn(41.140630);
            when(rsFetchParkID.getDouble("longitude")).thenReturn(-8.611180);
            when(rsFetchParkID.getInt("altitude_m")).thenReturn(25);
            when(rsFetchParkID.getFloat("park_input_voltage")).thenReturn(220f);
            when(rsFetchParkID.getFloat("park_input_current")).thenReturn(16f);
            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?")).thenReturn(stmFetchParkCapacities);
            when(dataHandler.executeQuery(stmFetchParkCapacities)).thenReturn(rsFetchParkCapacities);
            when(rsFetchParkCapacities.getString("vehicle_type_name")).thenReturn("electric_scooter");
            when(rsFetchParkCapacities.getInt("park_capacity")).thenReturn(10);
            when(rsFetchParkCapacities.getInt("amount_occupied")).thenReturn(5);
            when(rsFetchParkID.getString("poi_description")).thenReturn("Ribeira");
            when(dataHandler.prepareStatement("select * from PARKS p, points_of_interest poi where p.LATITUDE = ? AND p.LONGITUDE = ? " +
                    "AND p.latitude = poi.latitude AND p.longitude = poi.longitude")).thenReturn(stmFetchParkCoords);
            when(dataHandler.executeQuery(stmFetchParkCoords)).thenReturn(rsFetchParkCoords);

            PreparedStatement stmFetchVehiclePark = mock(PreparedStatement.class);
            ResultSet rsFetchVehiclePark = mock(ResultSet.class);

            when(dataHandler.prepareStatement("Select * from PARK_VEHICLE where PARK_ID = ?")).thenReturn(stmFetchVehiclePark);
            when(dataHandler.executeQuery(stmFetchVehiclePark)).thenReturn(rsFetchVehiclePark);
            when(rsFetchVehiclePark.next()).thenReturn(true).thenReturn(true).thenReturn(false);

            PreparedStatement stmFetchVehicle = mock(PreparedStatement.class);
            ResultSet rsFetchVehicle = mock(ResultSet.class);
            PreparedStatement stmFetchScooter = mock(PreparedStatement.class);
            ResultSet rsFetchScooter = mock(ResultSet.class);

            when(dataHandler.prepareStatement("select * from vehicles where description like ?")).thenReturn(stmFetchVehicle);
            when(dataHandler.executeQuery(stmFetchVehicle)).thenReturn(rsFetchVehicle);
            when(rsFetchVehicle.next()).thenReturn(true).thenReturn(true);
            when(rsFetchVehicle.getString(VEHICLE_TYPE_FIELD_NAME)).thenReturn("electric_scooter");
            when(dataHandler.prepareStatement("select * from " + "electric_scooters" + " where vehicle_description like ?")).thenReturn(stmFetchScooter);
            when(rsFetchScooter.next()).thenReturn(true).thenReturn(true);
            when(dataHandler.executeQuery(stmFetchScooter)).thenReturn(rsFetchScooter);

            when(rsFetchVehicle.getInt(UNIQUE_NUMBER_FIELD_NAME)).thenReturn(1);
            when(rsFetchVehicle.getString(DESCRIPTION_FIELD_NAME)).thenReturn("PT001").thenReturn("PT002");
            when(rsFetchVehicle.getFloat(AERO_COEFFICIENT_FIELD_NAME)).thenReturn(1f);
            when(rsFetchVehicle.getFloat(FRONTAL_AREA_FIELD_NAME)).thenReturn(1f);
            when(rsFetchVehicle.getInt(WEIGHT_FIELD_NAME)).thenReturn(10);
            when(rsFetchVehicle.getBoolean(AVAILABLE_FIELD_NAME)).thenReturn(true);
            when(rsFetchScooter.getString(ESCOOTER_TYPE_FIELD_NAME)).thenReturn("urban");
            when(rsFetchScooter.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(75);
            when(rsFetchScooter.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(1f);
            when(rsFetchScooter.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME)).thenReturn(200);

            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchPOI.getInt("altitude_m")).thenReturn(25).thenReturn(4);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(-8.61180).thenReturn(-8.689950);
            when(rsFetchPOI.getString("poi_description")).thenReturn("Ribeira").thenReturn("Castelo do Queijo");
            when(rsFetchPOI.getDouble("latitude")).thenReturn(41.140630).thenReturn(41.168750);

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);
            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(-8.61180);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(-41.140630);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(-41.168750);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(-8.689950);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6);

            PreparedStatement stmFetchCertainPOI = mock(PreparedStatement.class);
            ResultSet rsFetchCertainPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchCertainPOI);
            when(dataHandler.executeQuery(stmFetchCertainPOI)).thenReturn(rsFetchCertainPOI);
            when(rsFetchCertainPOI.next()).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchCertainPOI.getInt("altitude_m")).thenReturn(25).thenReturn(4);
            when(rsFetchCertainPOI.getString("poi_description")).thenReturn("Ribeira").thenReturn("Castelo do Queijo");

            PreparedStatement stmVehiclePark = mock(PreparedStatement.class);
            ResultSet rsVehiclePark = mock(ResultSet.class);

            when(dataHandler.prepareStatement("select PARK_ID from PARK_VEHICLE where VEHICLE_DESCRIPTION = ?")).thenReturn(stmVehiclePark);
            when(dataHandler.executeQuery(stmVehiclePark)).thenReturn(rsVehiclePark);
            when(rsVehiclePark.next()).thenReturn(true);
            when(rsVehiclePark.getString("park_id")).thenReturn("Ribeira");

            PreparedStatement stm3 = mock(PreparedStatement.class);
            ResultSet rs3 = mock(ResultSet.class);
            PreparedStatement stm4 = mock(PreparedStatement.class);
            ResultSet rs4 = mock(ResultSet.class);
            PreparedStatement stm5 = mock(PreparedStatement.class);
            ResultSet rs5 = mock(ResultSet.class);
            PreparedStatement stm6 = mock(PreparedStatement.class);
            PreparedStatement stm7 = mock(PreparedStatement.class);

            when(dataHandler.prepareStatement("select PARK_ID from PARK_VEHICLE where VEHICLE_DESCRIPTION = ?")).thenReturn(stm3);
            when(dataHandler.executeQuery(stm3)).thenReturn(rs3);
            when(rs3.next()).thenReturn(true);
            when(rs3.getString("park_id")).thenReturn("Trindade");
            when(dataHandler.prepareStatement("SELECT * FROM registered_users where USER_NAME = ?")).thenReturn(stm4);
            when(dataHandler.executeQuery(stm4)).thenReturn(rs4);
            when(rs4.next()).thenReturn(true);
            when(rs4.getString( "user_password")).thenReturn("333");
            when(rs4.getString("user_email")).thenReturn("c@c.c");
            when(dataHandler.prepareStatement("SELECT * FROM clients where USER_EMAIL like ?")).thenReturn(stm5);
            when(dataHandler.executeQuery(stm5)).thenReturn(rs5);
            when(rs5.next()).thenReturn(true);
            when(rs5.getInt("points")).thenReturn(300);
            when(rs5.getString("visa")).thenReturn("3333333333333333");
            when(rs5.getInt( "height_cm")).thenReturn(2);
            when(rs5.getInt( "weight_kg")).thenReturn(120);
            when(rs5.getString( "gender")).thenReturn("M");
            when(rs5.getFloat( "cycling_average_speed")).thenReturn(14.3f);
            when(rs5.getBoolean("is_riding")).thenReturn(false);

            when(dataHandler.prepareStatement("DELETE FROM PARK_VEHICLE WHERE VEHICLE_DESCRIPTION = ?")).thenReturn(stm5);
            when(dataHandler.executeUpdate(stm5)).thenReturn(1);
            when(dataHandler.prepareStatement("INSERT INTO TRIPS(start_time, user_email, vehicle_description, start_park_id)" +
                    "VALUES(current_timestamp, ?, ?, ?)")).thenReturn(stm6);
            when(dataHandler.executeUpdate(stm6)).thenReturn(1);
            when(dataHandler.prepareStatement("update clients set is_riding = ? where user_email = ?")).thenReturn(stm7);

            Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
            controller.startTripParkDest("Ribeira", "ivo1",41.168750, -8.689950, OUTPUT_FILE_PATH);
            verify(dataHandler, atLeastOnce()).commitTransaction();

            try(Scanner scanner = new Scanner(new FileReader(OUTPUT_FILE_PATH))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("escooter description;type;actual battery capacity")) {
                    if(scanner.hasNextLine() && scanner.nextLine().equalsIgnoreCase("PT001;city;75")) {
                        assertTrue(true, "Correctly found the scooter in the file");
                        Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
                    } else {
                        fail("The scooter was not correctly exported. Check if the variables match");
                    }
                } else {
                    fail("The header portion of the file was not exported correctly. Check if the headers match");
                }
            } catch (IOException e) {
                fail("IOException thrown when searching for file with a scooter");
            }
        } catch(SQLException e) {
            fail("SQLexception was thrown");
        } catch(IOException e) {
            fail("IOexception was thrown");
        }
    }
}
