package lapr.project.utils;

import lapr.project.data.AutoCloseableManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Utils {
    private static final Logger LOGGER = Logger.getLogger("UtilsLog");


    private Utils() {}

    public static List<String[]> parseDataFile(String filePath, String valueSeparator, String lineCommentTag) throws FileNotFoundException {
        List<String[]> result = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith(lineCommentTag)) {
                    String[] lineValues = trimArrayElements(line.split(valueSeparator,-1));
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

    public static String[] trimArrayElements(String... elements) {
        String[] result = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            result[i] = elements[i].trim();
        }
        return result;
    }

    public static void writeToFile(List<String> lines, String fileName) throws IOException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            autoCloseableManager.addAutoCloseable(fileWriter);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            autoCloseableManager.addAutoCloseable(printWriter);
            for (String line : lines) {
                printWriter.println(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }
}
