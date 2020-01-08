package lapr.project.controller;

import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;

import javax.xml.transform.Result;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShortestRouteBetweenParksControllerTest {

    private static DataHandler dataHandler;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static ShortestRouteBetweenParksController controller;

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new ShortestRouteBetweenParksController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dataHandler.prepareStatement(any(String.class))).thenReturn(preparedStatement);
            when(dataHandler.executeUpdate(preparedStatement)).thenReturn(1);
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
    void shortestRouteBetweenTwoParksFetchByCoordinatesEmpty() {
        try {
            PreparedStatement stmFetchCertainPOI = mock(PreparedStatement.class);
            ResultSet rsFetchCertainPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?")).thenReturn(stmFetchCertainPOI);
            when(dataHandler.executeQuery(stmFetchCertainPOI)).thenReturn(rsFetchCertainPOI);
            when(rsFetchCertainPOI.next()).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchCertainPOI.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(14).thenReturn(6);
            when(rsFetchCertainPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("Parque do kevin").thenReturn("4");

            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchPOI.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6).thenReturn(14);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(3.4).thenReturn(3.41).thenReturn(4.4).thenReturn(4.41).thenReturn(122.12);
            when(rsFetchPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4")
                    .thenReturn("Parque do kevin");
            when(rsFetchPOI.getDouble("latitude")).thenReturn(2.3).thenReturn(2.31).thenReturn(3.3).thenReturn(3.41).thenReturn(-20.222);

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(3.4).thenReturn(122.12).thenReturn(3.4).thenReturn(3.41);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(2.3).thenReturn(-20.222).thenReturn(2.3).thenReturn(2.31);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(-20.222).thenReturn(3.31).thenReturn(2.31).thenReturn(-20.222);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(122.12).thenReturn(4.41).thenReturn(3.41).thenReturn(122.12);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002).thenReturn(0.002).thenReturn(0.002).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90).thenReturn(90).thenReturn(90).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6).thenReturn(15.6).thenReturn(15.6).thenReturn(15.6);

            PreparedStatement stmFetchPOISpecific = mock(PreparedStatement.class);
            ResultSet rsFetchPOISpecific = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchPOISpecific);
            when(dataHandler.executeQuery(stmFetchPOISpecific)).thenReturn(rsFetchPOISpecific);
            when(rsFetchPOISpecific.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchPOISpecific.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6);
            when(rsFetchPOISpecific.getString("poi_description")).thenReturn("1").thenReturn("Parque do kevin").thenReturn("Parque do kevin").thenReturn("2");

            long expResult = 0;
            long result = controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(2.3,3.4,2.31,3.41,"testFiles/testShortestPathByCoordinates.txt","testFiles/outputShortestPathByCoordinates.txt");
            assertEquals(expResult, result);
        } catch (SQLException e) {
            fail("SQLException thrown\n" + e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown\n" + e.getMessage());
        } catch (InvalidFileDataException e) {
            fail("InvalidFileDataException thrown\n" + e.getMessage());
        }
    }

    @Test
    void shortestRouteBetweenTwoParksFetchByCoordinatesException() {
        try {
            PreparedStatement stmFetchCertainPOI = mock(PreparedStatement.class);
            ResultSet rsFetchCertainPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?")).thenReturn(stmFetchCertainPOI);
            when(dataHandler.executeQuery(stmFetchCertainPOI)).thenReturn(rsFetchCertainPOI);
            when(rsFetchCertainPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchCertainPOI.getInt("altitude_m")).thenReturn(15).thenReturn(5).thenReturn(14).thenReturn(6);
            when(rsFetchCertainPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("Parque do kevin").thenReturn("4");

            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchPOI.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6).thenReturn(14);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(3.4).thenReturn(3.41).thenReturn(4.4).thenReturn(4.41).thenReturn(122.12);
            when(rsFetchPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4")
                    .thenReturn("Parque do kevin");
            when(rsFetchPOI.getDouble("latitude")).thenReturn(2.3).thenReturn(2.31).thenReturn(3.3).thenReturn(3.41).thenReturn(-20.222);

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(3.4).thenReturn(122.12).thenReturn(3.4).thenReturn(3.41);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(2.3).thenReturn(-20.222).thenReturn(2.3).thenReturn(2.31);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(-20.222).thenReturn(3.31).thenReturn(2.31).thenReturn(-20.222);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(122.12).thenReturn(4.41).thenReturn(3.41).thenReturn(122.12);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002).thenReturn(0.002).thenReturn(0.002).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90).thenReturn(90).thenReturn(90).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6).thenReturn(15.6).thenReturn(15.6).thenReturn(15.6);

            PreparedStatement stmFetchPOISpecific = mock(PreparedStatement.class);
            ResultSet rsFetchPOISpecific = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchPOISpecific);
            when(dataHandler.executeQuery(stmFetchPOISpecific)).thenReturn(rsFetchPOISpecific);
            when(rsFetchPOISpecific.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchPOISpecific.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6);
            when(rsFetchPOISpecific.getString("poi_description")).thenReturn("1").thenReturn("Parque do kevin").thenReturn("Parque do kevin").thenReturn("2");

            Utils.writeToFile(new ArrayList<>(), "testFiles/outputShortestPathByCoordinates.txt");
            try {
                controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(2.3, 3.4, 2.31, 3.41, "testFiles/testShortestPathByCoordinatesNumberFormat.txt", "testFiles/outputShortestPathByCoordinates.txt");
                fail("Should have failed");
            } catch (InvalidFileDataException e) {
                assertTrue(true, "caught the exception");
            }
            try {
                controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(2.3, 3.4, 2.31, 3.41, "testFiles/testShortestPathByCoordinatesIndexOutBounds.txt", "testFiles/outputShortestPathByCoordinates.txt");
                fail("Should have failed2");
            } catch(InvalidFileDataException e) {
                assertTrue(true);
            }
        } catch (SQLException e) {
            fail("SQLException thrown\n" + e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown\n" + e.getMessage());
        }
    }

    @Test
    void shortestRouteBetweenTwoParksFetchByCoordinates() {
        try {
            PreparedStatement stmFetchCertainPOI = mock(PreparedStatement.class);
            ResultSet rsFetchCertainPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?")).thenReturn(stmFetchCertainPOI);
            when(dataHandler.executeQuery(stmFetchCertainPOI)).thenReturn(rsFetchCertainPOI);
            when(rsFetchCertainPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchCertainPOI.getInt("altitude_m")).thenReturn(15).thenReturn(5).thenReturn(14).thenReturn(6);
            when(rsFetchCertainPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("Parque do kevin").thenReturn("4");

            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchPOI.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6).thenReturn(14);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(3.4).thenReturn(3.41).thenReturn(4.4).thenReturn(4.41).thenReturn(122.12);
            when(rsFetchPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4")
                    .thenReturn("Parque do kevin");
            when(rsFetchPOI.getDouble("latitude")).thenReturn(2.3).thenReturn(2.31).thenReturn(3.3).thenReturn(3.41).thenReturn(-20.222);

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(3.4).thenReturn(122.12).thenReturn(3.4).thenReturn(3.41);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(2.3).thenReturn(-20.222).thenReturn(2.3).thenReturn(2.31);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(-20.222).thenReturn(3.31).thenReturn(2.31).thenReturn(-20.222);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(122.12).thenReturn(4.41).thenReturn(3.41).thenReturn(122.12);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002).thenReturn(0.002).thenReturn(0.002).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90).thenReturn(90).thenReturn(90).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6).thenReturn(15.6).thenReturn(15.6).thenReturn(15.6);

            PreparedStatement stmFetchPOISpecific = mock(PreparedStatement.class);
            ResultSet rsFetchPOISpecific = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchPOISpecific);
            when(dataHandler.executeQuery(stmFetchPOISpecific)).thenReturn(rsFetchPOISpecific);
            when(rsFetchPOISpecific.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchPOISpecific.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6);
            when(rsFetchPOISpecific.getString("poi_description")).thenReturn("1").thenReturn("Parque do kevin").thenReturn("Parque do kevin").thenReturn("2");

            Utils.writeToFile(new ArrayList<>(), "testFiles/outputShortestPathByCoordinates.txt");
            long expResult = 26105660;
            long result = controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(2.3,3.4,2.31,3.41,"testFiles/testShortestPathByCoordinates.txt","testFiles/outputShortestPathByCoordinates.txt");
            assertEquals(expResult, result);
            try(Scanner scanner = new Scanner(new FileReader("testFiles/outputShortestPathByCoordinates.txt"))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("Path 001")) {
                    assertTrue(true, "Correctly found the scooter in the file");
                    if(scanner.hasNextLine() && scanner.nextLine().equals("total distance: 26105660")) {
                        if(scanner.hasNextLine() && scanner.nextLine().equals("total energy: 15437,97")) {
                            if(scanner.hasNextLine() && scanner.nextLine().equals("elevation: 10")) {
                                assertTrue(true, "Passed");
                            } else {
                                fail();
                            }
                        } else {
                            fail();
                        }
                    } else {
                        fail();
                    }
                } else {
                    fail("The first line of the file was not exported correctly. Check if the values match");
                }
            } catch (IOException e) {
                fail("IOException should not be thrown");
            }
        } catch (SQLException e) {
            fail("SQLException thrown\n" + e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown\n" + e.getMessage());
        } catch (InvalidFileDataException e) {
            fail("InvalidFileDataException thrown\n" + e.getMessage());
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