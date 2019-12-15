package lapr.project.model.vehicles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BicycleTest {
    Bicycle instance;
    @BeforeEach
    void beforeEach() {
        instance = new Bicycle(1,2.3F,2.4F,35,2.3F,2.9F,60,true,VehicleType.BICYCLE,15,"description");
    }
    @Test
    void getSize() {
        assertEquals(15, instance.getSize());
    }

    @Test
    void getDescription() {
        assertEquals("description", instance.getDescription());
    }
}