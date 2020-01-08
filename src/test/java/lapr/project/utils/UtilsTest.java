package lapr.project.utils;

import lapr.project.controller.UnlockVehicleController;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilsTest {

    @Test
    void parseDataFileTest() {
        try {
            List<String[]> result = Utils.parseDataFile("UtilsTest.data", ";", "#");
            String[] line = {"Data1", "asoudhwData2", "Data3"};
            String[] line2 = {"Data4", "asoudhwData5", "Data6"};
            String[] line3 = {"Data7", "asoudhwData8", "Data9"};
            Utils.areArraysEqual(line, result.get(0));
            Utils.areArraysEqual(line2, result.get(1));
            Utils.areArraysEqual(line3, result.get(2));
        } catch (FileNotFoundException e) {
            fail();
        }

        try {
            List<String[]> result = Utils.parseDataFile("lol", ";", "#");
            fail("File not found exception not thrown");
        } catch (FileNotFoundException e) {
            assertTrue(true, "File not found exception thrown");
        }
    }

    @Test
    void areArraysEqualTest() {
        String[] a1 = {"Data1", "asoudhwData2", "Data3"};
        String[] a2 = {"Data4", "asoudhwData5", "Data6"};
        assertFalse(Utils.areArraysEqual(a1, a2));

        String[] a3 = {"Data1", "asoudhwData2", "Data3"};
        String[] a4 = {"Data1", "asoudhwData5", "Data3"};
        assertFalse(Utils.areArraysEqual(a3, a4));
        assertFalse(Utils.areArraysEqual(a4, a3));

        String[] a5 = {"Data1", "asoudhwData2", "Data3"};
        String[] a6 = {"Data1", "asoudhwData2", "Daa3"};
        assertFalse(Utils.areArraysEqual(a5, a6));

        String[] a7 = {"Data1", "asoudhwData2", "Data3", "asd"};
        String[] a8 = {"Data1", "asoudhwData2", "Data3"};
        assertFalse(Utils.areArraysEqual(a7, a8));

        assertFalse(Utils.areArraysEqual(a8, a7));

        String[] a9 = {"Data1", "asoudhwData2", "Data3"};
        String[] a10 = {"Data1", "asoudhwData2", "Data3"};
        assertTrue(Utils.areArraysEqual(a9, a10));
    }

    @Test
    void trimArrayElementsTest() {
        String[] arr = {};
        Utils.trimArrayElements(arr);
        String[] result = Utils.trimArrayElements("a a a   a   ", "bbbb", " ccc ccc   ");
        String[] expected = {"a a a   a", "bbbb", "ccc ccc"};
        assertTrue(Utils.areArraysEqual(result, expected));
    }

    @Test
    void writeToFileTest() {
        String filePath = "testFiles/temp/UtilsWriteToFileTest.output";
        List<String> lines = new ArrayList<>();

        // #1
        try {
            Utils.writeToFile(lines, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            if (scanner.hasNext() && !scanner.nextLine().isEmpty())
                fail();
            //else pass
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }


        // #2
        lines.add("abc");
        lines.add("");
        lines.add("   ");
        lines.add(" avb as sadkdjoaisn   idsajodijsao jdsaoijdoas jo");

        try {
            Utils.writeToFile(lines, filePath);
        } catch (IOException e) {
            fail();
        }

        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            int count = 0;
            while (scanner.hasNext()) {
                assertEquals(lines.get(count), scanner.nextLine());
                count++;
            }
            if (count != lines.size())
                fail();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void parseDataFileAndValidateHeaderTest() {
        try {
            List<String[]> result = null;
            try {
                result = Utils.parseDataFileAndValidateHeader("testFiles/parseDataFileAndValidateHeaderTest.txt", ";", "#", "someHeader");
            } catch (InvalidFileDataException e) {
                fail();
            }
            String[] line = {"Data1", "asoudhwData2", "Data3"};
            String[] line2 = {"Data4", "asoudhwData5", "Data6"};
            String[] line3 = {"Data7", "asoudhwData8", "Data9"};
            Utils.areArraysEqual(line, result.get(0));
            Utils.areArraysEqual(line2, result.get(1));
            Utils.areArraysEqual(line3, result.get(2));
        } catch (FileNotFoundException e) {
            fail();
        }

        try {
            Utils.parseDataFileAndValidateHeader("testFiles/parseDataFileAndValidateHeaderTestInvalid.txt", ";", "#", "someHeader");
            fail();
        } catch (InvalidFileDataException e) {
            // pass;
        } catch (Exception e) {
            fail();
        }

        try {
            List<String[]> result = Utils.parseDataFile("fail", ";", "#");
            fail("File not found exception not thrown");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void calculateDistanceInMeters() {
        LinkedList<PointOfInterest> path = new LinkedList<>();
        path.add(new PointOfInterest("123", new Coordinates(2.3, 3.4, 5)));
        path.add(new PointOfInterest("321", new Coordinates(2.3, 3.4, 5)));
        assertEquals(0, Utils.calculateDistanceInMeters(path));
    }

    @Test
    void calculateDistanceInMeter2() {
        LinkedList<PointOfInterest> path = new LinkedList<>();
        path.add(new PointOfInterest("123", new Coordinates(2.3, 3.4, 5)));
        path.add(new PointOfInterest("321", new Coordinates(2.31, 3.41, 5)));
        assertEquals(1572, Utils.calculateDistanceInMeters(path));
    }

    @Test
    void testGetOutputPaths() {
        Locale.setDefault(Locale.US);

        DataHandler dataHandler = mock(DataHandler.class);
        Company.reset();
        Company.createCompany(dataHandler);

        try {
            PreparedStatement stmFetchPOI = mock(PreparedStatement.class);
            ResultSet rsFetchPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest")).thenReturn(stmFetchPOI);
            when(dataHandler.executeQuery(stmFetchPOI)).thenReturn(rsFetchPOI);
            when(rsFetchPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchPOI.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6);
            when(rsFetchPOI.getDouble("longitude")).thenReturn(3.4).thenReturn(3.41).thenReturn(4.4).thenReturn(4.41);
            when(rsFetchPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4");
            when(rsFetchPOI.getDouble("latitude")).thenReturn(2.3).thenReturn(2.31).thenReturn(3.3).thenReturn(3.41);

            PreparedStatement stmFetchAllPaths = mock(PreparedStatement.class);
            ResultSet rsFetchAllPaths = mock(ResultSet.class);
            when(dataHandler.prepareStatement("SELECT * FROM paths")).thenReturn(stmFetchAllPaths);
            when(dataHandler.executeQuery(stmFetchAllPaths)).thenReturn(rsFetchAllPaths);
            when(rsFetchAllPaths.next()).thenReturn(true).thenReturn(true).thenReturn(false);
            when(rsFetchAllPaths.getDouble("longitudeA")).thenReturn(3.4).thenReturn(4.4);
            when(rsFetchAllPaths.getDouble("latitudeA")).thenReturn(2.3).thenReturn(3.3);
            when(rsFetchAllPaths.getDouble("latitudeB")).thenReturn(2.31).thenReturn(3.31);
            when(rsFetchAllPaths.getDouble("longitudeB")).thenReturn(3.41).thenReturn(4.41);
            when(rsFetchAllPaths.getDouble("kinetic_coefficient")).thenReturn(0.002).thenReturn(0.002);
            when(rsFetchAllPaths.getInt("wind_direction_degrees")).thenReturn(90).thenReturn(90);
            when(rsFetchAllPaths.getDouble("wind_speed")).thenReturn(15.6).thenReturn(15.6);

            PreparedStatement stmFetchCertainPOI = mock(PreparedStatement.class);
            ResultSet rsFetchCertainPOI = mock(ResultSet.class);

            when(dataHandler.prepareStatement("SELECT * FROM points_of_interest WHERE latitude =? and longitude =?")).thenReturn(stmFetchCertainPOI);
            when(dataHandler.executeQuery(stmFetchCertainPOI)).thenReturn(rsFetchCertainPOI);
            when(rsFetchCertainPOI.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true);
            when(rsFetchCertainPOI.getInt("altitude_m")).thenReturn(5).thenReturn(5).thenReturn(6).thenReturn(6);
            when(rsFetchCertainPOI.getString("poi_description")).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4");
        } catch (SQLException e) {
            fail("SQLException thrown\n" + e.getMessage());
        }

        List<LinkedList<PointOfInterest>> paths = new LinkedList<>();
        LinkedList<PointOfInterest> path1 = new LinkedList<>();
        LinkedList<PointOfInterest> path2 = new LinkedList<>();
        path1.add(new PointOfInterest("1", new Coordinates(2.3, 3.4, 5)));
        path1.add(new PointOfInterest("2", new Coordinates(2.31, 3.41, 5)));
        path2.add(new PointOfInterest("3", new Coordinates(3.3, 4.4, 6)));
        path2.add(new PointOfInterest("4", new Coordinates(3.31, 4.41, 6)));
        paths.add(path1);
        List<String> outputExpected = new LinkedList<>();
        List<String> output = new LinkedList<>();
        outputExpected.add("Path 001\ntotal distance: 5\ntotal energy: 3.00\nelevation: 10\n2.3;3.4\n2.31;3.41\n");
        outputExpected.add("Path 002\ntotal distance: 5\ntotal energy: 3.00\nelevation: 10\n3.3;4.4\n3.31;4.41\n");
        output = Utils.getOutputPaths(paths, 5, 10, new Client("a@a.a", "a", "123", 2, 50, 'M', 25f,
                false, new CreditCard("200123456")), new ElectricScooter(1, "PT001"
                , 1f, 1f, 10, true, ElectricScooterType.URBAN, 75
                , 1f, 500));
    }

    @Test
    void getOutputPathTest() {
        Locale.setDefault(Locale.US);

        LinkedList<PointOfInterest> path = new LinkedList<>();
        path.add(new PointOfInterest("123", new Coordinates(2.3, 3.4, 5)));
        path.add(new PointOfInterest("321", new Coordinates(2.31, 3.41, 5)));
        List<String> outputExpected = new LinkedList<>();
        List<String> output = new LinkedList<>();
        outputExpected.add("Path 002\ntotal distance: 5\ntotal energy: 3.00\nelevation: 10\n2.3;3.4\n2.31;3.41\n");
        long distance = 5;
        int elevation = 13;
        int pathNumber = 2;
        long energy = 3;
        Utils.getOutputPath(path, output, distance, energy, elevation, pathNumber);
        assertEquals(outputExpected, output);
    }

    @Test
    void sortPois() {
        LinkedList<PointOfInterest> path = new LinkedList<>();
        path.add(new PointOfInterest("123", new Coordinates(2.3, 3.4, 5)));
        path.add(new PointOfInterest("321", new Coordinates(2.31, 3.41, 6)));

        LinkedList<PointOfInterest> pathCopy = new LinkedList<>();
        pathCopy.add(new PointOfInterest("123", new Coordinates(2.3, 3.4, 5)));
        pathCopy.add(new PointOfInterest("321", new Coordinates(2.31, 3.41, 6)));
        List<LinkedList<PointOfInterest>> paths = new LinkedList<>();
        paths.add(path);

        LinkedList<PointOfInterest> pathSorted = new LinkedList<>();
        pathSorted.add(new PointOfInterest("321", new Coordinates(2.31, 3.41, 6)));
        pathSorted.add(new PointOfInterest("123", new Coordinates(2.3, 3.4, 5)));

        Utils.sort(paths);
        assertEquals(paths.get(0), pathSorted);
        assertNotEquals(paths.get(0), pathCopy);
    }

    @Test
    void howManyTimesBfitsIntoAPositiveTest() {
        assertEquals(5, Utils.howManyTimesBfitsIntoAPositive(5, 1));
        assertEquals(11, Utils.howManyTimesBfitsIntoAPositive(57, 5));
        assertEquals(0, Utils.howManyTimesBfitsIntoAPositive(5, 10));

        try {
            Utils.howManyTimesBfitsIntoAPositive(-5, 10);
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
        try {
            Utils.howManyTimesBfitsIntoAPositive(5, -10);
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
        try {
            Utils.howManyTimesBfitsIntoAPositive(0, 0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Passed");
        }
    }

    @Test
    void pointsToEurosTest() {
        assertEquals(10, Utils.pointsToEuros(100));
    }

    @Test
    void eurosToPointsTest() {
        assertEquals(100, Utils.eurosToPoints(10));
    }
}
