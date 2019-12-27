package lapr.project.controller;

import lapr.project.data.registers.Company;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterPathController {

    private final Company company;

    private static final int PATH_LAT_A = 0;
    private static final int PATH_LON_A = 1;
    private static final int PATH_LAT_B = 2;
    private static final int PATH_LON_B = 3;
    private static final int PATH_KINETIC_COEFFICIENT = 4;
    private static final int PATH_WIND_DIRECTION = 5;
    private static final int PATH_WIND_SPEED = 6;

    public RegisterPathController(Company company) {
        this.company = company;
    }

    /**
     * @return the number of users added
     */
    public int registerPaths(List<String[]> parsedData, String fileName) throws SQLException, InvalidFileDataException {
        String[] line = parsedData.get(0);
        if (line.length != 7 || !line[0].equals("latitudeA") || !line[1].equals("longitudeA") || !line[2].equals("latitudeB")
                || !line[3].equals("longitudeB") || !line[4].equals("kinetic coefficient") || !line[5].equals("wind direction")
                || !line[6].equals("wind speed"))
            throw new InvalidFileDataException("Header is different from expected");

        List<Double> latA = new ArrayList<>();
        List<Double> lonA = new ArrayList<>();
        List<Double> latB = new ArrayList<>();
        List<Double> lonB = new ArrayList<>();
        List<Double> kineticCoefficient = new ArrayList<>();
        List<Integer> windDirection = new ArrayList<>();
        List<Double> windSpeed = new ArrayList<>();

        int i = 0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);
                if (line.length == 1 && line[0].isEmpty())
                    continue;

                latA.add(Double.parseDouble(line[PATH_LAT_A]));
                lonA.add(Double.parseDouble(line[PATH_LON_A]));
                latB.add(Double.parseDouble(line[PATH_LAT_B]));
                lonB.add(Double.parseDouble(line[PATH_LON_B]));
                if(line[PATH_KINETIC_COEFFICIENT].isEmpty()){
                    kineticCoefficient.add(0.0); // in the input folder it says if there's no kin coef, to assume that it is 0
                }else{
                    kineticCoefficient.add(Double.parseDouble(line[PATH_KINETIC_COEFFICIENT]));
                }
                if(line[PATH_WIND_DIRECTION].isEmpty()){
                    windDirection.add(0); // in the input folder it says if there's no wind direction, to assume that it is 0
                }else{
                    windDirection.add(Integer.parseInt(line[PATH_WIND_DIRECTION]));
                }
                if(line[PATH_WIND_SPEED].isEmpty()){
                    windSpeed.add(0.0); // in the input folder it says if there's no wind speed, to assume that it is 0
                }else{
                    windSpeed.add(Double.parseDouble(line[PATH_WIND_SPEED]));
                }
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + fileName);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + fileName);
        }
        try {
            return company.getPathRegister().insertPaths(latA,lonA,latB,lonB,kineticCoefficient,windDirection,windSpeed);
        } catch (SQLException e) {
            throw new SQLException("Failed to write data to the database");
        }
    }
}
