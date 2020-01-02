package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.Path;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class FilterScootersWithAutonomyControllerTest {


    private static DataHandler dh;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static FilterScootersWithAutonomyController controller;

    @BeforeAll
    static void prepare() {
        dh = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new FilterScootersWithAutonomyController();

    }


    @Test
    void filterScootersWithAutonomy() {
        Path path1 = new Path(new PointOfInterest("desc1", new Coordinates(0.0, 0.0, 0)), new PointOfInterest("desc2", new Coordinates(0.0001, 0.0001, 1)), 0.002, 1, 0.3); // 1
        Path path2 = new Path(new PointOfInterest("desc1", new Coordinates(0.0001, 0.0001, 1)), new PointOfInterest("desc2", new Coordinates(0.0002, 0.0002, 2)), 0.002, 1, 0.3); // 1
        Path path3 = new Path(new PointOfInterest("desc1", new Coordinates(0.0002, 0.0002, 2)), new PointOfInterest("desc2", new Coordinates(0.0003, 0.0003, 3)), 0.002, 1, 0.3); // 1
        List<Path> trip = new ArrayList<>();
        trip.add(path1);
        trip.add(path2);
        trip.add(path3);

        ElectricScooter instance = new ElectricScooter(123, "PT001", 2.3F, 2.4F,
                35, true, ElectricScooterType.URBAN, 15,
                1f, 500);

        ElectricScooter instance1 = new ElectricScooter(456, "PT002", 2.3F, 2.4F,
                33, true, ElectricScooterType.URBAN, 15,
                1f, 500);


        List<ElectricScooter> listElectricScooters = new ArrayList<>();
        listElectricScooters.add(instance);
        listElectricScooters.add(instance1);

        List<ElectricScooter> result = controller.filterScootersWithAutonomy(listElectricScooters, trip);
        assertEquals(listElectricScooters, result);

    }
}