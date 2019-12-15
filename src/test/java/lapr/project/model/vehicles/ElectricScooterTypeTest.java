package lapr.project.model.vehicles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElectricScooterTypeTest {
    @Test
    void getSQLNameTest() {
        ElectricScooterType scooterType = ElectricScooterType.URBAN;
        assertEquals("urban", scooterType.getSQLName());

        scooterType = ElectricScooterType.OFFROAD;
        assertEquals("offroad", scooterType.getSQLName());
    }

    @Test
    void parseScooterTypeTest() {
        String sqlName = "urban";
        ElectricScooterType electricScooterType = ElectricScooterType.parseScooterType(sqlName);
        assertTrue(electricScooterType == ElectricScooterType.URBAN);

        sqlName = "offroad";
        electricScooterType = ElectricScooterType.parseScooterType(sqlName);
        assertTrue(electricScooterType == ElectricScooterType.OFFROAD);
    }
}
