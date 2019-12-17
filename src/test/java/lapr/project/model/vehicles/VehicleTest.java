package lapr.project.model.vehicles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class VehicleTest {

    private Vehicle instance;
    @BeforeEach
    void beforeEach() {
        instance = new Bicycle(1,2.3F,
                2.4F,35,true,
                15,"description");
    }

    @Test
    void getId() {
        assertEquals(1, instance.getId());
    }

    @Test
    void getAerodynamic_coefficient() {
        assertEquals(2.3F, instance.getAerodynamicCoefficient());
    }

    @Test
    void getFrontal_area() {
        assertEquals(2.4F, instance.getFrontalArea());
    }

    @Test
    void getWeight() {
        assertEquals(35, instance.getWeight());
    }

    @Test
    void isAvailable() {
        assertTrue(instance.isAvailable());
    }

    @Test
    void getType() {
        assertEquals(VehicleType.BICYCLE, instance.getType());
    }

    @Test
    void testEquals() {
        Object receipt1 = new Bicycle(0, 10f, 10f, 50,
                true, 15, "description");
        assertEquals(receipt1, receipt1);

        Vehicle receipt2 = new Bicycle(0, 12f, 10f, 50,
                true, 15, "description");
        assertEquals(receipt1, receipt2);

        receipt1 = new Bicycle(1, 10f, 10f, 50,
                true, 15, "description");
        assertNotEquals(receipt1, receipt2);

        receipt1 = null;
        assertNotEquals(receipt2, receipt1);

        receipt1 = "";
        assertNotEquals(receipt1, receipt2);
    }

    @Test
    void testHashCode() {
        Vehicle receipt1 = new Bicycle(0, 10f, 10f, 50,
                true, 15, "description");
        Vehicle receipt2 = new Bicycle(0, 10f, 10f, 50,
                true, 15, "description");
        int expResult = receipt2.hashCode();
        assertEquals(expResult, receipt1.hashCode());

        receipt1 = new Bicycle(1, 10f, 10f, 50,
                true, 15, "description");
        assertNotEquals(expResult, receipt1.hashCode());
    }
}