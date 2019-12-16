/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.point.of.interest.park;

import java.util.ArrayList;
import java.util.List;
import lapr.project.model.Coordinates;
import lapr.project.model.vehicles.VehicleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ParkTest {


    @Test
    public void testGetParkInputVoltage() {
        System.out.println("getParkInputVoltage");
        Coordinates coord = new Coordinates(0, 0, 0);

        Capacity cap = new Capacity(30,20, VehicleType.BICYCLE);
        List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        float expResult = 10.2f;
        float result = instance.getParkInputVoltage();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetParkInputCurrent() {
        System.out.println("getParkInputCurrent");
        Coordinates coord = new Coordinates(0, 0, 0);

        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        float expResult = 10.1f;
        float result = instance.getParkInputCurrent();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetParkId() {
        System.out.println("getParkId");
        Coordinates coord = new Coordinates(0, 0, 0);

        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        String expResult = "new park";
        String result = instance.getId();
        assertEquals(expResult, result);

    }

    @Test
    public void testGetcoord() {
        System.out.println("getcoord");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates coord = new Coordinates(0, 0, 0);
         List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        Coordinates result = instance.getCoordinates();
        assertEquals(coord, result);
    }

    @Test
    public void getAmountOccupiedByType(){
        System.out.println("getAmountOccupiedByType");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates coord = new Coordinates(0, 0, 0);
         List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        int expResult = 20;
        int result = instance.getAmountOccupiedByType(VehicleType.BICYCLE);
        assertEquals(expResult, result);
    }

    @Test
    public void getAmountOccupiedByType_01(){
        System.out.println("getAmountOccupiedByType_01");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates coord = new Coordinates(0, 0, 0);
         List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        try {
            instance.getMaxAmountByType(VehicleType.ELECTRIC_SCOOTER);
            fail();
        } catch (EnumConstantNotPresentException e) {}
    }

    @Test
    public void getMaxAmountByType(){
        System.out.println("getAmountOccupiedByType");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates coord = new Coordinates(0, 0, 0);
         List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        int expResult = 30;
        int result = instance.getMaxAmountByType(VehicleType.BICYCLE);
        assertEquals(expResult, result);
    }

    @Test
    public void getMaxAmountByType_01(){
        System.out.println("getAmountOccupiedByType_01");
        Capacity cap = new Capacity(30,20,VehicleType.BICYCLE);
        Coordinates coord = new Coordinates(0, 0, 0);
        List<Capacity> cp = new ArrayList<>();
        cp.add(cap);
        Park instance = new Park("new park", 10.2f, 10.1f, cp,"cool park",coord);
        try {
            instance.getMaxAmountByType(VehicleType.ELECTRIC_SCOOTER);
            fail();
        } catch (EnumConstantNotPresentException e) {}
    }
}
