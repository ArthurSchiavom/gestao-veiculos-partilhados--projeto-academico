/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.model.Vehicles.VehicleType;
import lapr.project.model.Park.Capacity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author kevin
 */
public class CapacityTest {

    @Test
    public void testGetCapacity() {
        System.out.println("getCapacity");
        Capacity instance = new Capacity(30,20,VehicleType.ELECTRIC_SCOOTER);
        int expResult = 30;
        int result = instance.getMaxCapacity();
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAmountOccupied() {
        System.out.println("getAmount_occupied");
        Capacity instance = new Capacity(30,20,VehicleType.ELECTRIC_SCOOTER);
        int expResult = 20;
        int result = instance.getAmountOccupied();
        assertNotNull(result);
        assertEquals(expResult, result);
    }
    
     @Test
    public void getVehicleType() {
        System.out.println("getVehicleType");
        Capacity instance = new Capacity(30,20,VehicleType.ELECTRIC_SCOOTER);
        VehicleType expResult = VehicleType.ELECTRIC_SCOOTER;
        VehicleType result = instance.getVehicleType();
        assertNotNull(result);
        assertEquals(expResult, result);
    }
    
     @Test
    public void getVehicleType_01() {
        System.out.println("getVehicleType_01");
        Capacity instance = new Capacity(30,20,VehicleType.ELECTRIC_SCOOTER);
        VehicleType expResult = VehicleType.BICYCLE;
        VehicleType result = instance.getVehicleType();
        assertNotNull(result);
        assertNotEquals(expResult, result);
    }
}
