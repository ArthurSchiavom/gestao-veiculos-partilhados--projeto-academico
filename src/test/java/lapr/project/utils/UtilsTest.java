package lapr.project.utils;

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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
        path.add(new PointOfInterest("123",new Coordinates(2.3,3.4,5)));
        path.add(new PointOfInterest("321",new Coordinates(2.3,3.4,5)));
        assertEquals(0,Utils.calculateDistanceInMeters(path));
    }

    @Test
    void calculateDistanceInMeter2() {
        LinkedList<PointOfInterest> path = new LinkedList<>();
        path.add(new PointOfInterest("123",new Coordinates(2.3,3.4,5)));
        path.add(new PointOfInterest("321",new Coordinates(2.31,3.41,5)));
        assertEquals(1572,Utils.calculateDistanceInMeters(path));
    }

    @Test
    void getOutputPathTest(){
        Locale.setDefault(Locale.US);

        LinkedList<PointOfInterest> path = new LinkedList<>();
        path.add(new PointOfInterest("123",new Coordinates(2.3,3.4,5)));
        path.add(new PointOfInterest("321",new Coordinates(2.31,3.41,5)));
        List<String> outputExpected = new LinkedList<>();
        List<String> output = new LinkedList<>();
        outputExpected.add("Path 002\ntotal distance: 5\ntotal energy: 3.00\nelevation: 10\n2.3;3.4\n2.31;3.41\n");
        long distance=5;
        int elevation = 13;
        int pathNumber = 2;
        long energy = 3;
        Utils.getOutputPath(path,output,distance,energy,elevation,pathNumber);
        assertEquals(outputExpected,output);
    }

    @Test
    void sortPois(){
        LinkedList<PointOfInterest> path = new LinkedList<>();
        path.add(new PointOfInterest("123",new Coordinates(2.3,3.4,5)));
        path.add(new PointOfInterest("321",new Coordinates(2.31,3.41,6)));

        LinkedList<PointOfInterest> pathCopy = new LinkedList<>();
        pathCopy.add(new PointOfInterest("123",new Coordinates(2.3,3.4,5)));
        pathCopy.add(new PointOfInterest("321",new Coordinates(2.31,3.41,6)));
        List<LinkedList<PointOfInterest>> paths = new LinkedList<>();
        paths.add(path);

        LinkedList<PointOfInterest> pathSorted = new LinkedList<>();
        pathSorted.add(new PointOfInterest("321",new Coordinates(2.31,3.41,6)));
        pathSorted.add(new PointOfInterest("123",new Coordinates(2.3,3.4,5)));

        Utils.sort(paths);
        assertEquals(paths.get(0),pathSorted);
        assertNotEquals(paths.get(0),pathCopy);
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
