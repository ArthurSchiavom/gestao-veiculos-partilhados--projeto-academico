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
     * Test of getPointA method, of class Path.
     */
    @Test
    public void testGetPointA() {
        PointOfInterest pointA = new PointOfInterest("parque", new Coordinates(0.0, 0.0, 0));
        PointOfInterest pointB = new PointOfInterest("parque", new Coordinates(1.0, 1.0, 1));

        Path instance = new Path(pointA, pointB, 1.20, 20, 3.4);
        PointOfInterest expResult = pointA;
        PointOfInterest result = instance.getPointA();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPointB method, of class Path.
     */
    @Test
    public void testGetPointB() {
        PointOfInterest pointA = new PointOfInterest("parque", new Coordinates(0.0, 0.0, 0));
        PointOfInterest pointB = new PointOfInterest("parque", new Coordinates(1.0, 1.0, 1));

        Path instance = new Path(pointA, pointB, 1.20, 20, 3.4);
        PointOfInterest expResult = pointB;
        PointOfInterest result = instance.getPointB();
        assertEquals(expResult, result);
    }

    /**
     * Test of getKineticCoefficient method, of class Path.
     */
    @Test
    public void testGetKineticCoefficient() {
        PointOfInterest pointA = new PointOfInterest("parque", new Coordinates(0.0, 0.0, 0));
        PointOfInterest pointB = new PointOfInterest("parque", new Coordinates(1.0, 1.0, 1));

        Path instance = new Path(pointA, pointB, 1.20, 20, 3.4);
        double expResult = 1.20;
        double result = instance.getKineticCoefficient();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindDirectionDegrees method, of class Path.
     */
    @Test
    public void testGetWindDirectionDegrees() {
        PointOfInterest pointA = new PointOfInterest("parque", new Coordinates(0.0, 0.0, 0));
        PointOfInterest pointB = new PointOfInterest("parque", new Coordinates(1.0, 1.0, 1));

        Path instance = new Path(pointA, pointB, 1.20, 20, 3.4);
        int expResult = 20;
        int result = instance.getWindDirectionDegrees();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindSpeed method, of class Path.
     */
    @Test
    public void testGetWindSpeed() {
        PointOfInterest pointA = new PointOfInterest("parque", new Coordinates(0.0, 0.0, 0));
        PointOfInterest pointB = new PointOfInterest("parque", new Coordinates(1.0, 1.0, 1));

        Path instance = new Path(pointA, pointB, 1.20, 20, 3.4);
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
        
        Object path1 = new Path(pointA, pointB, 1.2, 2, 1.3);
        assertEquals(path1, path1);

        Coordinates cor2 = new Coordinates(0, 0, 0);
        Path path2 = new Path(pointA, pointB, 1.0, 3, 2.0);
        assertEquals(path1, path2);

        path1 = null;
        assertNotEquals(path2, path1);

        
        Path path3 = new Path(pointB, pointA, 1.0, 2, 3.0);
        assertNotEquals(path3, path2);
    }

    /**
     * Test of hashCode method, of class Path.
     */
    @Test
    public void testHashCode() {
        PointOfInterest pointA = new PointOfInterest("parque", new Coordinates(0.0, 0.0, 0));
        PointOfInterest pointB = new PointOfInterest("parque2", new Coordinates(1.0, 1.0, 1));
        
        Path instance = new Path(pointA, pointB, 1.0, 2, 1.2);
        int expResult = instance.hashCode();
        Path path1 = new Path(pointA, pointB, 1.0,2, 1.2);
        assertEquals(expResult, path1.hashCode());

        
        Path path2 = new Path(pointB, pointA, 2.0, 3, 1.0);
        expResult = path2.hashCode();
        assertNotEquals(expResult, path1.hashCode());
    }

}
