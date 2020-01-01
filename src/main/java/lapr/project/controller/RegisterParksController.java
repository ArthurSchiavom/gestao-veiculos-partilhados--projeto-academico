package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;

import java.io.FileNotFoundException;
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

    public int registerParks(String filePath) throws InvalidFileDataException, SQLException, FileNotFoundException {
        List<String[]> parsedData = Utils.parseDataFileAndValidateHeader(filePath, ";", "#"
                , "park identification;latitude;longitude;elevation;park description;max number of bicycles;max number of escooters;park input voltage;park input current");
        String[] line;

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
            ;throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + filePath);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + filePath);
        }

        try {
            company.getParkAPI().registerParks(id, description, coordinates, inputVoltage, inputCurrent, maxNumScooters, maxNumBikes);
            return id.size();
        } catch (SQLException e) {
            throw new SQLException("Failed to write data to the database: \n" + e.getMessage());
        }
    }
}
