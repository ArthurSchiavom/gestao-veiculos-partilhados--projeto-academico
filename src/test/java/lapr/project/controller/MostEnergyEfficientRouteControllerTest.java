package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MostEnergyEfficientRouteControllerTest {

    private static DataHandler dh;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static MostEnergyEfficientRouteController controller;

    @BeforeEach
    void prepare() {
        dh = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new MostEnergyEfficientRouteController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareStatement(any(String.class))).thenReturn(preparedStatement);
            when(dh.executeUpdate(preparedStatement)).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void mostEnergyEfficientRouteBetweenTwoParks() {
        try {
            controller.mostEnergyEfficientRouteBetweenTwoParks("1","2","bicycle","15","123","testMostEnergyEfficient.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }

    @Test
    void mostEnergyEfficientRouteBetweenTwoParks2() {
        try {
            controller.mostEnergyEfficientRouteBetweenTwoParks("1","2","","15","123","testMostEnergyEfficient.txt");
        } catch (Exception e) {
            fail();
            System.out.println(e.getMessage());
        }
    }
}