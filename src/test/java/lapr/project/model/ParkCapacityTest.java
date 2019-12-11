/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.model.park.ParkCapacity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author kevin
 */
public class ParkCapacityTest {

    @Test
    public void testGetCapacity() {
        System.out.println("getCapacity");
        ParkCapacity instance = new ParkCapacity(1,20);
        int expResult = 1;
        int result = instance.getCapacity();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAmount_occupied() {
        System.out.println("getAmount_occupied");
        ParkCapacity instance = new ParkCapacity(1,20);
        int expResult = 20;
        int result = instance.getAmount_occupied();
        assertEquals(expResult, result);
    }
}
