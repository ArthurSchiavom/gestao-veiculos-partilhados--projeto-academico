/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * 
 */
public class PointOfInterestTest {
    
    public PointOfInterestTest() {
    }
    
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
        
        Coordinates cor2 = new Coordinates(0,0,0);
        PointOfInterest poi2= new PointOfInterest("parque 2", cor2);
        assertEquals(poi1, poi2);

        poi1 = null;
        assertNotEquals(poi2, poi1);

        Coordinates cor3 = new Coordinates(3,3,3);   
         PointOfInterest poi3 = new PointOfInterest("parque 3", cor3);
        assertNotEquals(poi3, poi2);
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
