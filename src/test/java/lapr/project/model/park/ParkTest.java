/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.park;

import java.util.HashSet;
import java.util.Set;
import lapr.project.model.Coordinates;
import lapr.project.model.Vehicles.VehicleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author kevin
 */
public class ParkTest {


    @Test
    public void testGetParkInputVoltage() {
        System.out.println("getParkInputVoltage");
        Coordinates cord = new Coordinates(0, 0, 0);

        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Fabuloso e grandioso",220F,160F);
        float expResult = 220F;
        float result = instance.getParkInputVoltage();
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetParkInputCurrent() {
        System.out.println("getParkInputCurrent");
        Coordinates cord = new Coordinates(0, 0, 0);

        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Lindo e amoroso",220F,160F);
        float expResult = 160F;
        float result = instance.getParkInputCurrent();
        assertNotNull(result);
        assertEquals(expResult, result);
    }


    @Test
    public void testGetName() {
        System.out.println("getName");
        Coordinates cord = new Coordinates(0, 0, 0);

        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Lindo e charmoso",220F,160F);
        String expResult = "parque";
        String result = instance.getName();
        assertNotNull(result);
        assertEquals(expResult, result);

    }

    @Test
    public void testGetParkId() {
        System.out.println("getParkId");
        Coordinates cord = new Coordinates(0, 0, 0);

        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Lindo e grandioso",220F,160F);
        int expResult = 123;
        int result = instance.getParkId();
        assertNotNull(result);
        assertEquals(expResult, result);

    }

    @Test
    public void testGetCord() {
        System.out.println("getCord");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Simpatico e grande",220F,160F);
        Coordinates expResult = cord;
        Coordinates result = instance.getCoords();
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    @Test
    public void getAmountOccupiedByType(){
        System.out.println("getAmountOccupiedByType");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Grande e pequeno",220F,160F);
        int expResult = 20;
        int result = instance.getAmountOccupiedByType(VehicleType.BICYCLE);
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    @Test
    public void getAmountOccupiedByType_01(){
        System.out.println("getAmountOccupiedByType_01");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Enorme e pequeno",220F,160F);
        int expResult = -1;
        int result = instance.getAmountOccupiedByType(VehicleType.ELECTRIC_SCOOTER);
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    @Test
    public void getMaxAmountByType(){
        System.out.println("getAmountOccupiedByType");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Lotado",220F,160F);
        int expResult = 30;
        int result = instance.getMaxAmountByType(VehicleType.BICYCLE);
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    @Test
    public void getMaxAmountByType_01(){
        System.out.println("getAmountOccupiedByType_01");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates cord = new Coordinates(0, 0, 0);
         Set<Capacity> cp = new HashSet<Capacity>();
        cp.add(cap);
        Park instance = new Park("parque", cord,cp,123,"Simples",220F,160F);
        int expResult = -2;
        int result = instance.getMaxAmountByType(VehicleType.ELECTRIC_SCOOTER);
        assertNotNull(result);
        assertEquals(expResult, result);
    }
}
