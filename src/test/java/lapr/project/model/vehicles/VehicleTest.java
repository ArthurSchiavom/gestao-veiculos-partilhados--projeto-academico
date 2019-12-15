package lapr.project.model.vehicles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class VehicleTest {

    Vehicle instance;
    @BeforeEach
    void beforeEach() {
        instance = new Bicycle(1,2.3F,2.4F,35,2.3F,2.9F,60,true,VehicleType.BICYCLE,15,"description");
    }

    @Test
    void getId() {
        assertEquals(1, instance.getId());
    }

    @Test
    void getLatitude() {
        assertEquals(2.3F, instance.getLatitude());
    }

    @Test
    void getLongitude() {
        assertEquals(2.4F, instance.getLongitude());
    }

    @Test
    void getAerodynamic_coefficient() {
        assertEquals(2.3F, instance.getAerodynamic_coefficient());
    }

    @Test
    void getFrontal_area() {
        assertEquals(2.9F, instance.getFrontal_area());
    }

    @Test
    void getAltitude() {
        assertEquals(35, instance.getAltitude());
    }

    @Test
    void getWeight() {
        assertEquals(60, instance.getWeight());
    }

    @Test
    void isAvailable() {
        assertEquals(true, instance.isAvailable());
    }

    @Test
    void getType() {
        assertEquals(VehicleType.BICYCLE, instance.getType());
    }

    @Test
    void testEquals() {
        assertEquals(true, instance.equals(new Bicycle(1,2.3F,2.4F,35,2.3F,2.9F,60,true,VehicleType.BICYCLE,15,"description")));
    }

    @Test
    void testHashCode() {
        assertEquals(new Bicycle(1,2.3F,2.4F,35,2.3F,2.9F,60,true,VehicleType.BICYCLE,15,"description").hashCode(), instance.hashCode());
    }
}