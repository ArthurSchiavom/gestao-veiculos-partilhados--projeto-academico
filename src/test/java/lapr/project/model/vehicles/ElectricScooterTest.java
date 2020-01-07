package lapr.project.model.vehicles;

import java.util.ArrayList;
import java.util.List;
import lapr.project.model.Coordinates;
import lapr.project.model.Path;
import lapr.project.model.point.of.interest.PointOfInterest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectricScooterTest {

    ElectricScooter instance;
    @BeforeEach
    void beforeEach() {
        instance = new ElectricScooter(123, "PT001",2.3F,2.4F,
                35,true,ElectricScooterType.URBAN,15,
                1f, 500);
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
        assertEquals(1f, instance.getMaxBatteryCapacity());
    }

    @Test
    void getEnginePowerTest() {
        assertEquals(500, instance.getEnginePower());
    }

    /**
     * Test of getScooterAutonomy method, of class ElectricScooter.
     */
    @Test
    public void testGetScooterAutonomy() {
        assertEquals(4.2, instance.getScooterAutonomy());
    }

    /**
     * Test of hasAutonomy method, of class ElectricScooter.
     */
    @Test
    public void testHasAutonomy() {
        Path path1 = new Path(new PointOfInterest("desc1",new Coordinates(0.0, 0.0, 0)),new PointOfInterest("desc2", new Coordinates(0.0001, 0.0001, 1)), 0.002, 1, 0.3); // 1
        Path path2 = new Path(new PointOfInterest("desc1",new Coordinates(0.0001, 0.0001, 1)), new PointOfInterest("desc2",new Coordinates(0.0002, 0.0002, 2)), 0.002, 1, 0.3); // 1
        Path path3 = new Path(new PointOfInterest("desc1",new Coordinates(0.0002, 0.0002, 2)), new PointOfInterest("desc2",new Coordinates(0.0003, 0.0003, 3)), 0.002, 1, 0.3); // 1
        Path path4 = new Path(new PointOfInterest("desc1",new Coordinates(0.0003, 0.0003, 3)), new PointOfInterest("desc2",new Coordinates(0.0004, 0.0004, 4)), 0.002, 1, 0.3); // 1
        Path path5 = new Path(new PointOfInterest("desc1",new Coordinates(4.0, 4.0, 4)), new PointOfInterest("desc2",new Coordinates(-90.0, 90.0, 4)), 0.002, 1, 0.3);
       
        List<Path> trip = new ArrayList<>();
        trip.add(path1);
        trip.add(path2);
        trip.add(path3);
        boolean expResult = true;
        boolean result = instance.hasAutonomy(trip); 
        assertEquals(expResult, result);
        
        List<Path> trip2 = new ArrayList<>();
        trip2.add(path1);
        trip2.add(path2);
        trip2.add(path3);
        trip2.add(path4);
        trip2.add(path5);
        boolean result2 = instance.hasAutonomy(trip2); 
        boolean expResult2 = false;
        assertEquals (expResult2, result2);

    }


    /**
     * Test of hasAutonomyFlat method, of class ElectricScooter.
     */
    @Test
    public void testHasAutonomyFlat() {
        int km = 3;
        boolean expResult = true;
        boolean result = instance.hasAutonomyFlat(km);
        assertEquals(expResult, result);
        
        km = 5;
        expResult = false;
        result = instance.hasAutonomyFlat(km);
        assertEquals(expResult, result);

        assertTrue(instance.hasAutonomyFlat(4));
    }

    @Test
    public void testGenerateExportString() {
        String expResult = "PT001;city;15";
        assertEquals(expResult, instance.generateExportString());

        ElectricScooter electricScooter = new ElectricScooter(0, "PT002", 1f
                , 1f, 10, true, ElectricScooterType.OFFROAD, 100, 2f, 500);

        expResult = "PT002;off-road;100";
        assertEquals(expResult, electricScooter.generateExportString());
    }
}