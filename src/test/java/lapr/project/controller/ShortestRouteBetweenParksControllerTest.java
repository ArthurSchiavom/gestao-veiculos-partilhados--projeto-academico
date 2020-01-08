package lapr.project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShortestRouteBetweenParksControllerTest {

    private static DataHandler dh;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static ShortestRouteBetweenParksController controller;



    @BeforeEach
    void prepare() {
        dh = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new ShortestRouteBetweenParksController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareStatement(any(String.class))).thenReturn(preparedStatement);
            when(dh.executeUpdate(preparedStatement)).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shortestRouteBetweenTwoParksFetchByCoordinates2() {
        try {
            controller.shortestRouteBetweenTwoParksFetchByCoordinates(2.3,3.2,-3.1,13.3);
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }

    @Test
    void shortestRouteBetweenTwoParksFetchByCoordinates() {
        try {
            controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(18.222,20.12,18.222,22.12,"testFiles/uhm.txt","outputWTF.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }

    @Test
    void shortestRouteBetweenTwoParksFetchByID() {
        try {
            controller.shortestRouteBetweenTwoParksFetchByID("uhm", "wot",2,"outputWTF.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }

    @Test
    void shortestRouteBetweenTwoParksAndGivenPoisFetchById0() {
        try {
            controller.shortestRouteBetweenTwoParksAndGivenPoisFetchById("uhm", "wot","ad.txt","outputWTF.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }

    @Test
    void shortestRouteBetweenTwoParksFetchByCoordinates0() {
        try {
            controller.shortestRouteBetweenTwoParksFetchByCoordinates(2.3,2.4,3.1,0,3,"outputWTF.txt");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }

    @Test
    void shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates() {
//        long result=0;
//        try {
//            result = controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(18.222,20.12,18.222,22.12,"testFiles/uhm.txt","outputWTF.txt");
//        } catch (SQLException | IOException | InvalidFileDataException e) {
//            System.out.println(e.getMessage());
//        }

        try {
            controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(18.222,20.12,18.222,22.12,"testFiles/uhm.txt","outputWTF.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
//        assertEquals(0,result);
    }

    @Test
    void shortestRouteBetweenTwoParksFetchById() {
        try {
            controller.shortestRouteBetweenTwoParksFetchById("1","2","outputWTF.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }

    @Test
    void shortestRouteBetweenTwoParksAndGivenPoisFetchById() {
        try {
            controller.shortestRouteBetweenTwoParksAndGivenPoisFetchById("1","2","testFiles/uhm.txt","outputWTF.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }
}