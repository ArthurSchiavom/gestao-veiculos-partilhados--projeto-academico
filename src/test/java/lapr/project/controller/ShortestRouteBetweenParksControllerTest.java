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
    void shortestRouteBetweenTwoParksFetchByCoordinatesSmall() {
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
            long result = controller.shortestRouteBetweenTwoParksFetchByCoordinates(2.3
                    , 3.4, 2.31, 3.41);
            assertEquals(expResult, result);
        } catch (SQLException e) {
            fail("SQLException thrown\n" + e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown\n" + e.getMessage());
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
            long result = controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(2.3, 3.4, 2.31, 3.41, "testFiles/testShortestPathByCoordinates.txt", "testFiles/outputShortestPathByCoordinates.txt");
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
            } catch (InvalidFileDataException e) {
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
            long result = controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(2.3, 3.4, 2.31, 3.41, "testFiles/testShortestPathByCoordinates.txt", "testFiles/outputShortestPathByCoordinates.txt");
            assertEquals(expResult, result);
            try (Scanner scanner = new Scanner(new FileReader("testFiles/outputShortestPathByCoordinates.txt"))) {
                if (scanner.hasNextLine() && scanner.nextLine().equals("Path 001")) {
                    assertTrue(true, "Correctly found the scooter in the file");
                    if (scanner.hasNextLine() && scanner.nextLine().equals("total distance: 26105660")) {
                        if (scanner.hasNextLine()) {
                            System.out.println(scanner.nextLine());
                            if (scanner.hasNextLine() && scanner.nextLine().equals("elevation: 10")) {
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
    void shortestRouteBetweenTwoParksFetchByCoordinates1() {
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

            long expResult = 26670910;
            long result = controller.shortestRouteBetweenTwoParksFetchByCoordinates(2.3, 3.4, 2.31, 3.41, 1, "outputtyBoy.txt");
            assertEquals(expResult, result);
        } catch (SQLException | IOException e) {
            fail("SQLException thrown\n" + e.getMessage());
        }
    }


    @Test
    void shortestRouteBetweenTwoParksFetchByID() {
        try {
            controller.shortestRouteBetweenTwoParksFetchByID("uhm", "wot", 2, "outputWTF.txt");
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
            controller.shortestRouteBetweenTwoParksAndGivenPoisFetchById("uhm", "wot", "ad.txt", "outputWTF.txt");
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
            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(false).thenReturn(false);
            when(rsFetchPOI.getInt("altitude_m")).thenReturn(0);
            when(rsFetchPOI.getDouble("latitude")).thenReturn(0.0);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(0.0);
            when(rsFetchPOI.getString("poi_description")).thenReturn("");

            Utils.writeToFile(new ArrayList<>(), "testFiles/outputShortestRouteByCoordinatesNumPoiEmpty.output");
            assertEquals(0, controller.shortestRouteBetweenTwoParksFetchByCoordinates(10,
                    20, 30, 40, 2,
                    "testFiles/outputShortestRouteByCoordinatesNumPoiEmpty.output"));
            try(Scanner scanner = new Scanner(new FileReader("testFiles/outputShortestRouteByCoordinatesNumPoiEmpty.output"))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("Não existem pontos de interesse com as coordenadas fornecidas")) {
                    assertTrue(true,"Passed");
                } else {
                    fail("Fail message didn't work");
                }
            }
        } catch (SQLException e) {
            fail("SQLException thrown");
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    void testShortestRouteBetweenTwoParksFetchByCoordinatesCantFind() {
        try {
            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true);
            when(rsFetchPOI.getDouble("latitude")).thenReturn(10.0).thenReturn(30.0);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(20.0).thenReturn(40.0);
            when(rsFetchPOI.getDouble("altitude_m")).thenReturn(5.0).thenReturn(10.0);
            when(rsFetchPOI.getString("poi_description")).thenReturn("Trindade").thenReturn("Ribeira");

            PreparedStatement stmFetchAllPOI = mock(PreparedStatement.class);
            ResultSet rsFetchAllPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchAllPOI);
            when(dataHandler.executeQuery(stmFetchAllPOI)).thenReturn(rsFetchAllPOI);
            when(rsFetchAllPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPOI.getDouble("latitude")).thenReturn(10.0).thenReturn(30.0).thenReturn(50.0).thenReturn(70.0);
            when(rsFetchAllPOI.getDouble("longitude")).thenReturn(20.0).thenReturn(40.0).thenReturn(60.0).thenReturn(80.0);
            when(rsFetchAllPOI.getInt("altitude_m")).thenReturn(5).thenReturn(10).thenReturn(15).thenReturn(25);
            when(rsFetchAllPOI.getString("poi_description")).thenReturn("Trindade").thenReturn("Ribeira")
                    .thenReturn("Paranhos").thenReturn("Maia");

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(10.0).thenReturn(30.0).thenReturn(50.0).thenReturn(80.0).thenReturn(10.0);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(20.0).thenReturn(40.0).thenReturn(60.0).thenReturn(90.0).thenReturn(20.0);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(30.0).thenReturn(50.0).thenReturn(70.0).thenReturn(10.0).thenReturn(50.0);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(40.0).thenReturn(60.0).thenReturn(80.0).thenReturn(20.0).thenReturn(60.0);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6);

            PreparedStatement stmFetchStartingAltitude = mock(PreparedStatement.class);
            ResultSet rsFetchStartingAltitude = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchStartingAltitude);
            when(dataHandler.executeQuery(stmFetchStartingAltitude)).thenReturn(rsFetchStartingAltitude);
            when(rsFetchStartingAltitude.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true)
                    .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchStartingAltitude.getInt("altitude_m")).thenReturn(5).thenReturn(10)
                    .thenReturn(10).thenReturn(15)
                    .thenReturn(15).thenReturn(20)
                    .thenReturn(20).thenReturn(5)
                    .thenReturn(5).thenReturn(10);
            when(rsFetchStartingAltitude.getString("poi_description")).thenReturn("Trindade").thenReturn("Ribeira")
                    .thenReturn("Ribeira").thenReturn("Paranhos")
                    .thenReturn("Paranhos").thenReturn("Maia")
                    .thenReturn("Maia").thenReturn("Trindade")
                    .thenReturn("Trindade").thenReturn("Paranhos");

            Utils.writeToFile(new ArrayList<>(), "testFiles/outputShortestRouteByCoordinatesNum.output");
            assertEquals(0, controller.shortestRouteBetweenTwoParksFetchByCoordinates(10,
                    20, 30, 40, 2,
                    "testFiles/outputShortestRouteByCoordinatesNum.output"));
            try(Scanner scanner = new Scanner(new FileReader("testFiles/outputShortestRouteByCoordinatesNum.output"))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("Não existem caminhos com esse tamanho")) {
                    assertTrue(true, "yey");
                } else {
                    fail("first line failed");
                }
            } catch (IOException e) {
                fail("IOException thrown");
            }
        } catch (SQLException e) {
            fail("SQLException thrown");
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @TestShort
    void testShortestRouteBetweenTwoParksFetchByCoordinates1() {
        try {
            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest where latitude=? AND longitude =?")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true);
            when(rsFetchPOI.getDouble("latitude")).thenReturn(10.0).thenReturn(80.0);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(20.0).thenReturn(90.0);
            when(rsFetchPOI.getDouble("altitude_m")).thenReturn(5.0).thenReturn(20.0);
            when(rsFetchPOI.getString("poi_description")).thenReturn("Trindade").thenReturn("Maia");

            PreparedStatement stmFetchAllPOI = mock(PreparedStatement.class);
            ResultSet rsFetchAllPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchAllPOI);
            when(dataHandler.executeQuery(stmFetchAllPOI)).thenReturn(rsFetchAllPOI);
            when(rsFetchAllPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPOI.getDouble("latitude")).thenReturn(10.0).thenReturn(30.0).thenReturn(50.0).thenReturn(70.0);
            when(rsFetchAllPOI.getDouble("longitude")).thenReturn(20.0).thenReturn(40.0).thenReturn(60.0).thenReturn(80.0);
            when(rsFetchAllPOI.getInt("altitude_m")).thenReturn(5).thenReturn(10).thenReturn(15).thenReturn(25);
            when(rsFetchAllPOI.getString("poi_description")).thenReturn("Trindade").thenReturn("Ribeira")
                    .thenReturn("Paranhos").thenReturn("Maia");

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(10.0).thenReturn(30.0).thenReturn(50.0).thenReturn(80.0).thenReturn(10.0);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(20.0).thenReturn(40.0).thenReturn(60.0).thenReturn(90.0).thenReturn(20.0);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(30.0).thenReturn(50.0).thenReturn(70.0).thenReturn(10.0).thenReturn(50.0);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(40.0).thenReturn(60.0).thenReturn(80.0).thenReturn(20.0).thenReturn(60.0);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6);

            PreparedStatement stmFetchStartingAltitude = mock(PreparedStatement.class);
            ResultSet rsFetchStartingAltitude = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchStartingAltitude);
            when(dataHandler.executeQuery(stmFetchStartingAltitude)).thenReturn(rsFetchStartingAltitude);
            when(rsFetchStartingAltitude.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true)
                    .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchStartingAltitude.getInt("altitude_m")).thenReturn(5).thenReturn(10)
                    .thenReturn(10).thenReturn(15)
                    .thenReturn(15).thenReturn(20)
                    .thenReturn(20).thenReturn(5)
                    .thenReturn(5).thenReturn(10);
            when(rsFetchStartingAltitude.getString("poi_description")).thenReturn("Trindade").thenReturn("Ribeira")
                    .thenReturn("Ribeira").thenReturn("Paranhos")
                    .thenReturn("Paranhos").thenReturn("Maia")
                    .thenReturn("Maia").thenReturn("Trindade")
                    .thenReturn("Trindade").thenReturn("Paranhos");

            Utils.writeToFile(new ArrayList<>(), "testFiles/outputShortestRouteByCoordinatesNum.output");
            assertEquals(9345009, controller.shortestRouteBetweenTwoParksFetchByCoordinates(10,
                    20, 80, 90, 2,
                    "testFiles/outputShortestRouteByCoordinatesNum.output"));
            try(Scanner scanner = new Scanner(new FileReader("testFiles/outputShortestRouteByCoordinatesNum.output"))) {
                if(scanner.hasNextLine() && scanner.nextLine().equals("Path 001")) {
                    if(scanner.hasNextLine() && scanner.nextLine().equals("total distance: 9345009")) {
                        if(scanner.hasNextLine() && scanner.nextLine().equals("total energy: 4897,86")) {
                            if(scanner.hasNextLine() && scanner.nextLine().equals("elevation: 0")) {
                                if(scanner.hasNextLine() && scanner.nextLine().equals("10.0;20.0")) {
                                    if(scanner.hasNextLine() && scanner.nextLine().equals("30.0;40.0")) {
                                        if(scanner.hasNextLine() && scanner.nextLine().equals("50.0;60.0")) {
                                            if(scanner.hasNextLine() && scanner.nextLine().equals("80.0;90.0")) {
                                                assertTrue(true,"Omg help me");
                                            } else {
                                                fail("eigth line failed");
                                            }
                                        } else {
                                            fail("seventh line failed");
                                        }
                                    } else {
                                        fail("sixth line failed");
                                    }
                                } else {
                                    fail("fifth line failed");
                                }
                            } else {
                                fail("fourth line failed");
                            }
                        } else {
                            fail("third line failed");
                        }
                    } else {
                        fail("second line failed");
                    }
                } else {
                    fail("first line failed");
                }
            } catch (IOException e) {
                fail("IOException thrown");
            }
        } catch (SQLException e) {
            fail("SQLException thrown");
        } catch (IOException e) {
            fail("IOException thrown");
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
            long distance = controller.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(18.222,20.12,18.222,22.12,"testFiles/uhm.txt","outputWTF.txt");
            assertEquals(0,distance);
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
            controller.shortestRouteBetweenTwoParksFetchById("1", "2", "outputWTF.txt");
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
            controller.shortestRouteBetweenTwoParksAndGivenPoisFetchById("1", "2", "testFiles/uhm.txt", "outputWTF.txt");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass() != NullPointerException.class)
                fail();
        }
    }
}