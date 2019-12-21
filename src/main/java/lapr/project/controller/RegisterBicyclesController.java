package lapr.project.controller;

import lapr.project.data.registers.Company;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterBicyclesController {
    private static final Logger LOGGER = Logger.getLogger("registerBicyclesControllerLogger");

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

    public int registerBicycles(List<String[]> parsedData, String fileName) {
        List<Integer> weight = new ArrayList<>();
        List<Float> aerodynamicCoefficient = new ArrayList<>();
        List<Float> frontalArea = new ArrayList<>();
        List<Integer> size = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<Double> parkLatitude = new ArrayList<>();
        List<Double> parkLongitude = new ArrayList<>();
        int i = 0;
        try {
            for (i = 0; i < parsedData.size(); i++) {
                String[] line = parsedData.get(i);
                if (line.length == 1 && line[0].isEmpty())
                    continue;

                aerodynamicCoefficient.add(Float.parseFloat(line[BICYCLES_AERODYNAMIC_COEFFICIENT_INDEX]));
                weight.add(Integer.parseInt(line[BICYCLES_WEIGHT_INDEX]));
                frontalArea.add(Float.parseFloat(line[BICYCLES_FRONTAL_AREA_INDEX]));
                size.add(Integer.parseInt(line[BICYCLES_WHEEL_SIZE_INDEX]));
                description.add(line[BICYCLES_BICYCLE_DESCRIPTION_INDEX]);
                parkLatitude.add(Double.parseDouble(line[BICYCLES_PARK_LAT_INDEX]));
                parkLongitude.add(Double.parseDouble(line[BICYCLES_PARK_LON_INDEX]));
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid data at non-commented, non-empty line number " + i + " of the file " + fileName);
            return -1;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, "Not all columns are present at non-commented, non-empty line " + i + " of the file " + fileName);
            return -1;
        }

        try {
            company.getVehicleRegister().registerBicycles(aerodynamicCoefficient, frontalArea, weight,
                    size, description, parkLatitude, parkLongitude);
            return aerodynamicCoefficient.size();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to register the bicycles on the database");
            return -1;
        }
    }
}
