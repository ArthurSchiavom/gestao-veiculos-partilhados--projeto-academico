/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class
 */
public class TripTest {

    /**
     * Test of getStartTime method, of class Trip.
     */
    @Test
    public void testGetStartTime() {
        System.out.println("getStartTime");

        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        Timestamp expResult = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp result = instance.getStartTime();
        assertEquals(expResult, result);

        instance = new Trip(startTime,clientEmail,startParkId,vehicleId);
        result = instance.getStartTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndTime method, of class Trip.
     */
    @Test
    public void testGetEndTime() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        Timestamp expResult = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp result = instance.getEndTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndTime method, of class Trip.
     */
    @Test
    public void testGetEndTime2() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, clientEmail, startParkId, vehicleId);

        Timestamp result = instance.getEndTime();
        assertNull(result);

    }

    /**
     * Test of getClient method, of class Trip.
     */
    @Test
    public void testGetClientId() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        String expResult = "email@email.com";

        String result = instance.getClientEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartPark method, of class Trip.
     */
    @Test
    public void testGetStartParkId() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        String expResult = "1";

        String result = instance.getStartParkId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndPark method, of class Trip.
     */
    @Test
    public void testGetEndParkId() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        String expResult = "2";

        String result = instance.getEndParkId();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetEndParkId2() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, clientEmail, startParkId, vehicleId);

        assertNull(instance.getEndParkId());
    }

    /**
     * Test of getVehicle method, of class Trip.
     */
    @Test
    public void testGetVehicleDescription() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        String expResult = "1";

        String result = instance.getVehicleDescription();
        assertEquals(expResult, result);
    }

    @Test
    void testEquals() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        Object trip1 = new Trip(startTime, "email@email.com", "0","1");
        assertEquals(trip1, trip1);

        Trip trip2 = new Trip(startTime, "email@email.com", "0","1");
        assertEquals(trip1, trip2);

        trip1 = new Trip(startTime, "email2@email.com", "0","1");
        assertNotEquals(trip1, trip2);

        trip1 = null;
        assertNotEquals(trip2, trip1);
    }

    @Test
    void testHashCode() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        Trip trip1 = new Trip(startTime, "email@email.com", "0","1");
        Trip trip2 = new Trip(startTime, "email@email.com","0","1");
        int expResult = trip1.hashCode();
        assertEquals(expResult, trip2.hashCode());

        trip1 = new Trip(startTime, "email2@email.com", "0","1");
        assertNotEquals(expResult, trip1.hashCode());
    }

    @Test
    void getListOfVehiclesNotAvailable(){
        Path path1 = new Path(new PointOfInterest("desc1",new Coordinates(0.0, 0.0, 0)), new PointOfInterest("desc2",new Coordinates(0.0001, 0.0001, 1)), 0.002, 1, 0.3); // 1
        Path path2 = new Path(new PointOfInterest("desc1",new Coordinates(0.0001, 0.0001, 1)), new PointOfInterest("desc2",new Coordinates(0.0002, 0.0002, 2)), 0.002, 1, 0.3); // 1
        Path path3 = new Path(new PointOfInterest("desc1",new Coordinates(0.0002, 0.0002, 2)), new PointOfInterest("desc2",new Coordinates(0.0003, 0.0003, 3)), 0.002, 1, 0.3); // 1
        List<Path> trip = new ArrayList<>();
        trip.add(path1);
        trip.add(path2);
        trip.add(path3);

        ElectricScooter instance = new ElectricScooter(123, "PT001",2.3F,2.4F,
                35,true, ElectricScooterType.URBAN,15,
                1f, 500);

        ElectricScooter instance1 = new ElectricScooter(456, "PT002",2.3F,2.4F,
                33,true,ElectricScooterType.URBAN,15,
                1f, 500);

        List<ElectricScooter> listElectricScooters = new ArrayList<>();
        listElectricScooters.add(instance);
        listElectricScooters.add(instance1);

        List<ElectricScooter> expected = new ArrayList<>();
        expected.add(instance);
        expected.add(instance1);

        List <ElectricScooter> result = lapr.project.model.Trip.filterScootersWithAutonomy(listElectricScooters,trip);
        assertEquals(expected,result);
    }

    @Test
    public void calculateTripCostTest(){
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,14,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        assertEquals(1.5,instance.calculateTripCost());
    }

    @Test
    public void calculateTripCostTest2(){
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,13,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        assertEquals(0,instance.calculateTripCost());
    }

    @Test
    public void calculateTripCostTest3(){
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        assertEquals(0,instance.calculateTripCost());
    }

    @Test
    public void calculateTripCostTest4(){
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,17,10));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        assertEquals(6,instance.calculateTripCost());
    }

    @Test
    public void calculateTripCostTest5(){
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,17,40));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        assertEquals(6.75,instance.calculateTripCost());
    }

    @Test
    public void calculateTripCostTest6(){
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = null;

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        assertEquals(0,instance.calculateTripCost());
    }

    @Test
    public void calculateTripCostTest7(){
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,11,40));

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);
        assertEquals(0,instance.calculateTripCost());
    }

    @Test
    void getTripDurationMillisTest() {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,12,10));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2019,10,9,11,40));
        Trip trip = new Trip(startTime, endTime, "a", "a", "a", "a");
        assertEquals(endTime.getTime()-startTime.getTime(), trip.getTripDurationMillis());

        trip = new Trip(startTime, null, "a", "a", "a", "a");
        assertTrue(((Calendar.getInstance().getTimeInMillis() - startTime.getTime()) - trip.getTripDurationMillis()) < 200); //200 = 200 milisseconds error margin from when the method called it to when the test called it
    }
}
