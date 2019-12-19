package lapr.project.model.vehicles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectricScooterTest {

    ElectricScooter instance;
    @BeforeEach
    void beforeEach() {
        instance = new ElectricScooter(1,2.3F,2.4F,
                35,true,ElectricScooterType.URBAN,15,
                150f,"description", 120);
    }
    @Test
    void getElectricScooterTypeTest() {
        assertEquals(ElectricScooterType.URBAN, instance.getElectricScooterType());
    }

    @Test
    void getActualBatteryCapacityTest() {
        assertEquals(15, instance.getActualBatteryCapacity());
    }

    @Test
    void getMaxBatteryCapacityTest() {
        assertEquals(150f, instance.getMaxBatteryCapacity());
    }

    @Test
    void getDescriptionTest() {
        assertEquals("description", instance.getDescription());
    }

    @Test
    void getEnginePowerTest() {
        assertEquals(120, instance.getEnginePower());
    }
}