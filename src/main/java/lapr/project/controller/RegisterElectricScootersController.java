package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooterType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterElectricScootersController {
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

    private final Company company;

    public RegisterElectricScootersController(Company company) {
        this.company = company;
    }

    public int registerElectricScooters(List<String[]> parsedData, String fileName) throws InvalidFileDataException, SQLException {
        String[] line = parsedData.get(0);
        if (line.length != 10 || !line[0].equals("escooter description") || !line[1].equals("weight") || !line[2].equals("type")
                || !line[3].equals("park latitude") || !line[4].equals("park longitude") || !line[5].equals("max battery capacity")
                || !line[6].equals("actual battery capacity") || !line[7].equals("aerodynamic coefficient")
                || !line[8].equals("frontal area") || !line[9].equals("motor"))
            throw new InvalidFileDataException("Header is different from expected");

        List<Float> aerodynamicCoefficient = new ArrayList<>();
        List<Float> frontalArea = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        List<ElectricScooterType> type = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<Float> maxBatteryCapacity = new ArrayList<>();
        List<Integer> actualBatteryCapacity = new ArrayList<>();
        List<Double> parkLatitude = new ArrayList<>();
        List<Integer> enginePower = new ArrayList<>();
        List<Double> parkLongitude = new ArrayList<>();

        int i = 0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);
                if (line.length == 1 && line[0].isEmpty())
                    continue;

                weight.add(Integer.parseInt(line[SCOOTERS_WEIGHT_INDEX]));
                aerodynamicCoefficient.add(Float.parseFloat(line[SCOOTERS_AERODYNAMIC_COEFFICIENT_INDEX]));
                frontalArea.add(Float.parseFloat(line[SCOOTERS_FRONTAL_AREA_INDEX]));
                ElectricScooterType currentType = line[SCOOTERS_TYPE_INDEX].equalsIgnoreCase("city") ? ElectricScooterType.URBAN : ElectricScooterType.OFFROAD;
                type.add(currentType);
                maxBatteryCapacity.add(Float.parseFloat(line[SCOOTERS_MAX_BATTERY_CAPACITY_INDEX]));
                description.add(line[SCOOTERS_DESCRIPTION_INDEX]);
                actualBatteryCapacity.add(Integer.parseInt(line[SCOOTERS_ACTUAL_BATTERY_CAPACITY_INDEX]));
                parkLatitude.add(Double.parseDouble(line[SCOOTERS_PARK_LAT_INDEX]));
                enginePower.add(Integer.parseInt(line[SCOOTERS_ENGINE_POWER_INDEX]));
                parkLongitude.add(Double.parseDouble(line[SCOOTERS_PARK_LON_INDEX]));
            }
        }  catch (NumberFormatException e) {
            throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + fileName);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + fileName);
        }

        try {
            company.getVehicleAPI().registerElectricScooters(aerodynamicCoefficient, frontalArea, weight, type, description, maxBatteryCapacity, actualBatteryCapacity, enginePower, parkLatitude, parkLongitude);
            return aerodynamicCoefficient.size();
        } catch (SQLException e) {
            throw new SQLException("Failed to write data to the database");
        }
    }
}
