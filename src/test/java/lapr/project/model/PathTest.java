/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.model.point.of.interest.PointOfInterest;
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

        Path instance = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);
        double expResult = 1.20;
        double result = instance.getKineticCoefficient();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindDirectionDegrees method, of class Path.
     */
    @Test
    public void testGetWindDirectionDegrees() {
        Path instance = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);
        int expResult = 20;
        int result = instance.getWindDirectionDegrees();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindSpeed method, of class Path.
     */
    @Test
    public void testGetWindSpeed() {
        Path instance = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);
        double expResult = 3.4;
        double result = instance.getWindSpeed();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Path.
     */
    @Test
    public void testEquals() {

        Object path1 = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);
        assertEquals(path1, path1);

        Path path2 = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);;
        assertEquals(path1, path2);

        path1 = null;
        assertNotEquals(path2, path1);

        Path path3 = new Path(new Coordinates(1.0, 1.0, 0), new Coordinates(2.0, 2.0, 1), 1.20, 20, 3.4);;
        assertNotEquals(path3, path2);
    }

    /**
     * Test of hashCode method, of class Path.
     */
    @Test
    public void testHashCode() {

        Path instance = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);
        int expResult = instance.hashCode();
        Path path1 = new Path(new Coordinates(0.0, 0.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);
        assertEquals(expResult, path1.hashCode());

        Path path2 = new Path(new Coordinates(1.0, 10.0, 0), new Coordinates(1.0, 1.0, 1), 1.20, 20, 3.4);
        expResult = path2.hashCode();
        assertNotEquals(expResult, path1.hashCode());
    }

    /**
     * Test of getStartingPoint method, of class Path.
     */
    @Test
    public void testGetStartingPoint() {
        Coordinates cor1 = new Coordinates(0.0, 0.0, 0);
        Coordinates cor2 = new Coordinates(1.0, 1.0, 1);
        Path instance = new Path(cor1, cor2, 1.20, 20, 3.4);
        Coordinates expResult = cor1;
        Coordinates result = instance.getStartingPoint();
        assertEquals(expResult, result);

    }

    /**
     * Test of getEndingPoint method, of class Path.
     */
    @Test
    public void testGetEndingPoint() {
        Coordinates cor1 = new Coordinates(0.0, 0.0, 0);
        Coordinates cor2 = new Coordinates(1.0, 1.0, 1);
        Path instance = new Path(cor1, cor2, 1.20, 20, 3.4);;
        Coordinates expResult = cor2;
        Coordinates result = instance.getEndingPoint();
        assertEquals(expResult, result);
    }

}
