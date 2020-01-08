package lapr.project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {

    @Test
    void testGetLatitude() {
        Coordinates coords = new Coordinates(0, 0, 0);
        double expResult = 0;
        double result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(91, 0, 0);
        expResult = 89;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(185, 0, 0);
        expResult = -5;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-91, 0, 0);
        expResult = -89;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-181, 0, 0);
        expResult = 1;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(90, 0, 0);
        expResult = 90;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-90, 0, 0);
        expResult = -90;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(180, 0, 0);
        expResult = 0;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(-180, 0, 0);
        expResult = 0;
        result = coords.getLatitude();
        assertEquals(expResult, result, 0);
    }

    @Test
    void testGetLongitude() {
        Coordinates coords = new Coordinates(0, 0, 0);
        double expResult = 0;
        double result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0, 181, 0);
        expResult = -179;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0, -181, 0);
        expResult = 179;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0, -180, 0);
        expResult = 180;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);

        coords = new Coordinates(0, 180, 0);
        expResult = 180;
        result = coords.getLongitude();
        assertEquals(expResult, result, 0);
    }

    @Test
    void testGetAltitude() {
        Coordinates coords = new Coordinates(0, 0, 0);
        double expResult = 0;
        assertEquals(expResult, coords.getAltitude(), 0);
    }

    @Test
    void testDistance() {
        Coordinates coords1 = new Coordinates(0, 0, 0);
        Coordinates coords2 = new Coordinates(0, 0, 0);
        double expResult = 0;
        assertEquals(expResult, coords1.distance(coords2), 0);

        coords2 = new Coordinates(0.0, 0.0, 5000);
        expResult = 5;
        assertEquals(expResult, coords1.distance(coords2));

        coords2 = new Coordinates(0, 50, 0);
        expResult = 5559.74;
        assertEquals(expResult, coords1.distance(coords2), 0.01);

        coords2 = new Coordinates(50, 0, 0);
        expResult = 5559.74;
        assertEquals(expResult, coords1.distance(coords2), 0.01);

        coords1 = new Coordinates(38.7071631, -9.135517, 0);
        coords2 = new Coordinates(40.4166909, -3.7003454, 0);
        expResult = 503.096;
        assertEquals(expResult, coords1.distance(coords2), 0.01);

        coords1 = new Coordinates(38.7071631, -9.135517, 50);
        coords2 = new Coordinates(40.4166909, -3.7003454, 500);
        expResult = 503.097;
        assertEquals(expResult, coords1.distance(coords2), 0.01);

        coords1 = new Coordinates(0.0, 0.0, 100);
        coords2 = new Coordinates(0.0, 0.0001, 300);
        expResult = 0.01;
        assertEquals(expResult, coords1.distance(coords2), 0.01);

    }

    @Test
    void testToString() {
        Coordinates coords = new Coordinates(0, 0, 0);
        String expResult = "Coordinates{"
                + "latitude=" + coords.getLatitude()
                + ", longitude=" + coords.getLongitude()
                + ", altitude=" + coords.getAltitude()
                + '}';
        assertEquals(expResult, coords.toString());
    }

    @Test
    void testEquals() {
        Object coords1 = new Coordinates(0, 0, 0);
        assertEquals(coords1, coords1);
        Coordinates coords2 = new Coordinates(0, 0, 0);
        assertEquals(coords1, coords2);

        coords1 = null;
        assertNotEquals(coords2, coords1);

        coords1 = "";
        assertNotEquals(coords1, coords2);

        coords1 = new Coordinates(10, 10, 10);
        assertNotEquals(coords1, coords2);
    }

    @Test
    void testHashCode() {
        Coordinates coord = new Coordinates(0, 0, 0);
        int expResult = coord.hashCode();
        Coordinates coord1 = new Coordinates(0, 0, 0);
        assertEquals(expResult, coord1.hashCode());

        coord = new Coordinates(10, 10, 10);
        expResult = coord.hashCode();
        assertNotEquals(expResult, coord1.hashCode());
    }

    /**
     * Test of getCoordinateX method, of class Coordinates.
     */
    @Test
    public void testGetCoordinateX() {
        Coordinates instance = new Coordinates(0, 0, 0);
        double expResult = 6371000;
        double result = instance.getCoordinateX();
        assertEquals(expResult, result, 0.0001);

        Coordinates instance2 = new Coordinates(1, 1, 1);
//        double expResult2 = 1859864.2521791;
        double expResult2 = 6369059.47946933;//TODO: @Zé corrigir este teste
        double result2 = instance2.getCoordinateX();
        assertEquals(expResult2, result2, 0.0001);

        Coordinates instance3 = new Coordinates(-1, -1, -1);
//        double expResult3 = 1859864.2521791;//TODO: @Zé fix this test
        double expResult3 = 6369059.47946933;
        double result3 = instance3.getCoordinateX();
        assertEquals(expResult3, result3, 0.0001);
    }

    /**
     * Test of getCoordinateY method, of class Coordinates.
     */
    @Test
    public void testGetCoordinateY() {
        Coordinates instance = new Coordinates(0, 0, 0);
        double expResult = 0.0;
        double result = instance.getCoordinateY();
        assertEquals(expResult, result, 0.0001);

        Coordinates instance2 = new Coordinates(1, 1, 1);
//        double expResult2 = 2896566.9531532; //TODO @Zé fix this teste
        double expResult2 = 111172.34674581685;
        double result2 = instance2.getCoordinateY();
        assertEquals(expResult2, result2, 0.0001);

        Coordinates instance3 = new Coordinates(-1, -1, -1);
//        double expResult3 = -2896566.9531532;//TODO @Zé fix this test
        double expResult3 = -111172.34674581685;
        double result3 = instance3.getCoordinateY();
        assertEquals(expResult3, result3, 0.0001);
    }

    /**
     * Test of getCoordinateZ method, of class Coordinates.
     */
    @Test
    public void testGetCoordinateZ() {
        Coordinates instance = new Coordinates(0, 0, 0);
        double expResult = 0.0;
        double result = instance.getCoordinateZ();
        assertEquals(expResult, result, 0.0001);

        Coordinates instance2 = new Coordinates(1, 1, 1);
//        double expResult2 = 5361011.6442111; //TODO: @Zé fix this test
        double expResult2 = 111189.28141193325;
        double result2 = instance2.getCoordinateZ();
        assertEquals(expResult2, result2, 0.0001);

        Coordinates instance3 = new Coordinates(-1, -1, -1);
//        double expResult3 = -5361011.6442111;//TODO: @Zé fix this test
        double expResult3 = -111189.28141193325;
        double result3 = instance3.getCoordinateZ();
        assertEquals(expResult3, result3, 0.0001);
    }
}
