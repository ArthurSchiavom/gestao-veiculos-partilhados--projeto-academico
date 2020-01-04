package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterBicyclesController {
    private static final int BICYCLES_BICYCLE_DESCRIPTION_INDEX = 0;
    private static final int BICYCLES_WEIGHT_INDEX = 1;
    private static final int BICYCLES_PARK_LAT_INDEX = 2;
    private static final int BICYCLES_PARK_LON_INDEX = 3;
    private static final int BICYCLES_AERODYNAMIC_COEFFICIENT_INDEX = 4;
    private static final int BICYCLES_FRONTAL_AREA_INDEX = 5;
    private static final int BICYCLES_WHEEL_SIZE_INDEX = 6;

    private final Company company;

    public RegisterBicyclesController(Company company) {
        this.company = company;
    }

    public int registerBicycles(String filePath) throws InvalidFileDataException, SQLException, FileNotFoundException {
        List<Float> frontalArea = new ArrayList<>();
        List<Integer> size = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        List<Double> parkLatitude = new ArrayList<>();
        List<Float> aerodynamicCoefficient = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<Double> parkLongitude = new ArrayList<>();
        int i = 0;
        try {
            List<String[]> parsedData = Utils.parseDataFileAndValidateHeader(filePath, ";", "#"
                    , "bicycle description;weight;park latitude;park longitude;aerodynamic coefficient;frontal area;wheel size");
            String[] line;

            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);

                aerodynamicCoefficient.add(Float.parseFloat(line[BICYCLES_AERODYNAMIC_COEFFICIENT_INDEX]));
                weight.add(Integer.parseInt(line[BICYCLES_WEIGHT_INDEX]));
                frontalArea.add(Float.parseFloat(line[BICYCLES_FRONTAL_AREA_INDEX]));
                size.add(Integer.parseInt(line[BICYCLES_WHEEL_SIZE_INDEX]));
                description.add(line[BICYCLES_BICYCLE_DESCRIPTION_INDEX]);
                parkLatitude.add(Double.parseDouble(line[BICYCLES_PARK_LAT_INDEX]));
                parkLongitude.add(Double.parseDouble(line[BICYCLES_PARK_LON_INDEX]));
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + filePath);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + filePath);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found - " + filePath);
        }

        company.getVehicleAPI().registerBicycles(aerodynamicCoefficient, frontalArea, weight, size, description, parkLatitude, parkLongitude);
        return aerodynamicCoefficient.size();

    }
}
