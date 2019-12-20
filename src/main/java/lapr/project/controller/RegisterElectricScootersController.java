package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooterType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterElectricScootersController {
    private static final Logger LOGGER = Logger.getLogger("registerElectricScootersControllerLogger");

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

//    private final Company company;
//
//    public RegisterElectricScootersController(Company company) {
//        this.company = company;
//    }
//
//    public int registerElectricScooters(List<String[]> parsedData, String fileName) {
//        List<Float> aerodynamicCoefficient = new ArrayList<>();
//        List<Float> frontalArea = new ArrayList<>();
//        List<Integer> weight = new ArrayList<>();
//        List<ElectricScooterType> type = new ArrayList<>();
//        List<String> description = new ArrayList<>();
//        List<Float> maxBatteryCapacity = new ArrayList<>();
//        List<Integer> actualBatteryCapacity = new ArrayList<>();
//        List<Double> parkLatitude = new ArrayList<>();
//        List<Integer> enginePower = new ArrayList<>();
//        List<Double> parkLongitude = new ArrayList<>();
//
//        int i = 0;
//        try {
//            for (i = 0; i < parsedData.size(); i++) {
//                String[] line = parsedData.get(i);
//                if (line.length == 1 && line[0].isEmpty())
//                    continue;
//
//                aerodynamicCoefficient.add(Float.parseFloat(line[SCOOTERS_AERODYNAMIC_COEFFICIENT_INDEX]));
//                weight.add(Integer.parseInt(line[SCOOTERS_WEIGHT_INDEX]));
//                frontalArea.add(Float.parseFloat(line[SCOOTERS_FRONTAL_AREA_INDEX]));
//                ElectricScooterType currentType = line[SCOOTERS_TYPE_INDEX].equalsIgnoreCase("city") ? ElectricScooterType.URBAN : ElectricScooterType.OFFROAD;
//                type.add(currentType);
//                description.add(line[SCOOTERS_DESCRIPTION_INDEX]);
//                maxBatteryCapacity.add(Float.parseFloat(line[SCOOTERS_MAX_BATTERY_CAPACITY_INDEX]));
//                actualBatteryCapacity.add(Integer.parseInt(line[SCOOTERS_ACTUAL_BATTERY_CAPACITY_INDEX]));
//                parkLatitude.add(Double.parseDouble(line[SCOOTERS_PARK_LAT_INDEX]));
//                enginePower.add(Integer.parseInt(line[SCOOTERS_ENGINE_POWER_INDEX]));
//                parkLongitude.add(Double.parseDouble(line[SCOOTERS_PARK_LON_INDEX]));
//            }
//        } catch (NumberFormatException e) {
//            LOGGER.log(Level.SEVERE, "Invalid data at line number " + i + " of the file " + fileName);
//            return -1;
//        } catch (IndexOutOfBoundsException e) {
//            LOGGER.log(Level.SEVERE, "Not all columns are present at line " + i + " of the file " + fileName);
//            return -1;
//        }
//
//        try {
//            company.getVehicleRegister().registerElectricScooters(aerodynamicCoefficient, frontalArea, weight, type, description, maxBatteryCapacity, actualBatteryCapacity, enginePower, parkLatitude, parkLongitude);
//            return aerodynamicCoefficient.size();
//        } catch (SQLException e) {
//            LOGGER.log(Level.SEVERE, "Failed to register the scooters on the database");
//            return -1;
//        }
//    }
}
