/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class PathTest {

    public PathTest() {
    }

    Path instance;
    Object path1;
    Path path3;

    @BeforeEach
    void beforeEach() {
        instance = new Path(new PointOfInterest("desc1",new Coordinates(0.0, 0.0, 0)), new PointOfInterest("desc1", new Coordinates (1.0, 1.0, 1)), 1.20, 20, 3.4);

        path1 = new Path(new PointOfInterest("desc1",new Coordinates(0.0, 0.0, 0)), new PointOfInterest("desc1",new Coordinates(1.0, 1.0, 1)), 1.20, 20, 3.4);

        path3 = new Path(new PointOfInterest("desc1",new Coordinates(1.0, 1.0, 0)), new PointOfInterest("desc2",new Coordinates(2.0, 2.0, 1)), 1.20, 20, 3.4);;
    }
    /**
     * Test of getKineticCoefficient method, of class Path.
     */
    @Test
    public void testGetKineticCoefficient() {
        double expResult = 1.20;
        double result = instance.getKineticCoefficient();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindDirectionDegrees method, of class Path.
     */
    @Test
    public void testGetWindDirectionDegrees() {
        int expResult = 20;
        int result = instance.getWindDirectionDegrees();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWindSpeed method, of class Path.
     */
    @Test
    public void testGetWindSpeed() {
        double expResult = 3.4;
        double result = instance.getWindSpeed();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Path.
     */
    @Test
    public void testEquals() {

        assertEquals(path1, path1);

        assertEquals(path1, instance);

        path1 = null;
        assertNotEquals(instance, path1);

        assertNotEquals(path3, instance);
    }

    /**
     * Test of hashCode method, of class Path.
     */
    @Test
    public void testHashCode() {
        int expResult = instance.hashCode();
        assertEquals(expResult, path1.hashCode());
        expResult = path3.hashCode();
        assertNotEquals(expResult, path1.hashCode());
    }

    /**
     * Test of getStartingPoint method, of class Path.
     */
    @Test
    public void testGetStartingPoint() {
        PointOfInterest expResult = new PointOfInterest("desc1",new Coordinates(0.0, 0.0, 0));
        PointOfInterest result = instance.getStartingPoint();
        assertEquals(expResult, result);

    }

    /**
     * Test of getEndingPoint method, of class Path.
     */
    @Test
    public void testGetEndingPoint() {
        PointOfInterest expResult = new PointOfInterest("desc1" ,new Coordinates(1.5, 1.0, 1));
        PointOfInterest result = instance.getEndingPoint();
        assertEquals(expResult, result);
    }

}
