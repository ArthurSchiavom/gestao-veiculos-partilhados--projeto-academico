/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 *
 */
public class CaloriesBurntBetweenTwoParksControllerTest {

    private DataHandler dataHandler;
    private Company company;
    private CaloriesBurntBetweenTwoParksController controller;
    private PreparedStatement stm1 = mock(PreparedStatement.class);
    private PreparedStatement stm2 = mock(PreparedStatement.class);
    private PreparedStatement stm3 = mock(PreparedStatement.class);
    private PreparedStatement stm4 = mock(PreparedStatement.class);
    private PreparedStatement stm5 = mock(PreparedStatement.class);
    private PreparedStatement stm6 = mock(PreparedStatement.class);
    private PreparedStatement stm7 = mock(PreparedStatement.class);
    private PreparedStatement stm8 = mock(PreparedStatement.class);
    private PreparedStatement stm9 = mock(PreparedStatement.class);
    private PreparedStatement stm10 = mock(PreparedStatement.class);
    private ResultSet rs1 = mock(ResultSet.class);
    private ResultSet rs2 = mock(ResultSet.class);
    private ResultSet rs3 = mock(ResultSet.class);
    private ResultSet rs4 = mock(ResultSet.class);
    private ResultSet rs5 = mock(ResultSet.class);
    private ResultSet rs6 = mock(ResultSet.class);
    private ResultSet rs7 = mock(ResultSet.class);
    private ResultSet rs8 = mock(ResultSet.class);
    private ResultSet rs9 = mock(ResultSet.class);
    private ResultSet rs10 = mock(ResultSet.class);

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new CaloriesBurntBetweenTwoParksController(company);

    }

    private void prepareGets(String username, String vehicleDescription, String startParkId, String endParkId, double latA, double lonA, double latB, double lonB) {
        reset(stm1);
        reset(stm2);
        reset(stm3);
        reset(stm4);
        reset(stm5);
        reset(stm6);
        reset(stm7);
        reset(stm8);
        reset(stm9);
        reset(stm10);
        reset(rs1);
        reset(rs2);
        reset(rs3);
        reset(rs4);
        reset(rs5);
        reset(rs6);
        reset(rs7);
        reset(rs8);
        reset(rs9);
        reset(rs10);
        try {
            when(dataHandler.prepareStatement(anyString())).thenReturn(stm7);

            when(dataHandler.prepareStatement("SELECT * FROM registered_users where USER_NAME = ?")).thenReturn(stm1);
            when(dataHandler.executeQuery(stm1)).thenReturn(rs1);
            when(dataHandler.prepareStatement("SELECT * FROM clients where USER_EMAIL like ?")).thenReturn(stm2);
            when(dataHandler.executeQuery(stm2)).thenReturn(rs2);
            when(dataHandler.prepareStatement("select * from vehicles where description like ?")).thenReturn(stm3);
            when(dataHandler.executeQuery(stm3)).thenReturn(rs3);
            when(dataHandler.prepareStatement("select * from " + "bicycles" + " where vehicle_description like ?")).thenReturn(stm4);
            when(dataHandler.executeQuery(stm4)).thenReturn(rs4);
            when(dataHandler.prepareStatement("Select * from parks p, points_of_interest poi where park_id=? AND p.latitude = poi.latitude AND p.longitude = poi.longitude")).thenReturn(stm5);
            when(dataHandler.executeQuery(stm5)).thenReturn(rs5);
            when(dataHandler.prepareStatement("Select * from park_capacity where park_id=?")).thenReturn(stm6);
            when(dataHandler.executeQuery(stm6)).thenReturn(rs6);
            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?")).thenReturn(stm7);
            when(dataHandler.executeQuery(stm7)).thenReturn(rs7);
            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stm8);
            when(dataHandler.executeQuery(stm8)).thenReturn(rs8);
            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stm9);
            when(dataHandler.executeQuery(stm9)).thenReturn(rs9);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stm10);
            when(rs1.getString("username")).thenReturn(username);

            when(rs3.getString("description")).thenReturn(vehicleDescription);

            when(rs4.getString("vehicle_description")).thenReturn(vehicleDescription);

            when(rs5.getString("park_id")).thenReturn(startParkId).thenReturn(endParkId);

            when(rs1.next()).thenReturn(true).thenReturn(false);
            when(rs2.next()).thenReturn(true).thenReturn(false);
            when(rs3.next()).thenReturn(true).thenReturn(false);
            when(rs4.next()).thenReturn(true).thenReturn(false);
            when(rs5.next()).thenReturn(true).thenReturn(true).thenReturn(false);
//            when(rs7.next()).thenReturn(true).thenReturn(true).thenReturn(false);
//            when(rs8.next()).thenReturn(true).thenReturn(false);
//            when(rs9.next()).thenReturn(true).thenReturn(true).thenReturn(false);

            when(rs1.getString("user_password")).thenReturn("a");
            when(rs1.getString("user_email")).thenReturn("a@a.a");

            when(rs2.getInt("points")).thenReturn(10);
            when(rs2.getString("visa")).thenReturn("1111111111111111");
            when(rs2.getInt("height_cm")).thenReturn(170);
            when(rs2.getInt("weight_kg")).thenReturn(75);
            when(rs2.getString("gender")).thenReturn("M");
            when(rs2.getFloat("cycling_average_speed")).thenReturn(3.0f);
            when(rs2.getBoolean("is_riding")).thenReturn(false);

            when(rs3.getString(anyString())).thenReturn("bicycle");
            when(rs3.getInt("unique_number")).thenReturn(1);
            when(rs3.getFloat("aerodynamic_coefficient")).thenReturn(1.10f);
            when(rs3.getFloat("frontal_area")).thenReturn(0.3f);
            when(rs3.getInt("weight")).thenReturn(20);
            when(rs3.getBoolean("available")).thenReturn(true);
            when(rs3.getString("vehicle_type_name")).thenReturn("bicycle");

            when(rs4.getInt("bicycle_size")).thenReturn(15);

            when(rs5.getString("park_id")).thenReturn("1").thenReturn("2");
            when(rs5.getDouble("latitude")).thenReturn(0.0).thenReturn(0.0018);
            when(rs5.getDouble("longitude")).thenReturn(0.0).thenReturn(0.002);
            when(rs5.getInt("altitude_m")).thenReturn(100).thenReturn(300);
            when(rs5.getFloat("park_input_voltage")).thenReturn(200f).thenReturn(200f);
            when(rs5.getFloat("park_input_current")).thenReturn(16f).thenReturn(17f);
            when(rs5.getString("poi_description")).thenReturn("poiDesc1").thenReturn("poiDesc2");

            when(rs6.getString("vehicle_type_name")).thenReturn("n");
            when(rs6.getInt("park_capacity")).thenReturn(2);
            when(rs6.getInt("amount_occupied")).thenReturn(2);

            when(rs7.getDouble("latitude")).thenReturn(latA).thenReturn(latB);
            when(rs7.getDouble("longitude")).thenReturn(lonA).thenReturn(lonB);
            when(rs7.getInt("altitude_m")).thenReturn(100).thenReturn(300);
            when(rs7.getString("poi_description")).thenReturn("poiDesc1").thenReturn("poiDesc2");

            when(rs8.getDouble("latitudeA")).thenReturn(latA);
            when(rs8.getDouble("longitudeA")).thenReturn(lonA);
            when(rs8.getDouble("latitudeB")).thenReturn(latB);
            when(rs8.getDouble("longitudeB")).thenReturn(lonB);
            when(rs8.getDouble("kinetic_coefficient")).thenReturn(0.002);
            when(rs8.getInt("wind_direction_degrees")).thenReturn(90);
            when(rs8.getDouble("wind_speed")).thenReturn(1.0);
            when(rs8.getString("poi_description")).thenReturn("poiDesc1").thenReturn("poiDesc2");
            when(rs8.getInt("altitude_m")).thenReturn(100).thenReturn(300);

            when(rs9.getDouble("latitude")).thenReturn(latA).thenReturn(latB);
            when(rs9.getDouble("longitude")).thenReturn(lonA).thenReturn(lonB);
            when(rs9.getInt("altitude_m")).thenReturn(100).thenReturn(300);
            when(rs9.getString("poi_description")).thenReturn("poiDesc1").thenReturn("poiDesc2");

            when(rs10.getInt("altitude_m")).thenReturn(100).thenReturn(300);
            when(rs10.getString("poi_description")).thenReturn("poiDesc1").thenReturn("poiDesc2");

        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

    }

    private void verifyGets(String username, String vehicleDescription) {
        try {
            verify(stm1).setString(1, username);
            verify(stm2).setString(1, rs1.getString("user_email"));
            verify(stm3).setString(1, vehicleDescription);
            verify(stm4).setString(1, vehicleDescription);
            verify(stm1).close();
            verify(stm2).close();
            verify(stm3).close();
            verify(stm4).close();
            verify(rs1).close();
            verify(rs2).close();
            verify(rs3).close();
            verify(rs4).close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of predictCaloriesBurnt method, of class
     * CaloriesBurntBetweenTwoParksController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testPredictCaloriesBurnt() throws Exception {

        String username = "1";
        String vehicleDescription = "PT50";
        String startParkId = "1";
        String endParkId = "2";
        double latA = 0.0;
        double lonA = 0.0;
        double latB = 0.0018;
        double lonB = 0.002;
        String filePath = "testFiles/temp/CaloriesBurntBetweenTwoParksTest.writeOutputFileTest.output";
        prepareGets(username, vehicleDescription, startParkId, endParkId, latA, lonA, latB, lonB);
        try {
            double energy = controller.predictCaloriesBurnt(username, vehicleDescription, startParkId, endParkId, latA, lonA, latB, lonB, filePath);
            verifyGets(username, vehicleDescription);
            assertEquals(0.0, energy);
//            assertEquals(75, client.getWeight());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

    }

}
//60064.5787
