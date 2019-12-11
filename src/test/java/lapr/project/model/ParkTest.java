/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.HashSet;
import java.util.Set;
import lapr.project.model.Vehicles.VehicleType;
import lapr.project.model.Park.Park;
import lapr.project.model.Park.Capacity;
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
        Coordinates cord = new Coordinates(0, 0, 0);
        
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        
        Park instance = new Park("parque", cord,cp,123);
        String expResult = "parque";
        String result = instance.getName();
        assertEquals(expResult, result);

    }

    @Test
    public void testGetCord() {
        System.out.println("getCord");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123);
        Coordinates expResult = cord;
        Coordinates result = instance.getCoords();
        assertEquals(expResult, result);
    }
    
    @Test
    public void getAmountOccupiedByType(){
        System.out.println("getAmountOccupiedByType");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123);
        int expResult = 20;
        int result = instance.getAmountOccupiedByType(VehicleType.BICYCLE);
        assertEquals(expResult, result);
    }
    
    @Test
    public void getMaxAmountByType(){
        System.out.println("getAmountOccupiedByType");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123);
        int expResult = 30;
        int result = instance.getMaxAmountByType(VehicleType.BICYCLE);
        assertEquals(expResult, result);
    }
}
