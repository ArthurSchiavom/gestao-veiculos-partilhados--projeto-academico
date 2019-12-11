package lapr.project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {

    @Test
    void testGetLatitude() {
        Coordinates coords = new Coordinates(0,0,0);
        double expResult = 0;
        double result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(91, 0,0);
        expResult = 89;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(185, 0,0);
        expResult = -5;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-91,0,0);
        expResult = -89;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-181,0,0);
        expResult = 1;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(90,0,0);
        expResult = 90;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-90,0,0);
        expResult = -90;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(180,0,0);
        expResult = 0;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-180,0,0);
        expResult = 0;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);
    }

    @Test
    void testGetLongitude() {
        Coordinates coords = new Coordinates(0,0,0);
        double expResult = 0;
        double result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0,181,0);
        expResult = -179;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0, -181,0);
        expResult = 179;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0, -180,0);
        expResult = 180;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0, 180,0);
        expResult = 180;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);
    }

    @Test
    void testGetAltitude() {
        Coordinates coords = new Coordinates(0,0,0);
        double expResult = 0;
        assertEquals(expResult, coords.getAltitude(), 0);
    }

    @Test
    void testDistance() {
        Coordinates coords1 = new Coordinates(0,0,0);
        Coordinates coords2 = new Coordinates(0,0,0);
        double expResult = 0;
        assertEquals(expResult,coords1.distance(coords2),0);

        coords2 = new Coordinates(0,0,50);
        expResult = 50;
        assertEquals(expResult,coords1.distance(coords2),0);

        coords2 = new Coordinates(0,50,0);
        expResult = 5559.74;
        assertEquals(expResult,coords1.distance(coords2),0.01);

        coords2 = new Coordinates(50,0,0);
        expResult = 5559.74;
        assertEquals(expResult,coords1.distance(coords2),0.01);

        coords1 = new Coordinates(38.7071631, -9.135517,0);
        coords2 = new Coordinates(40.4166909, -3.7003454,0);
        expResult = 503.096;
        assertEquals(expResult,coords1.distance(coords2),0.01);

        coords1 = new Coordinates(38.7071631, -9.135517,50);
        coords2 = new Coordinates(40.4166909, -3.7003454,500);
        expResult = 953.096;
        assertEquals(expResult,coords1.distance(coords2),0.01);
    }

    @Test
    void testToString() {
        Coordinates coords = new Coordinates(0,0,0);
        String expResult = "Coordinates{"
                + "latitude=" + coords.getLatitude()
                + ", longitude=" + coords.getLongitude()
                + ", altitude=" + coords.getAltitude()
                + '}';
        assertEquals(expResult, coords.toString());
    }

    @Test
    void testEquals() {
        Object coords1 = new Coordinates(0,0,0);
        assertEquals(coords1, coords1);
        Coordinates coords2 = new Coordinates(0,0,0);
        assertEquals(coords1, coords2);

        coords1 = null;
        assertNotEquals(coords2, coords1);

        coords1 = "";
        assertNotEquals(coords1, coords2);

        coords1 = new Coordinates(10,10,10);
        assertNotEquals(coords1, coords2);
    }

    @Test
    void testHashCode() {
        Coordinates coord = new Coordinates(0,0,0);
        int expResult = coord.hashCode();
        Coordinates coord1 = new Coordinates(0,0,0);
        assertEquals(expResult, coord1.hashCode());

        coord = new Coordinates(10,10,10);
        expResult = coord.hashCode();
        assertNotEquals(expResult, coord1.hashCode());
    }
}