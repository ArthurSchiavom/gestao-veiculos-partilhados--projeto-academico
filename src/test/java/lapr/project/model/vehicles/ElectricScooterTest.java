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
                150f,"description");
    }
    @Test
    void getElectricScooterType() {
        assertEquals(ElectricScooterType.URBAN, instance.getElectricScooterType());
    }

    @Test
    void getActual_battery_capacity() {
        assertEquals(15, instance.getActualBatteryCapacity());
    }

    @Test
    void getMax_battery_capacity() {
        assertEquals(150f, instance.getMaxBatteryCapacity());
    }

    @Test
    void getEletric_scooter_description() {
        assertEquals("description", instance.getElectricScooterDescription());
    }
}