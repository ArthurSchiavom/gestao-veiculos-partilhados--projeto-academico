/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.point.of.interest;

import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Capacity;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * 
 */
public class PointOfInterestTest {
    
    @BeforeEach
    public void setUp() {
    }

    /**
     * Test of getDescription method, of class PointOfInterest.
     */
    @Test
    public void testGetDescription() {
        Coordinates coordenadas = new Coordinates(11.111, 22.222, -10);
        PointOfInterest instance = new PointOfInterest("parque 1", coordenadas);
        String expResult = "parque 1";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        
        String expResult2 = "parque 2";
        assertNotEquals(expResult2, result);

        try{
            instance = new PointOfInterest(null, coordenadas);
            fail("Didn't catch the IllegalArgumentException exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true,"Caught the IllegalArgumentException" +
                    " exception");
        }

        try{
            instance = new PointOfInterest("an interesting description",
                    null);
            fail("Didn't catch the IllegalArgumentException exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true,"Caught the IllegalArgumentException" +
                    " exception");
        }
    }

    /**
     * Test of getCoordinates method, of class PointOfInterest.
     */
    @Test
    public void testGetCoordinates() {
        Coordinates coordenadas = new Coordinates(11.111, 22.222, 10);
        PointOfInterest instance = new PointOfInterest("parque 1", coordenadas);;
        Coordinates expResult = coordenadas;
        Coordinates result = instance.getCoordinates();
        assertEquals(expResult, result);
       
        Coordinates expResult2 = new Coordinates(1.111, 2.222, -1);
        assertNotEquals(expResult2, result);
    }

    /**
     * Test of equals method, of class PointOfInterest.
     */
    @Test
    public void testEquals() {
        Coordinates cor1 = new Coordinates(0,0,0);
        Object poi1= new PointOfInterest("parque 1", cor1);
        assertEquals(poi1, poi1);
        
        Coordinates cor2 = new Coordinates(30,10,20);
        PointOfInterest poi2= new PointOfInterest("parque 1", cor2);
        assertEquals(poi1, poi2);

        poi1 = null;
        assertNotEquals(poi2, poi1);

        Coordinates cor3 = new Coordinates(3,3,3);   
         PointOfInterest poi3 = new PointOfInterest("parque 3", cor3);

        assertNotEquals(poi3, poi2);

        List<Capacity> capacities = new ArrayList<>();
        capacities.add(new Capacity(15,2, VehicleType.ELECTRIC_SCOOTER));
        Park park = new Park("Ribeira", 220f, 16f,capacities,"Ribeira",new Coordinates(0,0,0));
        PointOfInterest poiPedro = new PointOfInterest("Ribeira", new Coordinates(0,0,0));
        assertEquals(poiPedro, park);
    }

    /**
     * Test of hashCode method, of class PointOfInterest.
     */
    @Test
    public void testHashCode() {
        Coordinates coordenadas = new Coordinates(11.111, 22.222, 10);
        PointOfInterest instance = new PointOfInterest("parque 1", coordenadas);;
        int expResult = instance.hashCode();
        PointOfInterest poi1 = new PointOfInterest("parque 1", coordenadas);
        assertEquals(expResult, poi1.hashCode());

        Coordinates cor2 = new Coordinates(0,0,0);
        PointOfInterest poi2= new PointOfInterest("parque 2", cor2);
        expResult = poi2.hashCode();
        assertNotEquals(expResult, poi1.hashCode());
    }
    
}
