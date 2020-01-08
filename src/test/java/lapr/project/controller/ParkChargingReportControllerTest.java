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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParkChargingReportControllerTest {
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

    private DataHandler dataHandler;
    private ParkChargingReportController controller;

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        Company.createCompany(dataHandler);
        controller = new ParkChargingReportController();
    }

    @Test
    void retrieveParkChargingReport() {
        try {
            PreparedStatement stmFetchVehiclePark = mock(PreparedStatement.class);
            ResultSet rsFetchVehiclePark = mock(ResultSet.class);

            when(dataHandler.prepareStatement("Select * from PARK_VEHICLE where PARK_ID = ?")).thenReturn(stmFetchVehiclePark);
            when(dataHandler.executeQuery(stmFetchVehiclePark)).thenReturn(rsFetchVehiclePark);
            when(rsFetchVehiclePark.next()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);

            PreparedStatement stmFetchVehicle = mock(PreparedStatement.class);
            ResultSet rsFetchVehicle = mock(ResultSet.class);
            PreparedStatement stmFetchScooter = mock(PreparedStatement.class);
            ResultSet rsFetchScooter = mock(ResultSet.class);

            when(dataHandler.prepareStatement("select * from vehicles where description like ?")).thenReturn(stmFetchVehicle);
            when(dataHandler.executeQuery(stmFetchVehicle)).thenReturn(rsFetchVehicle);
            when(rsFetchVehicle.next()).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchVehicle.getString(VEHICLE_TYPE_FIELD_NAME)).thenReturn("electric_scooter");
            when(dataHandler.prepareStatement("select * from " + "electric_scooters" + " where vehicle_description like ?")).thenReturn(stmFetchScooter);
            when(rsFetchScooter.next()).thenReturn(true).thenReturn(true).thenReturn(true);
            when(dataHandler.executeQuery(stmFetchScooter)).thenReturn(rsFetchScooter);

            when(rsFetchVehicle.getInt(UNIQUE_NUMBER_FIELD_NAME)).thenReturn(1);
            when(rsFetchVehicle.getString(DESCRIPTION_FIELD_NAME)).thenReturn("PT001").thenReturn("PT004").thenReturn("PT003").thenReturn("PT002");
            when(rsFetchVehicle.getFloat(AERO_COEFFICIENT_FIELD_NAME)).thenReturn(1f);
            when(rsFetchVehicle.getFloat(FRONTAL_AREA_FIELD_NAME)).thenReturn(1f);
            when(rsFetchVehicle.getInt(WEIGHT_FIELD_NAME)).thenReturn(10);
            when(rsFetchVehicle.getBoolean(AVAILABLE_FIELD_NAME)).thenReturn(true);
            when(rsFetchScooter.getString(ESCOOTER_TYPE_FIELD_NAME)).thenReturn("urban");
            when(rsFetchScooter.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(75).thenReturn(25).thenReturn(100).thenReturn(25);
            when(rsFetchScooter.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME)).thenReturn(1f);
            when(rsFetchScooter.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME)).thenReturn(200);

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
            when(rsFetchParkID.getString("poi_description")).thenReturn("Trindade");

            Utils.writeToFile(new ArrayList<>(), "testFiles/outputParkChargingReport.output");
            assertEquals(0,controller.retrieveParkChargingReport("Trindade", "testFiles/outputParkChargingReport.output"));
            try(Scanner scanner = new Scanner(new FileReader("testFiles/outputParkChargingReport.output"))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("Could not find any scooter.")) {
                    assertTrue(true, "Passed");
                } else {
                    fail("Fail message not found");
                }
            } catch (IOException e) {
                fail("IOException thrown");
            }
            assertEquals(3, controller.retrieveParkChargingReport("Trindade", "testFiles/outputParkChargingReport.output"));
            try(Scanner scanner = new Scanner(new FileReader("testFiles/outputParkChargingReport.output"))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("escooter description;actual battery capacity;time to finish charge in seconds")) {
                    if(scanner.hasNextLine() && scanner.nextLine().equals("PT002;25;767")) {
                        if(scanner.hasNextLine() && scanner.nextLine().equals("PT004;25;767")) {
                            if(scanner.hasNextLine() && scanner.nextLine().equals("PT001;75;256")) {
                                if(scanner.hasNextLine() && scanner.nextLine().equals("PT003;100;0")) {
                                    assertTrue(true, "All the scooters were correct");
                                } else {
                                    fail("Last scooter did not have correct values");
                                }
                            } else {
                                fail("Third scooter was wrong");
                            }
                        } else {
                            fail("Secnd scooter was wrong");
                        }
                    } else {
                        fail("First scooter was wrong");
                    }
                } else {
                    fail("Fail message not found");
                }
            } catch (IOException e) {
                fail("IOException thrown");
            }
        } catch (SQLException e) {
            fail("SQLException was thrown");
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }
}