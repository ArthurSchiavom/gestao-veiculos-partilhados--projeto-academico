/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jose
 */
public class PathTest {

    public PathTest() {
    }

    /**
     * Test of getKineticCoefficient method, of class Path.
     */
    @Test
    public void testGetKineticCoefficient() {

        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        double expResult = 1.20;
        double result = instance.getKineticCoefficient();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindDirectionDegrees method, of class Path.
     */
    @Test
    public void testGetWindDirectionDegrees() {
        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        int expResult = 20;
        int result = instance.getWindDirectionDegrees();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindSpeed method, of class Path.
     */
    @Test
    public void testGetWindSpeed() {
        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        double expResult = 3.4;
        double result = instance.getWindSpeed();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Path.
     */
    @Test
    public void testEquals() {
        PointOfInterest pointA = new PointOfInterest("parque", new Coordinates(0.0, 0.0, 0));
        PointOfInterest pointB = new PointOfInterest("parque2", new Coordinates(1.0, 1.0, 1));

        Object path1 = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        assertEquals(path1, path1);

        
        Path path2 = new Path(0.0, 0.0, 1.0, 1.0, 1.0, 3, 2.0);
        assertEquals(path1, path2);

        path1 = null;
        assertNotEquals(path2, path1);

        Path path3 = new Path(1.0, 1.0, 0.0, 0.0, 1.0, 2, 3.0);
        assertNotEquals(path3, path2);
    }

    /**
     * Test of hashCode method, of class Path.
     */
    @Test
    public void testHashCode() {


        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        int expResult = instance.hashCode();
        Path path1 = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        assertEquals(expResult, path1.hashCode());

        Path path2 = new Path(1.0, 1.0, 1.0, 1.0, 1.20, 20, 3.4);
        expResult = path2.hashCode();
        assertNotEquals(expResult, path1.hashCode());
    }

    /**
     * Test of getLatA method, of class Path.
     */
    @Test
    public void testGetLatA() {
        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        Double expResult = 0.0;
        Double result = instance.getLatA();
        assertEquals(expResult, result);
        Double expResult2 = 0.1;
        assertNotEquals(result,expResult2);
    }

    /**
     * Test of getLonA method, of class Path.
     */
    @Test
    public void testGetLonA() {
        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        Double expResult = 0.0;
        Double result = instance.getLonA();
        assertEquals(expResult, result);
        Double expResult2 = 0.1;
        assertNotEquals(result,expResult2);
    }

    /**
     * Test of getLatB method, of class Path.
     */
    @Test
    public void testGetLatB() {
        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        Double expResult = 1.0;
        Double result = instance.getLatB();
        assertEquals(expResult, result);
        Double expResult2 = 0.1;
        assertNotEquals(result,expResult2);
    }

    /**
     * Test of getLonB method, of class Path.
     */
    @Test
    public void testGetLonB() {
        Path instance = new Path(0.0, 0.0, 1.0, 1.0, 1.20, 20, 3.4);
        Double expResult = 1.0;
        Double result = instance.getLonB();
        assertEquals(expResult, result);
        Double expResult2 = 0.1;
        assertNotEquals(result,expResult2);
    }

}
