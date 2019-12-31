package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.utils.InvalidFileDataException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterParksController {
    private final Company company;

    private static final int ID_INDEX = 0;
    private static final int LATITUDE_INDEX = 1;
    private static final int LONGITUDE_INDEX = 2;
    private static final int ELEVATION_INDEX = 3;
    private static final int DESCRIPTION_INDEX = 4;
    private static final int MAX_NUM_BIKES_INDEX = 5;
    private static final int MAX_NUM_SCOOTERS_INDEX = 6;
    private static final int INPUT_VOLTAGE_INDEX = 7;
    private static final int INPUT_CURRENT_INDEX = 8;

    public RegisterParksController(Company company) {
        this.company = company;
    }

    public int registerParks(List<String[]> parsedData, String fileName) throws InvalidFileDataException, SQLException {
        String[] line = parsedData.get(0);
        if (line.length != 9 || !line[0].equals("park identification") || !line[1].equals("latitude") || !line[2].equals("longitude")
                || !line[3].equals("elevation") || !line[4].equals("park description") || !line[5].equals("max number of bicycles")
                || !line[6].equals("max number of escooters") || !line[7].equals("park input voltage")
                || !line[8].equals("park input current"))
            throw new InvalidFileDataException("Header is different from expected");

        List<String> id = new ArrayList<>();
        List<Coordinates> coordinates = new ArrayList<>();
        List<Integer> maxNumBikes = new ArrayList<>();
        List<Float> inputVoltage = new ArrayList<>();
        List<Integer> maxNumScooters = new ArrayList<>();
        List<Float> inputCurrent = new ArrayList<>();
        List<String> description = new ArrayList<>();
        int i = 0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);
                if (line.length == 1 && line[0].isEmpty())
                    continue;

                id.add(parsedData.get(i)[ID_INDEX]);
                description.add(parsedData.get(i)[DESCRIPTION_INDEX]);
                double latitude = Double.parseDouble(parsedData.get(i)[LATITUDE_INDEX]);
                double longitude = Double.parseDouble(parsedData.get(i)[LONGITUDE_INDEX]);
                String elevationS = parsedData.get(i)[ELEVATION_INDEX];
                int elevationI;
                if (elevationS.isEmpty())
                    elevationI = 0; // Default if not specified
                else
                    elevationI = Integer.parseInt(elevationS);
                coordinates.add(new Coordinates(latitude, longitude, elevationI));
                maxNumBikes.add(Integer.parseInt(parsedData.get(i)[MAX_NUM_BIKES_INDEX]));
                maxNumScooters.add(Integer.parseInt(parsedData.get(i)[MAX_NUM_SCOOTERS_INDEX]));
                inputVoltage.add(Float.parseFloat(parsedData.get(i)[INPUT_VOLTAGE_INDEX]));
                inputCurrent.add(Float.parseFloat(parsedData.get(i)[INPUT_CURRENT_INDEX]));
            }
        } catch (NumberFormatException e) {
            ;throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + fileName);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + fileName);
        }

        try {
            company.getParkAPI().registerParks(id, description, coordinates, inputVoltage, inputCurrent, maxNumScooters, maxNumBikes);
            return id.size();
        } catch (SQLException e) {
            throw new SQLException("Failed to write data to the database: \n" + e.getMessage());
        }
    }
}
