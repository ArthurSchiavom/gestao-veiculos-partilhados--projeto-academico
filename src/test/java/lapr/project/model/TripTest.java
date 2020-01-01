/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jose
 */
public class TripTest {

    /**
     * Test of getStartTime method, of class Trip.
     */
    @Test
    public void testGetStartTime() {
        System.out.println("getStartTime");

        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime endTime = LocalDateTime.of(2019,10,9,12,10);

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        LocalDateTime expResult = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime result = instance.getStartTime();
        assertEquals(expResult, result);

        instance = new Trip(startTime,clientEmail,startParkId,endParkId,vehicleId);
        result = instance.getStartTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndTime method, of class Trip.
     */
    @Test
    public void testGetEndTime() {
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime endTime = LocalDateTime.of(2019,10,9,12,10);

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, endTime, clientEmail, startParkId, endParkId, vehicleId);

        LocalDateTime expResult = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime result = instance.getEndTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndTime method, of class Trip.
     */
    @Test
    public void testGetEndTime2() {
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);

        String clientEmail = "email@email.com";
        String startParkId = "1";
        String endParkId = "2";
        String vehicleId = "1";
        Trip instance = new Trip(startTime, clientEmail, startParkId, vehicleId);

        LocalDateTime result = instance.getEndTime();
        assertNull(result);

    }

    /**
     * Test of getClient method, of class Trip.
     */
    @Test
    public void testGetClientId() {
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime endTime = LocalDateTime.of(2019,10,9,12,10);

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
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime endTime = LocalDateTime.of(2019,10,9,12,10);

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
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime endTime = LocalDateTime.of(2019,10,9,12,10);

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
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);

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
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);
        LocalDateTime endTime = LocalDateTime.of(2019,10,9,12,10);

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
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);

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
        LocalDateTime startTime = LocalDateTime.of(2019,10,9,12,10);

        Trip trip1 = new Trip(startTime, "email@email.com", "0","1");
        Trip trip2 = new Trip(startTime, "email@email.com","0","1");
        int expResult = trip1.hashCode();
        assertEquals(expResult, trip2.hashCode());

        trip1 = new Trip(startTime, "email2@email.com", "0","1");
        assertNotEquals(expResult, trip1.hashCode());
    }

    @Test
    void getListOfVehiclesNotAvailable(){
        Path path1 = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(0.0001, 0.0001, 1), 0.002, 1, 0.3); // 1
        Path path2 = new Path(new Coordinates(0.0001, 0.0001, 1), new Coordinates(0.0002, 0.0002, 2), 0.002, 1, 0.3); // 1
        Path path3 = new Path(new Coordinates(0.0002, 0.0002, 2), new Coordinates(0.0003, 0.0003, 3), 0.002, 1, 0.3); // 1
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
}
