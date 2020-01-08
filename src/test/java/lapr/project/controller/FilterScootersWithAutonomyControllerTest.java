package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.Path;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilterScootersWithAutonomyControllerTest {
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
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static FilterScootersWithAutonomyController controller;

    @BeforeAll
    static void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new FilterScootersWithAutonomyController();
    }

    @Test
    void filterScootersWithAutonomy() {
        Path path1 = new Path(new PointOfInterest("desc1", new Coordinates(0.0, 0.0, 0)), new PointOfInterest("desc2", new Coordinates(0.0001, 0.0001, 1)), 0.002, 1, 0.3); // 1
        Path path2 = new Path(new PointOfInterest("desc1", new Coordinates(0.0001, 0.0001, 1)), new PointOfInterest("desc2", new Coordinates(0.0002, 0.0002, 2)), 0.002, 1, 0.3); // 1
        Path path3 = new Path(new PointOfInterest("desc1", new Coordinates(0.0002, 0.0002, 2)), new PointOfInterest("desc2", new Coordinates(0.0003, 0.0003, 3)), 0.002, 1, 0.3); // 1
        List<Path> trip = new ArrayList<>();
        trip.add(path1);
        trip.add(path2);
        trip.add(path3);

        ElectricScooter instance = new ElectricScooter(123, "PT001", 2.3F, 2.4F,
                35, true, ElectricScooterType.URBAN, 15,
                1f, 500);

        ElectricScooter instance1 = new ElectricScooter(456, "PT002", 2.3F, 2.4F,
                33, true, ElectricScooterType.URBAN, 15,
                1f, 500);

        List<ElectricScooter> listElectricScooters = new ArrayList<>();
        listElectricScooters.add(instance);
        listElectricScooters.add(instance1);

        List<ElectricScooter> result = controller.filterScootersWithAutonomy(listElectricScooters, trip);
        assertEquals(listElectricScooters, result);
    }

    @Test
    void testSuggestScootersBetweenParks() {
        final String OUTPUT_FILE_PATH = "testFiles/temp/SuggestScootersBetweenParks.output";
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
            assertEquals(0, controller.suggestScootersBetweenParks("Ribeira", null,41.168750, -8.689950, OUTPUT_FILE_PATH));
            try(Scanner scanner = new Scanner(new FileReader(OUTPUT_FILE_PATH))) {
                if(scanner.hasNext() && scanner.nextLine().equalsIgnoreCase("Couldn't find scooters to suggest between the two parks.")) {
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
            when(rsFetchScooter.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(0).thenReturn(75);
            when(rsFetchScooter.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(1f);
            when(rsFetchScooter.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME)).thenReturn(200);

            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchPOI.getInt("altitude_m")).thenReturn(25).thenReturn(4).thenReturn(25).thenReturn(4);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(-8.61180).thenReturn(-8.689950).thenReturn(-8.61180).thenReturn(-8.689950);
            when(rsFetchPOI.getString("poi_description")).thenReturn("Ribeira").thenReturn("Castelo do Queijo");
            when(rsFetchPOI.getDouble("latitude")).thenReturn(41.140630).thenReturn(41.168750);

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);
            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(-8.61180).thenReturn(-8.61180);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(-41.140630).thenReturn(-41.140630);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(-41.168750).thenReturn(-41.168750);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(-8.689950).thenReturn(-8.689950);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6).thenReturn(15.6);

            PreparedStatement stmFetchCertainPOI = mock(PreparedStatement.class);
            ResultSet rsFetchCertainPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchCertainPOI);
            when(dataHandler.executeQuery(stmFetchCertainPOI)).thenReturn(rsFetchCertainPOI);
            when(rsFetchCertainPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchCertainPOI.getInt("altitude_m")).thenReturn(25).thenReturn(4).thenReturn(25).thenReturn(4);
            when(rsFetchCertainPOI.getString("poi_description")).thenReturn("Ribeira").thenReturn("Castelo do Queijo")
                    .thenReturn("Ribeira").thenReturn("Castelo do Queijo");

            PreparedStatement stmVehiclePark = mock(PreparedStatement.class);
            ResultSet rsVehiclePark = mock(ResultSet.class);

            when(dataHandler.prepareStatement("select PARK_ID from PARK_VEHICLE where VEHICLE_DESCRIPTION = ?")).thenReturn(stmVehiclePark);
            when(dataHandler.executeQuery(stmVehiclePark)).thenReturn(rsVehiclePark);
            when(rsVehiclePark.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
            when(rsVehiclePark.getString("park_id")).thenReturn("Ribeira").thenReturn("Ribeira");

            Utils.writeToFile(new ArrayList<>(), OUTPUT_FILE_PATH);
            assertEquals(1, controller.suggestScootersBetweenParks("Ribeira", "ivo1",41.168750
                    , -8.689950, OUTPUT_FILE_PATH));
            try(Scanner scanner = new Scanner(new FileReader(OUTPUT_FILE_PATH))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("escooter description;type;actual battery capacity")) {
                    if(scanner.hasNextLine() && scanner.nextLine().equalsIgnoreCase("PT002;city;75")) {
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
            fail("SQLException thrown");
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}