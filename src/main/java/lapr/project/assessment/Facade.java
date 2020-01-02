package lapr.project.assessment;

import lapr.project.controller.*;
import lapr.project.data.Bootstrap;
import lapr.project.data.Shutdown;
import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.Utils;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Facade implements Serviceable {
    private static final Logger LOGGER = Logger.getLogger("FacadeLogger");

    private Company company;
    private RegisterBicyclesController registerBicyclesController;
    private RegisterElectricScootersController registerElectricScootersController;
    private RegisterParksController registerParksController;
    private RegisterUserController registerUserController;
    private RegisterPOIController registerPOIController;
    private RemoveParkController removeParkController;
    private GetFreeSlotsByTypeController getFreeSlotsByTypeController;
    private RegisterPathController registerPathController;
    private VisualizeVehiclesAtParkController visualizeVehiclesAtParkController;
    private FindParksNearbyController findParksNearbyController;
    private UnlockVehicleController unlockVehicleController;
    private LockVehicleController lockVehicleController;

    /**
     * Prepares facade by booting up the application and Facade variables.
     *
     * @return if the boot-up was successful
     */
    private boolean prepare(boolean resetDatabase) {
        try {
            Bootstrap.boot();
            if (resetDatabase)
                Bootstrap.resetDatabase();
        } catch (SQLException | IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to boot up: " + e.getMessage());
            return false;
        }

        company = Company.getInstance();
        registerBicyclesController = new RegisterBicyclesController(company);
        registerElectricScootersController = new RegisterElectricScootersController(company);
        registerParksController = new RegisterParksController(company);
        registerUserController = new RegisterUserController(company);
        registerPOIController = new RegisterPOIController(company);
        removeParkController = new RemoveParkController(company);
        getFreeSlotsByTypeController = new GetFreeSlotsByTypeController(company);
        registerPathController = new RegisterPathController(company);
        visualizeVehiclesAtParkController = new VisualizeVehiclesAtParkController(company);
        findParksNearbyController = new FindParksNearbyController(company);
        unlockVehicleController = new UnlockVehicleController(company);
        lockVehicleController = new LockVehicleController(company);

        return true;
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

    private String formatInputCoordinate(double coordinate) {
        return new BigDecimal(coordinate).setScale(10, RoundingMode.FLOOR).stripTrailingZeros().toPlainString();
    }

    @Override
    public int addBicycles(String s) {
        try {
            return registerBicyclesController.registerBicycles(s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute the operation.\n" + e.getMessage());
            return 0;
        } finally {
            Shutdown.shutdown();
        }
    }

    @Override
    public int addEscooters(String s) {
        try {
            return registerElectricScootersController.registerElectricScooters(s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute the operation.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int addParks(String s) {
        prepare(true);
        try {
            int result = registerParksController.registerParks(s);
            Shutdown.shutdown();
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "\nFailed to addParks:\n" + e.getMessage());
            Shutdown.shutdown();
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
        try {
            return registerPOIController.registerPOIs(s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute the operation.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int addUsers(String s) {
        try {
            return registerUserController.registerClients(s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute the operation.\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int addPaths(String s) {
        try {
            return registerPathController.registerPaths(s);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute the operation.\n" + e.getMessage());
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
        Shutdown.shutdown();
    }

    @Override
    public void getNearestParks(double v, double v1, String s, int i) {
        prepare(false);
        Coordinates location = new Coordinates(v, v1, 0);
        List<Park> parks = new ArrayList<>();
        List<String> outputLines = new ArrayList<>();
        outputLines.add("latitude;longitude;distance in meters");
        try {
            parks = findParksNearbyController.findParksNearby(v, v1, i);
            for (Park park : parks) {
                Coordinates parkCoord = park.getCoordinates();
                outputLines.add(String.format("%s;%s;%.0f", formatInputCoordinate(parkCoord.getLatitude()), formatInputCoordinate(parkCoord.getLongitude()), parkCoord.distanceIgnoringHeight(location) * 1000));
            }
            Utils.writeToFile(outputLines, s);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to access the database when attempting to find nearby parks.");
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write the output file.");
        }
        Shutdown.shutdown();
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
        return (int) new Coordinates(v, v1, 0).distanceIgnoringHeight(new Coordinates(v2, v3, 0));
    }

    @Override
    public int pathDistanceTo(double v, double v1, double v2, double v3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long unlockBicycle(String s, String s1) {
        prepare(false);
        try {
            unlockVehicleController.startTrip(s1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to unlock bicycle:\n" + e.getMessage());
        }
        Shutdown.shutdown();
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public long unlockEscooter(String s, String s1) {
        prepare(false);
        try {
            unlockVehicleController.startTrip(s1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to unlock scooter:\n" + e.getMessage());
        }
        Shutdown.shutdown();
        return Calendar.getInstance().getTimeInMillis();
    }

    //TODO - TEST (database was down)
    @Override
    public long lockBicycle(String s, double v, double v1, String s1) {
        try {
            lockVehicleController.lockVehicle(v, v1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock bicycle: " + e.getMessage());
        } catch (MessagingException e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    //TODO - TEST (database was down)
    @Override
    public long lockBicycle(String s, String s1, String s2) {
        try {
            lockVehicleController.lockVehicle(s1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock bicycle: " + e.getMessage());
        } catch (MessagingException e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    //TODO - TEST (database was down)
    @Override
    public long lockEscooter(String s, double v, double v1, String s1) {
        try {
            lockVehicleController.lockVehicle(v, v1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock EScooter: " + e.getMessage());
        } catch (MessagingException e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    //TODO - TEST (database was down)
    @Override
    public long lockEscooter(String s, String s1, String s2) {
        try {
            lockVehicleController.lockVehicle(s1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock EScooter: " + e.getMessage());
        } catch (MessagingException e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public int registerUser(String s, String s1, String s2, int i, int i1, String s3) {
        try {
            registerUserController.registerClient(s, s1, s2, i, i1, s3);
            return 1;
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to register user: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public long unlockAnyEscootereAtPark(String s, String s1, String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long unlockAnyEscootereAtParkForDestination(String s, String s1, double v, double v1, String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int suggestEscootersToGoFromOneParkToAnother(String s, String s1, double v, double v1, String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long mostEnergyEfficientRouteBetweenTwoParks(String s, String s1, String s2, String s3, String s4, String s5) {
        throw new UnsupportedOperationException();
    }
}
