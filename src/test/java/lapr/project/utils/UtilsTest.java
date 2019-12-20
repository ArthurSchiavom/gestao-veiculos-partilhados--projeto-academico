package lapr.project.utils;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

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

        try{
            List<String[]> result = Utils.parseDataFile("lol", ";", "#");
            fail("File not found exception not thrown");
        } catch (FileNotFoundException e) {
            assertTrue(true,"File not found exception thrown");
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
}
