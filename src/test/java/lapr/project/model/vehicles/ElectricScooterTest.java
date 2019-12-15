package lapr.project.model.vehicles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectricScooterTest {

    ElectricScooter instance;
    @BeforeEach
    void beforeEach() {
        instance = new ElectricScooter(1,2.3F,2.4F,35,2.3F,2.9F,60,true,VehicleType.ELECTRIC_SCOOTER,ElectricScooterType.URBAN,15,150f,"description");
    }
    @Test
    void getElectricScooterType() {
        assertEquals(ElectricScooterType.URBAN, instance.getElectricScooterType());
    }

    @Test
    void getActual_battery_capacity() {
        assertEquals(15, instance.getActual_battery_capacity());
    }

    @Test
    void getMax_battery_capacity() {
        assertEquals(150f, instance.getMax_battery_capacity());
    }

    @Test
    void getEletric_scooter_description() {
        assertEquals("description", instance.getEletric_scooter_description());
    }
}