package lapr.project.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    public static List<String[]> parseDataFile(String filePath, String valueSeparator) throws FileNotFoundException {
        List<String[]> result = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(filePath));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineValues = line.split(valueSeparator);
            result.add(lineValues);
        }

        return result;
    }
}
