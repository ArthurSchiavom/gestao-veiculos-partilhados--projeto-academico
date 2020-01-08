package lapr.project.model.vehicles;

import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Capacity;
import lapr.project.model.point.of.interest.park.Park;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScooterDescriptionComparatorTest {
    @Test
    void compare() {
        ElectricScooter scooter = new ElectricScooter(1,"PT001",1f,1f,10
                ,true, ElectricScooterType.URBAN,75,1,200);
        ElectricScooter scooterEqual = new ElectricScooter(2,"PT002",1f,1f,10
                ,true, ElectricScooterType.URBAN,75,1,200);
        ElectricScooter scooterLess = new ElectricScooter(3,"PT003",1f,1f,10
                ,true, ElectricScooterType.URBAN,65,1,200);
        ElectricScooter scooterMore = new ElectricScooter(4,"PT004",1f,1f,10
                ,true, ElectricScooterType.URBAN,85,1,200);

        List<Capacity> cp = new ArrayList<>();
        Park park = new Park("Trindade", 220, 16, cp, "Trindade", new Coordinates(0,0,0));

        List<ElectricScooter> scooterList = new ArrayList<>();
        scooterList.add(scooterMore);
        scooterList.add(scooterEqual);
        scooterList.add(scooter);
        scooterList.add(scooterLess);

        Collections.sort(scooterList, new ScooterDescriptionComparator());

        List<ElectricScooter> expResult = new ArrayList<>();
        expResult.add(scooter);
        expResult.add(scooterEqual);
        expResult.add(scooterLess);
        expResult.add(scooterMore);

        assertEquals(expResult, scooterList);
    }
}