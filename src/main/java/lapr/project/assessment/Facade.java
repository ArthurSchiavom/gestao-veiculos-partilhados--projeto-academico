package lapr.project.assessment;

import lapr.project.controller.*;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Facade implements Serviceable {
    private static final Logger LOGGER = Logger.getLogger("FacadeLogger");

    private final Company company = Company.getInstance();
    private final RegisterBicyclesController registerBicyclesController = new RegisterBicyclesController(company);
    private final RegisterElectricScootersController registerElectricScootersController = new RegisterElectricScootersController(company);
    private final RegisterParksController registerParksController = new RegisterParksController(company);
    private final RegisterUserController registerUserController = new RegisterUserController(company);
    private final RegisterPOIController registerPOIController = new RegisterPOIController(company);
    private final RemoveParkController removeParkController = new RemoveParkController(company);
    private final GetFreeSlotsByTypeController getFreeSlotsByTypeController = new GetFreeSlotsByTypeController(company);
    private final RegisterPathController registerPathController = new RegisterPathController(company);
    private final VisualizeVehiclesAtParkController visualizeVehiclesAtParkController = new VisualizeVehiclesAtParkController(company);
    private final FindParksNearbyController findParksNearbyController = new FindParksNearbyController(company);

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

    @Override
    public int addBicycles(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return 0;

        try {
            return registerBicyclesController.registerBicycles(parsedData, s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "File with incorrect data.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int addEscooters(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return 0;

        try {
            return registerElectricScootersController.registerElectricScooters(parsedData, s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "File with incorrect data.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int addParks(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return 0;

        try {
            return registerParksController.registerParks(parsedData, s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "File with incorrect data.\n" + e.getMessage());
            return 0;
        }

    }

    @Override
    public int removePark(String s) {
        try {
            removeParkController.removePark(s);
            return 1;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to remove park: \n" + e.getMessage());
        }
        return 0;
    }

    @Override
    public int addPOIs(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return 0;

        try {
            return registerPOIController.registerPOIs(parsedData,s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "File with incorrect data.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int addUsers(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return 0;
        try {
            return registerUserController.registerClients(parsedData,s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "File with incorrect data.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int addPaths(String s) {
        List<String[]> parsedData = loadParsedData(s);
        if (parsedData == null)
            return 0;
        try {
            return registerPathController.registerPaths(parsedData,s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "File with incorrect data.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int getNumberOfBicyclesAtPark(double v, double v1, String s) {
        List<Bicycle> bicycles;
        int result = -1;
        try {
            bicycles = visualizeVehiclesAtParkController.getVehiclesAtPark(v, v1, Bicycle.class);
            result = bicycles.size();
            visualizeVehiclesAtParkController.writeOutputFile(bicycles, s);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get number of vehicles at park");
            return -1;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write output file");
        }
        return result;
    }

    @Override
    public int getNumberOfBicyclesAtPark(String s, String s1) {
        List<Bicycle> bicycles;
        int result = -1;
        try {
            bicycles = visualizeVehiclesAtParkController.getVehiclesAtPark(s, Bicycle.class);
            result = bicycles.size();
            visualizeVehiclesAtParkController.writeOutputFile(bicycles, s);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get number of vehicles at park");
            return -1;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write output file");
        }
        return result;
    }

    @Override
    public void getNearestParks(double v, double v1, String s) {
        getNearestParks(v, v1, s, 0);
    }

    @Override
    public void getNearestParks(double v, double v1, String s, int i) {
        Coordinates location = new Coordinates(v, v1, 0);
        List<Park> parks = new ArrayList<>();
        List<String> outputLines = new ArrayList<>();
        outputLines.add("latitude;longitude;distance in meters");
        try {
            parks = findParksNearbyController.findParksNearby(v, v1, i);
            for (Park park : parks) {
                Coordinates parkCoord = park.getCoordinates();
                outputLines.add(String.format("%f; %f; %.0f", parkCoord.getLatitude(), parkCoord.getLongitude(), parkCoord.distanceIgnoringHeight(location)));
            }
            Utils.writeToFile(outputLines, s);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to access the database when attempting to find nearby parks.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write the output file.");
        }
    }

    @Override
    public int getFreeBicycleSlotsAtPark(String s, String s1) {
        try {
            return getFreeSlotsByTypeController.getFreeSlotsByType(s,s1, VehicleType.BICYCLE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return 0;
        }
    }

    @Override
    public int getFreeEscooterSlotsAtPark(String s, String s1) {
        try {
            return getFreeSlotsByTypeController.getFreeSlotsByType(s,s1, VehicleType.ELECTRIC_SCOOTER);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return 0;
        }
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
