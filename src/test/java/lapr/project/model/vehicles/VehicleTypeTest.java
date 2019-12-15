package lapr.project.model.vehicles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleTypeTest {
    @Test
    void getSQLNameTest() {
        VehicleType vehicleType = VehicleType.BICYCLE;
        assertEquals("bicycle", vehicleType.getSQLName());

        vehicleType = VehicleType.ELECTRIC_SCOOTER;
        assertEquals("electric_scooter", vehicleType.getSQLName());
    }

    @Test
    void parseVehicleTypeTest() {
        String sqlName = "bicycle";
        VehicleType vehicleType = VehicleType.parseVehicleType(sqlName);
        assertTrue(vehicleType == VehicleType.BICYCLE);

        sqlName = "electric_scooter";
        vehicleType = VehicleType.parseVehicleType(sqlName);
        assertTrue(vehicleType == VehicleType.ELECTRIC_SCOOTER);
    }
}
