package lapr.project.model.vehicles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertSame(VehicleType.BICYCLE, vehicleType);

        sqlName = "electric_scooter";
        vehicleType = VehicleType.parseVehicleType(sqlName);
        assertSame(VehicleType.ELECTRIC_SCOOTER, vehicleType);

        sqlName = "car";
        assertNull(VehicleType.parseVehicleType(sqlName));
    }
}
