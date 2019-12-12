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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author Jose
 */
public class TripTest {
    
    public TripTest() {
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

        Client client = new Client("a@a.a", "aaa", 10, 111, 19, 1.78f, 75.0f, 'M', "1111111111111111", "10/12/2021");

        Coordinates coordenadas = new Coordinates(10.0, 20.0, 12.0);
        Set<Capacity> vehicleCapacities = new HashSet<>();
        Capacity bicycles = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities.add(scooters);
        vehicleCapacities.add(bicycles);
        
        Coordinates coordenadas2 = new Coordinates(30.0, 10.0, 2.0);
        Set<Capacity> vehicleCapacities2 = new HashSet<>();
        Capacity bicycles2 = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters2 = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities2.add(scooters2);
        vehicleCapacities2.add(bicycles2);
        
        Park startPark = new Park("parque 1", coordenadas, vehicleCapacities, 1);
        Park endPark = new Park("parque 2", coordenadas2, vehicleCapacities2, 2);
        Vehicle vehicle = new Bicycle(1, "xpto", 10.0, 10.0, 10.0, '1',VehicleType.BICYCLE, 15);
        Trip instance = new Trip(startTime, endTime, client, startPark, endPark, vehicle);

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

        Client client = new Client("a@a.a", "aaa", 10, 111, 19, 1.78f, 75.0f, 'M', "1111111111111111", "10/12/2021");

        Coordinates coordenadas = new Coordinates(10.0, 20.0, 12.0);
        Set<Capacity> vehicleCapacities = new HashSet<>();
        Capacity bicycles = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities.add(scooters);
        vehicleCapacities.add(bicycles);
        
        Coordinates coordenadas2 = new Coordinates(30.0, 10.0, 2.0);
        Set<Capacity> vehicleCapacities2 = new HashSet<>();
        Capacity bicycles2 = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters2 = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities2.add(scooters2);
        vehicleCapacities2.add(bicycles2);
        
        Park startPark = new Park("parque 1", coordenadas, vehicleCapacities, 1);
        Park endPark = new Park("parque 2", coordenadas2, vehicleCapacities2, 2);
        Vehicle vehicle = new Bicycle(1, "xpto", 10.0, 10.0, 10.0, '1',VehicleType.BICYCLE, 15);
        Trip instance = new Trip(startTime, endTime, client, startPark, endPark, vehicle);

        Calendar expResult = Calendar.getInstance();
        expResult.clear();
        expResult.set(2019, 10, 9, 12, 10);
        Calendar result = instance.getStartTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getClient method, of class Trip.
     */
    @Test
    public void testGetClient() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

        Client client = new Client("a@a.a", "aaa", 10, 111, 19, 1.78f, 75.0f, 'M', "1111111111111111", "10/12/2021");

        Coordinates coordenadas = new Coordinates(10.0, 20.0, 12.0);
        Set<Capacity> vehicleCapacities = new HashSet<>();
        Capacity bicycles = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities.add(scooters);
        vehicleCapacities.add(bicycles);
        
        Coordinates coordenadas2 = new Coordinates(30.0, 10.0, 2.0);
        Set<Capacity> vehicleCapacities2 = new HashSet<>();
        Capacity bicycles2 = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters2 = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities2.add(scooters2);
        vehicleCapacities2.add(bicycles2);
        
        Park startPark = new Park("parque 1", coordenadas, vehicleCapacities, 1);
        Park endPark = new Park("parque 2", coordenadas2, vehicleCapacities2, 2);
        
        Vehicle vehicle = new Bicycle(1, "xpto", 10.0, 10.0, 10.0, '1',VehicleType.BICYCLE, 15);
        Trip instance = new Trip(startTime, endTime, client, startPark, endPark, vehicle);

        Client expResult = new Client("a@a.a", "aaa", 10, 111, 19, 1.78f, 75.0f, 'M', "1111111111111111", "10/12/2021");

        Client result = instance.getClient();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartPark method, of class Trip.
     */
    @Test
    public void testGetStartPark() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

        Client client = new Client("a@a.a", "aaa", 10, 111, 19, 1.78f, 75.0f, 'M', "1111111111111111", "10/12/2021");
        
        Coordinates coordenadas = new Coordinates(10.0, 20.0, 12.0);
        Set<Capacity> vehicleCapacities = new HashSet<>();
        Capacity bicycles = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities.add(scooters);
        vehicleCapacities.add(bicycles);
        
        Coordinates coordenadas2 = new Coordinates(30.0, 10.0, 2.0);
        Set<Capacity> vehicleCapacities2 = new HashSet<>();
        Capacity bicycles2 = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters2 = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities2.add(scooters2);
        vehicleCapacities2.add(bicycles2);
        
        Park startPark = new Park("parque 1", coordenadas, vehicleCapacities, 1);
        Park endPark = new Park("parque 2", coordenadas2, vehicleCapacities2, 2);
        
        Vehicle vehicle = new Bicycle(1, "xpto", 10.0, 10.0, 10.0, '1',VehicleType.BICYCLE, 15);
        Trip instance = new Trip(startTime, endTime, client, startPark, endPark, vehicle);

        Park expResult = new Park("parque 1", coordenadas, vehicleCapacities, 1);

        Park result = instance.getStartPark();
        assertEquals(expResult.getParkId(), result.getParkId());
    }

    /**
     * Test of getEndPark method, of class Trip.
     */
    @Test
    public void testGetEndPark() {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

        Client client = new Client("a@a.a", "aaa", 10, 111, 19, 1.78f, 75.0f, 'M', "1111111111111111", "10/12/2021");
        
        Coordinates coordenadas = new Coordinates(10.0, 20.0, 12.0);
        Set<Capacity> vehicleCapacities = new HashSet<>();
        Capacity bicycles = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities.add(scooters);
        vehicleCapacities.add(bicycles);
        
        Coordinates coordenadas2 = new Coordinates(30.0, 10.0, 2.0);
        Set<Capacity> vehicleCapacities2 = new HashSet<>();
        Capacity bicycles2 = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters2 = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities2.add(scooters2);
        vehicleCapacities2.add(bicycles2);
        
        Park startPark = new Park("parque 1", coordenadas, vehicleCapacities, 1);
        Park endPark = new Park("parque 2", coordenadas2, vehicleCapacities2, 2);
        
        Vehicle vehicle = new Bicycle(1, "xpto", 10.0, 10.0, 10.0, '1',VehicleType.BICYCLE, 15);
        Trip instance = new Trip(startTime, endTime, client, startPark, endPark, vehicle);

        Park expResult = new Park("parque 2", coordenadas2, vehicleCapacities2, 2);;

        Park result = instance.getEndPark();
        assertEquals(expResult.getParkId(), result.getParkId());
    }

    /**
     * Test of getVehicle method, of class Trip.
     */
    @Test
    public void testGetVehicle() {
          Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(2019, 10, 9, 12, 10);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(2019, 10, 9, 12, 10);

        Client client = new Client("a@a.a", "aaa", 10, 111, 19, 1.78f, 75.0f, 'M', "1111111111111111", "10/12/2021");
        
        Coordinates coordenadas = new Coordinates(10.0, 20.0, 12.0);
        Set<Capacity> vehicleCapacities = new HashSet<>();
        Capacity bicycles = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities.add(scooters);
        vehicleCapacities.add(bicycles);
        
        Coordinates coordenadas2 = new Coordinates(30.0, 10.0, 2.0);
        Set<Capacity> vehicleCapacities2 = new HashSet<>();
        Capacity bicycles2 = new Capacity(20, 10, VehicleType.BICYCLE);
        Capacity scooters2 = new Capacity(30, 2, VehicleType.ELECTRIC_SCOOTER);
        vehicleCapacities2.add(scooters2);
        vehicleCapacities2.add(bicycles2);
        
        Park startPark = new Park("parque 1", coordenadas, vehicleCapacities, 1);
        Park endPark = new Park("parque 2", coordenadas2, vehicleCapacities2, 2);
        Vehicle vehicle = new Bicycle(1, "xpto", 10.0, 10.0, 10.0, '1',VehicleType.BICYCLE, 15);
        Trip instance = new Trip(startTime, endTime, client, startPark, endPark, vehicle);

        Vehicle expResult = new Bicycle(1, "xpto", 10.0, 10.0, 10.0, '1',VehicleType.BICYCLE, 15);

        Vehicle result = instance.getVehicle();
        assertEquals(expResult, result);
    }
    
}
