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

    @Test
    void findTypeTest() {
        VehicleType type = VehicleType.findType(Bicycle.class);
        assertEquals(type, VehicleType.BICYCLE);
        type = VehicleType.findType(ElectricScooter.class);
        assertEquals(type, VehicleType.ELECTRIC_SCOOTER);
        type = VehicleType.findType(Vehicle.class);
        assertNull(type);
    }

    @Test
    void getTypeClassTest() {
        assertEquals(VehicleType.BICYCLE.getActualClass(), Bicycle.class);
        assertEquals(VehicleType.ELECTRIC_SCOOTER.getActualClass(), ElectricScooter.class);
    }
}
