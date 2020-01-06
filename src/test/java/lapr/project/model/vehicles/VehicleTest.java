package lapr.project.model.vehicles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class VehicleTest {

    private Vehicle instance;
    @BeforeEach
    void beforeEach() {
        instance = new Bicycle(123,"PT001",2.3F,
                2.4F,35,true,
                15);
    }

    @Test
    void constructor() {
        try{
            Bicycle t = new Bicycle(123,null,2.3F,
                    2.4F,35,true,
                    15);
        }catch(IllegalArgumentException e) {
            if (e.getClass()!= IllegalArgumentException.class)
                fail();
        }
    }

    @Test
    void constructor2() {
        try{
            Bicycle t = new Bicycle(123,"d",2.3F,
                    2.4F,35,true,
                    15);
        }catch(IllegalArgumentException e) {
            if (e.getClass()!= IllegalArgumentException.class)
                fail();
        }
    }

    @Test
    void getUniqueNumberTest() {
        assertEquals(123, instance.getUniqueNumber());
    }

    @Test
    void getDescriptionTest() {
        assertEquals("PT001", instance.getDescription());
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
        Object bike = new Bicycle(123, "PT002", 10f, 10f, 50,
                true, 15);
        assertEquals(bike, bike);

        Vehicle bike2 = new Bicycle(123, "PT002", 12f, 10f, 50,
                true, 15);
        assertEquals(bike, bike2);

        bike = new Bicycle(123, "PT004", 10f, 10f, 50,
                true, 15);
        assertNotEquals(bike, bike2);

        bike = null;
        assertNotEquals(bike2, bike);

        bike = "";
        assertNotEquals(bike, bike2);

        ElectricScooter e = new ElectricScooter(123, "PT001",2.3F,2.4F,
                35,true,ElectricScooterType.URBAN,15,
                1f, 500);

        assertNotEquals(bike, e);
    }

    @Test
    void testHashCode() {
        Vehicle bike1 = new Bicycle(123,"PT002", 10f, 10f, 50,
                true, 15);
        Vehicle bike2 = new Bicycle(123, "PT002", 10f, 10f, 50,
                true, 15);
        int expResult = bike2.hashCode();
        assertEquals(expResult, bike1.hashCode());

        bike1 = new Bicycle(123, "PT003", 10f, 10f, 50,
                true, 15);
        assertNotEquals(expResult, bike1.hashCode());
    }
}