/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.model.park.Park;
import lapr.project.model.park.ParkCapacity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author kevin
 */
public class ParkTest {

    @Test
    public void testGetName() {
        System.out.println("getName");
        Coordinates coord = new Coordinates(0,0,0);
        Park instance = new Park("parque", coord);
        String expResult = "parque";
        String result = instance.getName();
        assertEquals(expResult, result);

    }

    @Test
    public void testGetCord() {
        System.out.println("getCord");
        Coordinates cord = new Coordinates(0, 0, 0);
        Park instance = new Park("parque", cord);
        Coordinates expResult = cord;
        Coordinates result = instance.getCord();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetBikeCapacity() {
        System.out.println("getBikeCapacity");
        Coordinates cord = new Coordinates(0, 0, 0);
        Park instance = new Park("parque", cord);
        ParkCapacity expResult = new ParkCapacity(10, 0);
        ParkCapacity result = instance.getBikeCapacity();
        assertEquals(expResult, result);
    }

    @Test
    public void getScooterOffroadCapacity() {
        System.out.println("getScooterOffroadCapacity");
        Coordinates cord = new Coordinates(0, 0, 0);
        Park instance = new Park("parque", cord);
        ParkCapacity expResult = new ParkCapacity(10, 0);
        ParkCapacity result = instance.getScooterOffroadCapacity();
        assertEquals(expResult, result);
    }
    
    @Test
    public void getScooterUrbanCapacity() {
        System.out.println("getScooterUrbanCapacity");
        Coordinates cord = new Coordinates(0, 0, 0);
        Park instance = new Park("parque", cord);
        ParkCapacity expResult = new ParkCapacity(10, 0);
        ParkCapacity result = instance.getScooterUrbanCapacity();
        assertEquals(expResult, result);
    }
}
