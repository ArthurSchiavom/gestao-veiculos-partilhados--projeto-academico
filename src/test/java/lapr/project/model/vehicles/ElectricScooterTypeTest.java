package lapr.project.model.vehicles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertSame(ElectricScooterType.URBAN, electricScooterType);

        sqlName = "offroad";
        electricScooterType = ElectricScooterType.parseScooterType(sqlName);
        assertSame(ElectricScooterType.OFFROAD, electricScooterType);

        sqlName = "flying";
        assertNull(ElectricScooterType.parseScooterType(sqlName));
    }

    @Test
    void getFileNameTest() {
        assertEquals("city", ElectricScooterType.URBAN.getFileName());
        assertEquals("off-road", ElectricScooterType.OFFROAD.getFileName());
    }
}
