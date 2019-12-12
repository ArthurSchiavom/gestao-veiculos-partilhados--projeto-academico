/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import lapr.project.model.Park.Capacity;
import lapr.project.model.Park.Park;
import lapr.project.model.Users.Client;
import lapr.project.model.Vehicles.Bicycle;
import lapr.project.model.Vehicles.Vehicle;
import lapr.project.model.Vehicles.VehicleType;
import org.junit.Before;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author Jose
 */
public class TripTest {
    
    public TripTest() {
    }

    @Before
    public void setUp() throws Exception {
    }
    


    /**
     * Test of getStartTime method, of class Trip.
     */
    @Test
    public void testGetStartTime() {
   System.out.println("getStartTime");

        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

        int clientId = 1;
        int startParkId = 1;
        int endParkId = 2;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, endTime, clientId, startParkId, endParkId, vehicleId);

        Calendar expResult = Calendar.getInstance();
        expResult.clear();
        expResult.set(2019, 10, 9, 12, 10);
        Calendar result = instance.getStartTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndTime method, of class Trip.
     */
    @Test
    public void testGetEndTime() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

        int clientId = 1;
        int startParkId = 1;
        int endParkId = 2;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, endTime, clientId, startParkId, endParkId, vehicleId);


        Calendar expResult = Calendar.getInstance();
        expResult.clear();
        expResult.set(2019, 10, 9, 12, 10);
        Calendar result = instance.getEndTime();
        assertEquals(expResult, result);
    }

       /**
     * Test of getEndTime method, of class Trip.
     */
    @Test
    public void testGetEndTime2() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

    

        int clientId = 1;
        int startParkId = 1;
        int endParkId = 2;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, clientId, startParkId, vehicleId);


    
        Calendar result = instance.getEndTime();
        assertNull(result);
     
    }

    /**
     * Test of getClient method, of class Trip.
     */
    @Test
    public void testGetClientId() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

          int clientId = 1;
        int startParkId = 1;
        int endParkId = 2;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, endTime, clientId, startParkId, endParkId, vehicleId);

        int expResult = 1;

        int result = instance.getClientId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartPark method, of class Trip.
     */
    @Test
    public void testGetStartParkId() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

         int clientId = 1;
        int startParkId = 1;
        int endParkId = 2;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, endTime, clientId, startParkId, endParkId, vehicleId);

        int expResult = 1;

        int result = instance.getStartParkId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndPark method, of class Trip.
     */
    @Test
    public void testGetEndParkId() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

          int clientId = 1;
        int startParkId = 1;
        int endParkId = 2;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, endTime, clientId, startParkId, endParkId, vehicleId);

        int expResult = 2;

        int result = instance.getEndParkId();
        assertEquals(expResult, result);
    }

     @Test
    public void testGetEndParkId2() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        int clientId = 1;
        int startParkId = 1;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, clientId, startParkId, vehicleId);

        int expResult = 0;

        Integer result = instance.getEndParkId();
        assertEquals(expResult, result);
    }
    /**
     * Test of getVehicle method, of class Trip.
     */
    @Test
    public void testGetVehicleId() {
          Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

        
        int clientId = 1;
        int startParkId = 1;
        int endParkId = 2;
        int vehicleId = 1;
        Trip instance = new Trip(startTime, endTime, clientId, startParkId, endParkId, vehicleId);

        int expResult = 1;

        int result = 1;
        assertEquals(expResult, result);
    }
    
    
}
