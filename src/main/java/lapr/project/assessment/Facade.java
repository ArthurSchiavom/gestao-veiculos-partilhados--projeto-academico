package lapr.project.assessment;

import lapr.project.controller.UserController;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Facade implements Serviceable {
    private static final Logger LOGGER = Logger.getLogger("FacadeLogger");
    private final Company company;
    private UserController userController;

    public Facade() {
        this.company = Company.getInstance();
        userController = new UserController(company);
    }

    private List<String[]> loadParsedData(String filePath) {
        List<String[]> parsedData;
        try {
            parsedData = Utils.parseDataFile(filePath, ";", "#");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, () -> "Inexistent file: " + filePath);
            return null;
        }

        return parsedData;
    }

    private static final int BICYCLES_BICYCLE_DESCRIPTION_INDEX = 0;
    private static final int BICYCLES_WEIGHT_INDEX = 1;
    private static final int BICYCLES_PARK_LAT_INDEX = 2;
    private static final int BICYCLES_PARK_LON_INDEX = 3;
    private static final int BICYCLES_AERODYNAMIC_COEFFICIENT_INDEX = 4;
    private static final int BICYCLES_FRONTAL_AREA_INDEX = 5;
    private static final int BICYCLES_WHEEL_SIZE_INDEX = 6;

    private static final int USER_USERNAME = 0;
    private static final int USER_EMAIL = 1;
    private static final int USER_HEIGHT = 2;
    private static final int USER_WEIGHT = 3;
    private static final int USER_CYCLING_AVERAGE_SPEED = 4;
    private static final int USER_VISA = 5;
    private static final int USER_GENDER = 6;

    @Override
    public int addBicycles(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return -1;

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
            LOGGER.log(Level.SEVERE, "Invalid data at line number " + i + " of the file " + s);
            return -1;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, "Not all columns are present at line " + i + " of the file " + s);
            return -1;
        }

        try {
            return company.getVehicleRegister().registerBicycles(aerodynamicCoefficient, frontalArea, weight,
                     size, description, parkLatitude, parkLongitude);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to register the bicycles on the database");
            e.printStackTrace();
            return -1;
        }
    }

    private static final int SCOOTERS_DESCRIPTION_INDEX = 0;
    private static final int SCOOTERS_WEIGHT_INDEX = 1;
    private static final int SCOOTERS_TYPE_INDEX = 2;
    private static final int SCOOTERS_PARK_LAT_INDEX = 3;
    private static final int SCOOTERS_PARK_LON_INDEX = 4;
    private static final int SCOOTERS_MAX_BATTERY_CAPACITY_INDEX = 5;
    private static final int SCOOTERS_ACTUAL_BATTERY_CAPACITY_INDEX = 6;
    private static final int SCOOTERS_AERODYNAMIC_COEFFICIENT_INDEX = 7;
    private static final int SCOOTERS_FRONTAL_AREA_INDEX = 8;
    private static final int SCOOTERS_ENGINE_POWER_INDEX = 9;
    @Override
    public int addEscooters(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return -1;

        List<Float> aerodynamicCoefficient = new ArrayList<>();
        List<Float> frontalArea = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        List<ElectricScooterType> type = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<Float> maxBatteryCapacity = new ArrayList<>();
        List<Integer> actualBatteryCapacity = new ArrayList<>();
        List<Integer> enginePower = new ArrayList<>();
        List<Double> parkLatitude = new ArrayList<>();
        List<Double> parkLongitude = new ArrayList<>();

        int i = 0;
        try {
            for (i = 0; i < parsedData.size(); i++) {
                String[] line = parsedData.get(i);
                if (line.length == 1 && line[0].isEmpty())
                    continue;

                aerodynamicCoefficient.add(Float.parseFloat(line[SCOOTERS_AERODYNAMIC_COEFFICIENT_INDEX]));
                frontalArea.add(Float.parseFloat(line[BICYCLES_FRONTAL_AREA_INDEX]));
                weight.add(Integer.parseInt(line[SCOOTERS_WEIGHT_INDEX]));
                ElectricScooterType currentType = line[SCOOTERS_TYPE_INDEX].equalsIgnoreCase("city") ? ElectricScooterType.URBAN : ElectricScooterType.OFFROAD;
                type.add(currentType);
                description.add(line[SCOOTERS_DESCRIPTION_INDEX]);
                maxBatteryCapacity.add(Float.parseFloat(line[SCOOTERS_MAX_BATTERY_CAPACITY_INDEX]));
                actualBatteryCapacity.add(Integer.parseInt(line[SCOOTERS_ACTUAL_BATTERY_CAPACITY_INDEX]));
                enginePower.add(Integer.parseInt(line[SCOOTERS_ENGINE_POWER_INDEX]));
                parkLatitude.add(Double.parseDouble(line[SCOOTERS_PARK_LAT_INDEX]));
                parkLongitude.add(Double.parseDouble(line[SCOOTERS_PARK_LON_INDEX]));
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid data at line number " + i + " of the file " + s);
            return -1;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, "Not all columns are present at line " + i + " of the file " + s);
            return -1;
        }

        try {
            return company.getVehicleRegister().registerElectricScooters(aerodynamicCoefficient, frontalArea, weight, type, description, maxBatteryCapacity, actualBatteryCapacity, enginePower, parkLatitude, parkLongitude);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to register the scooters on the database");
            return -1;
        }
    }

    @Override
    public int addParks(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int removePark(String s) {
        return 0;
    }

    @Override
    public int addPOIs(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addUsers(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return -1;

        List<String> username = new ArrayList<>();
        List<String> email = new ArrayList<>();
        List<Integer> height = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        List<Float> cyclingAvgSpeed = new ArrayList<>();
        List<String> visa = new ArrayList<>();
        List<Character> gender = new ArrayList<>();
        List<String> password = new ArrayList<>();

        int i = 0;
        try {
            for (i = 0; i < parsedData.size(); i++) {
                String[] line = parsedData.get(i);
                if (line.length == 1 && line[0].isEmpty())
                    continue;

                username.add(line[USER_USERNAME]);
                email.add(line[USER_EMAIL]);
                height.add(Integer.parseInt(line[USER_HEIGHT]));
                weight.add(Integer.parseInt(line[USER_WEIGHT]));
                cyclingAvgSpeed.add(Float.parseFloat(line[USER_CYCLING_AVERAGE_SPEED]));
                visa.add(line[USER_VISA]);
                gender.add(line[USER_GENDER].charAt(0));
                password.add(line[SCOOTERS_ENGINE_POWER_INDEX]);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid data at line number " + i + " of the file " + s);
            return -1;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, "Not all columns are present at line " + i + " of the file " + s);
            return -1;
        }
        try {
            return userController.addUsers(email,username,height,weight,gender,visa,cyclingAvgSpeed);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to register the users on the database");
            return -1;
        }
    }

    @Override
    public int addPaths(String s) {
        return 0;
    }

    @Override
    public int getNumberOfBicyclesAtPark(double v, double v1, String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfBicyclesAtPark(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    /**
     * Distance is returns in metres, rounded to the unit e.g. (281,58 rounds
     * to 282);
     *
     * @param v  Latitude in degrees.
     * @param v1 Longitude in degrees.
     * @param s  Filename for output.
     */
    @Override
    public void getNearestParks(double v, double v1, String s) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(s))) {
            writer.write("41.152712,-8.609297,494");
            writer.newLine();
            writer.write("41.145883,-8.610680,282");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getNearestParks(double v, double v1, String s, int i) {

    }

    @Override
    public int getFreeBicycleSlotsAtPark(String s, String s1) {
        return 0;
    }

    @Override
    public int getFreeEscooterSlotsAtPark(String s, String s1) {
        return 0;
    }

    @Override
    public int getFreeSlotsAtParkForMyLoanedVehicle(String s) {
        return 0;
    }

    @Override
    public int linearDistanceTo(double v, double v1, double v2, double v3) {
        return 0;
    }

    @Override
    public int pathDistanceTo(double v, double v1, double v2, double v3) {
        return 0;
    }

    @Override
    public long unlockBicycle(String s, String s1) {
        return 0;
    }

    @Override
    public long unlockEscooter(String s, String s1) {
        return 0;
    }

    @Override
    public long lockBicycle(String s, double v, double v1, String s1) {
        return 0;
    }

    @Override
    public long lockBicycle(String s, String s1, String s2) {
        return 0;
    }

    @Override
    public long lockEscooter(String s, double v, double v1, String s1) {
        return 0;
    }

    @Override
    public long lockEscooter(String s, String s1, String s2) {
        return 0;
    }

    @Override
    public int registerUser(String s, String s1, String s2, int i, int i1, String s3) {
        return 0;
    }

    @Override
    public long unlockAnyEscootereAtPark(String s, String s1, String s2) {
        return 0;
    }

    @Override
    public long unlockAnyEscootereAtParkForDestination(String s, String s1, double v, double v1, String s2) {
        return 0;
    }

    @Override
    public int suggestEscootersToGoFromOneParkToAnother(String s, String s1, double v, double v1, String s2) {
        return 0;
    }

    @Override
    public long mostEnergyEfficientRouteBetweenTwoParks(String s, String s1, String s2, String s3, String s4, String s5) {
        return 0;
    }
}
