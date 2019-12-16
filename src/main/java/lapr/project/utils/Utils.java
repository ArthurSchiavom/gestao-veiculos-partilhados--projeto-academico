package lapr.project.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    private Utils() {}

    public static List<String[]> parseDataFile(String filePath, String valueSeparator, String lineCommentTag) throws FileNotFoundException {
        List<String[]> result = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith(lineCommentTag)) {
                    String[] lineValues = line.split(valueSeparator);
                    result.add(lineValues);
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        }

        return result;
    }

    public static <T> boolean areArraysEqual(T[] a1, T[] a2) {
        if (a1.length != a2.length)
            return false;

        for (int i = 0; i < a1.length; i++) {
            if (!a1[i].equals(a2[i]))
                return false;
        }
        return true;
    }
}
