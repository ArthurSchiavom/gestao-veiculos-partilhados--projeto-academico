package lapr.project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

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