package lapr.project.assessment;

import lapr.project.controller.*;
import lapr.project.data.Bootstrap;
import lapr.project.data.Shutdown;
import lapr.project.data.registers.*;
import lapr.project.model.Coordinates;
import lapr.project.model.Invoice;
import lapr.project.model.Trip;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.UnregisteredDataException;
import lapr.project.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private VisualizeFreeSlotsAtParkController visualizeFreeSlotsAtParkController;
    private RegisterPathController registerPathController;
    private VisualizeVehiclesAtParkController visualizeVehiclesAtParkController;
    private FindParksNearbyController findParksNearbyController;
    private UnlockVehicleController unlockVehicleController;
    private LockVehicleController lockVehicleController;
    private ShortestRouteBetweenParksController shortestRouteBetweenParksController;
    private FilterScootersWithAutonomyController filterScootersWithAutonomyController;
    private MostEnergyEfficientRouteController mostEnergyEfficientRouteController;
    private ParkChargingReportController parkChargingReportController;

    /**
     * Prepares facade by booting up the application.
     *
     * @return if the boot-up was successful
     */
    private boolean prepare() {
        try {
            Bootstrap.boot();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to boot up: " + e.getMessage());
            return false;
        }

        company = Company.getInstance();
        registerBicyclesController = new RegisterBicyclesController(company);
        registerElectricScootersController = new RegisterElectricScootersController(company);
        registerParksController = new RegisterParksController(company);
        registerUserController = new RegisterUserController(company);
        registerPOIController = new RegisterPOIController(company);
        removeParkController = new RemoveParkController(company);
        visualizeFreeSlotsAtParkController = new VisualizeFreeSlotsAtParkController(company);
        registerPathController = new RegisterPathController(company);
        visualizeVehiclesAtParkController = new VisualizeVehiclesAtParkController(company);
        findParksNearbyController = new FindParksNearbyController(company);
        unlockVehicleController = new UnlockVehicleController(company);
        lockVehicleController = new LockVehicleController(company);
        shortestRouteBetweenParksController = new ShortestRouteBetweenParksController(company);
        filterScootersWithAutonomyController = new FilterScootersWithAutonomyController();
        mostEnergyEfficientRouteController = new MostEnergyEfficientRouteController(company);
        parkChargingReportController = new ParkChargingReportController();

        return true;
    }

    private List<String[]> loadParsedData(String filePath) {
        List<String[]> parsedData;
        try {
            parsedData = Utils.parseDataFile(filePath, ";", "#");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, () -> "Inexistent file: " + filePath);
            return null;
        }

        return parsedData;
    }

    private String formatInputCoordinate(double coordinate) {
        //BigDecimal.valueOf(coordinate).setScale(10, RoundingMode.FLOOR).stripTrailingZeros().toPlainString() - old method
        return String.format("%.6f", coordinate);
    }

    @Override
    public int addBicycles(String s) {
        prepare();
        try {
            return registerBicyclesController.registerBicycles(s);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to execute the operation:\n" + e.getMessage());
            return 0;
        } finally {
            Shutdown.shutdown();
        }
    }

    @Override
    public int addEscooters(String s) {
        prepare();
        try {
            return registerElectricScootersController.registerElectricScooters(s);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to execute the operation:\n" + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public int addParks(String s) {
        prepare();
        try {
            int result = registerParksController.registerParks(s);
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "\nFailed to addParks:\n" + e.getMessage());
            return 0;
        } finally {
            Shutdown.shutdown();
        }
    }

    @Override
    public int removePark(String s) {
        prepare();
        try {
            removeParkController.removePark(s);
            return 1;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to remove park: \n" + e.getMessage());
        } finally {
            Shutdown.shutdown();
        }
        return 0;
    }

    @Override
    public int addPOIs(String s) {
        prepare();
        try {
            return registerPOIController.registerPOIs(s);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to execute the operation.\n" + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public int addUsers(String s) {
        prepare();
        try {
            return registerUserController.registerClients(s);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to execute the operation.\n" + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public int addPaths(String s) {
        prepare();
        try {
            return registerPathController.registerPaths(s);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to execute the operation.\n" + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public int getNumberOfBicyclesAtPark(double v, double v1, String s) {
        prepare();
        List<Bicycle> bicycles;
        int result = -1;
        try {
            bicycles = visualizeVehiclesAtParkController.getVehiclesAtPark(v, v1, Bicycle.class);
            result = bicycles.size();
            visualizeVehiclesAtParkController.writeOutputFileBicycle(bicycles, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to get number of vehicles at park");
            return -1;
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to write output file");
        } finally {
            Shutdown.shutdown();
        }
        return result;
    }

    @Override
    public int getNumberOfBicyclesAtPark(String s, String s1) {
        prepare();
        List<Bicycle> bicycles;
        int result = -1;
        try {
            bicycles = visualizeVehiclesAtParkController.getVehiclesAtPark(s, Bicycle.class);
            result = bicycles.size();
            visualizeVehiclesAtParkController.writeOutputFileBicycle(bicycles, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to get number of vehicles at park");
            return -1;
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to write output file");
        } finally {
            Shutdown.shutdown();
        }
        return result;
    }

    @Override
    public int getNumberOfEscootersAtPark(double v, double v1, String s) {
        prepare();
        List<ElectricScooter> electricScooters;
        int result = -1;
        try {
            electricScooters = visualizeVehiclesAtParkController.getVehiclesAtPark(v, v1, ElectricScooter.class);
            result = electricScooters.size();
            visualizeVehiclesAtParkController.writeOutputFileElectricScooter(electricScooters, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to get number of vehicles at park");
            return -1;
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to write output file");
        } finally {
            Shutdown.shutdown();
        }
        return result;
    }

    @Override
    public int getNumberOfEScootersAtPark(String s, String s1) {
        prepare();
        List<ElectricScooter> electricScooters;
        int result = -1;
        try {
            electricScooters = visualizeVehiclesAtParkController.getVehiclesAtPark(s, ElectricScooter.class);
            result = electricScooters.size();
            visualizeVehiclesAtParkController.writeOutputFileElectricScooter(electricScooters, s1);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to get number of vehicles at park");
            return -1;
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to write output file");
        } finally {
            Shutdown.shutdown();
        }
        return result;
    }

    @Override
    public void getNearestParks(double v, double v1, String s) {
        prepare();
        getNearestParks(v, v1, s, 0);
        Shutdown.shutdown();
    }

    @Override
    public void getNearestParks(double v, double v1, String s, int i) {
        prepare();
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
            LOGGER.log(Level.INFO, "Failed to access the database when attempting to find nearby parks.");
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to write the output file.");
        }
        Shutdown.shutdown();
    }

    @Override
    public int getFreeBicycleSlotsAtPark(String s, String s1) {
        prepare();
        try {
            return visualizeFreeSlotsAtParkController.fetchFreeSlotsAtParkByType(s, VehicleType.BICYCLE);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "failed to retrieve free slots at park: " + e.getMessage());
            return 0;
        } finally {
            Shutdown.shutdown();
        }
    }

    @Override
    public int getFreeEscooterSlotsAtPark(String s, String s1) {
        prepare();
        try {
            return visualizeFreeSlotsAtParkController.fetchFreeSlotsAtParkByType(s, VehicleType.ELECTRIC_SCOOTER);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "failed to retrieve free slots at park: " + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public int getFreeSlotsAtParkForMyLoanedVehicle(String s, String s1) {
        prepare();
        try {
            return visualizeFreeSlotsAtParkController.fetchFreeSlotsAtPark(s1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, String.format("Failed to get free slots at park (%s) for user's (%s) loaned vehicle", s1, s));
        } catch (UnregisteredDataException e) {
            LOGGER.log(Level.INFO, "Invalid information provided: " + e.getMessage());
        } finally {
            Shutdown.shutdown();
        }
        return -1;
    }

    @Override
    public int linearDistanceTo(double v, double v1, double v2, double v3) {
        return (int) (new Coordinates(v, v1, 0).distanceIgnoringHeight(new Coordinates(v2, v3, 0)) * 1000);
    }

    @Override
    public int pathDistanceTo(double v, double v1, double v2, double v3) {
        prepare();
        try {
            return shortestRouteBetweenParksController.shortestRouteBetweenTwoParksFetchByCoordinates(v,v1,v2,v3);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "No path found:\n" + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public long unlockBicycle(String s, String s1) {
        prepare();
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
        prepare();
        try {
            unlockVehicleController.startTrip(s1, s);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to unlock scooter:\n" + e.getMessage());
        }
        Shutdown.shutdown();
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public long lockBicycle(String s, double v, double v1, String s1) {
        prepare();
        try {
            lockVehicleController.lockVehicle(v, v1, s, true);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock bicycle: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        } finally{
            Shutdown.shutdown();
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public long lockBicycle(String s, String s1, String s2) {
        prepare();
        try {
            lockVehicleController.lockVehicle(s1, s, true);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock bicycle: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        } finally{
            Shutdown.shutdown();
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public long lockEscooter(String s, double v, double v1, String s1) {
        prepare();
        try {
            lockVehicleController.lockVehicle(v, v1, s, true);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock EScooter: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        } finally {
            Shutdown.shutdown();
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public long lockEscooter(String s, String s1, String s2) {
        prepare();
        try {
            lockVehicleController.lockVehicle(s1, s, true);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to lock EScooter: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to email the client: " + e.getMessage());
        } finally{
            Shutdown.shutdown();
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Register a user on the system.
     *
     * @param username User's username.
     * @param email User's email.
     * @param password User's desired password.
     * @param visaCardNumber User's Visa Card number.
     * @param height User's height in cm.
     * @param weight User's weight in kg.
     * @param averageCyclingSpeed User's average speed in m/s with two decimal places e.g 4.17.
     * @param gender User's gender in text.
     * @return Return 1 if a user is successfully registered.
     */
    @Override
    public int registerUser(String username, String email, String password, String visaCardNumber, int height, int weight, BigDecimal averageCyclingSpeed, String gender) {
        prepare();
        try {
            registerUserController.registerClient(username, email, password, visaCardNumber, height, weight, gender, averageCyclingSpeed.floatValue());
            return 1;
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to register user: " + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public long unlockAnyEscooterAtPark(String s, String s1, String s2) {
        prepare();
        try {
            unlockVehicleController.startTripPark(s1, s, s2);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to unlock scooter with highest battery:\n" + e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to export information about the scooter to a file\n" + e.getMessage());
        }
        Shutdown.shutdown();
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public long unlockAnyEscooterAtParkForDestination(String s, String s1, double v, double v1, String s2) {
        prepare();
        try {
            unlockVehicleController.startTripParkDest(s, s1, v, v1, s2);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to unlock scooter with highest battery:\n"
                    + e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to export information about the scooter to a file\n" + e.getMessage());
        }
        Shutdown.shutdown();
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public int suggestEscootersToGoFromOneParkToAnother(String s, String s1, double v, double v1, String s2) {
        prepare();
        int size = 0;
        try {
            size = filterScootersWithAutonomyController.suggestScootersBetweenParks(s, s1, v, v1, s2);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Failed to retrieve data from the database with scooters that have enough battery\n" + e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to export the file with the scooter information\n" + e.getMessage());
        }
        Shutdown.shutdown();
        return size;
    }

    @Override
    public long mostEnergyEfficientRouteBetweenTwoParks(String s, String s1, String s2, String s3, String s4, String s5) {
        prepare();
        try {
            return mostEnergyEfficientRouteController.mostEnergyEfficientRouteBetweenTwoParks(s,s1,s2,s3,s4,s5);
        } catch (SQLException |IOException e) {
            LOGGER.log(Level.INFO, "Couldn't find a route:\n" + e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public double getUserCurrentDebt(String s, String s1) {
        prepare();
        double totalDebt = 0;
        try {
            ParkAPI parkAPI = company.getParkAPI();
            List<Trip> tripsInDebt = company.getTripAPI().fetchUserTripsLastInvoice(s, true);
            Collections.sort(tripsInDebt, new compareTripsByUnlockTime());

            List<String> fileLines = new ArrayList<>();
            fileLines.add("vehicle description;vehicle unlock time;vehicle lock time;origin park latitude;origin park longitude;destination park latitude;destination park longitude;total time spent in seconds;charged value");
            for (Trip trip : tripsInDebt) {
                Park startPark = parkAPI.fetchParkById(trip.getStartParkId());
                Park endPark = parkAPI.fetchParkById(trip.getEndParkId());
                Coordinates startParkCoordinates = startPark.getCoordinates();
                Coordinates endParkCoordinates = endPark.getCoordinates();
                long unlockTimeMilli = trip.getStartTime().getTime();
                long lockTimeMilli = trip.getEndTime().getTime();
                int tripDuration = Math.toIntExact((lockTimeMilli - unlockTimeMilli) / 1000);
                double tripCost = trip.calculateTripCost();
                totalDebt += tripCost;
                fileLines.add(String.format("%s;%d;%d;%s;%s;%s;%s;%d;%.2f",
                        trip.getVehicleDescription(),
                        unlockTimeMilli,
                        lockTimeMilli,
                        formatInputCoordinate(startParkCoordinates.getLatitude()),
                        formatInputCoordinate(startParkCoordinates.getLongitude()),
                        formatInputCoordinate(endParkCoordinates.getLatitude()),
                        formatInputCoordinate(endParkCoordinates.getLongitude()),
                        tripDuration,
                        tripCost));
            }

            Utils.writeToFile(fileLines, s1);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to fetch trips in debt: " + e.getMessage());
        }
        Shutdown.shutdown();
        return totalDebt;
    }

    // Extra
    public double getUserCurrentDebtForAllUnpaidInvoices(String s, String s1) {
        prepare();
        double totalDebt = 0;
        try {
            ParkAPI parkAPI = company.getParkAPI();
            List<Trip> tripsInDebt = company.getTripAPI().fetchUserTripsInDebt(s);
            Collections.sort(tripsInDebt, new compareTripsByUnlockTime());

            List<String> fileLines = new ArrayList<>();
            fileLines.add("vehicle description;vehicle unlock time;vehicle lock time;origin park latitude;origin park longitude;destination park latitude;destination park longitude;total time spent in seconds;charged value");
            for (Trip trip : tripsInDebt) {
                Park startPark = parkAPI.fetchParkById(trip.getStartParkId());
                Park endPark = parkAPI.fetchParkById(trip.getEndParkId());
                Coordinates startParkCoordinates = startPark.getCoordinates();
                Coordinates endParkCoordinates = endPark.getCoordinates();
                long unlockTimeMilli = trip.getStartTime().getTime();
                long lockTimeMilli = trip.getEndTime().getTime();
                int tripDuration = Math.toIntExact((lockTimeMilli - unlockTimeMilli) / 1000);
                double tripCost = trip.calculateTripCost();
                totalDebt += tripCost;
                fileLines.add(String.format("%s;%d;%d;%s;%s;%s;%s;%d;%.2f",
                        trip.getVehicleDescription(),
                        unlockTimeMilli,
                        lockTimeMilli,
                        formatInputCoordinate(startParkCoordinates.getLatitude()),
                        formatInputCoordinate(startParkCoordinates.getLongitude()),
                        formatInputCoordinate(endParkCoordinates.getLatitude()),
                        formatInputCoordinate(endParkCoordinates.getLongitude()),
                        tripDuration,
                        tripCost));
            }

            Utils.writeToFile(fileLines, s1);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to fetch trips in debt: " + e.getMessage());
        }
        Shutdown.shutdown();
        return totalDebt;
    }

    @Override
    public double getUserCurrentPoints(String s, String s1) {
        prepare();
        Client client = null;
        int totalPoints = 0;
        try {
            ParkAPI parkAPI = company.getParkAPI();
            UserAPI userAPI = company.getUserAPI();
            TripAPI tripAPI = company.getTripAPI();
            client = userAPI.fetchClientByUsername(s);
            List<Trip> trips = tripAPI.fetchAllClientTrips(client.getEmail());
            List<String> fileLines = new ArrayList<>();
            fileLines.add("vehicle description;vehicle unlock time;vehicle lock time;origin park latitude;origin park longitude;origin park elevation;" +
                    "destination park latitude;destination park longitude;destination park elevation;elevation difference;points");
            for (Trip trip : trips) {
                if (trip.getEndTime() == null)
                    continue;
                Park startPark = parkAPI.fetchParkById(trip.getStartParkId());
                Park endPark = parkAPI.fetchParkById(trip.getEndParkId());
                Coordinates startParkCoordinates = startPark.getCoordinates();
                Coordinates endParkCoordinates = endPark.getCoordinates();
                long unlockTimeMilli = trip.getStartTime().getTime();
                long lockTimeMilli = trip.getEndTime().getTime();
                int pointGivenToUser = tripAPI.calculatePointsGivenToUser(trip);
                fileLines.add(String.format("%s;%d;%d;%s;%s;%d;%s;%s;%d;%d;%d",
                        trip.getVehicleDescription(),
                        unlockTimeMilli,
                        lockTimeMilli,
                        formatInputCoordinate(startParkCoordinates.getLatitude()),
                        formatInputCoordinate(startParkCoordinates.getLongitude()),
                        startParkCoordinates.getAltitude(),
                        formatInputCoordinate(endParkCoordinates.getLatitude()),
                        formatInputCoordinate(endParkCoordinates.getLongitude()),
                        endParkCoordinates.getAltitude(),
                        endParkCoordinates.getAltitude() - startParkCoordinates.getAltitude(),
                        pointGivenToUser
                ));
                totalPoints += pointGivenToUser;
            }
            Utils.writeToFile(fileLines, s1);
            if (client == null)
                LOGGER.log(Level.INFO, "username " + s + " is not registered");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to access the database");
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to write to file (user points)");
        } finally{
            Shutdown.shutdown();
        }
        if (client != null)
            return totalPoints;
        else
            return -1;
    }

    @Override
    public double calculateElectricalEnergyToTravelFromOneLocationToAnother(double v, double v1, double v2, double v3, String s) {
        prepare();
        try {
            return BigDecimal.valueOf(mostEnergyEfficientRouteController.calculateElectricalEnergyToTravelFromOneLocationToAnother(v,v1,v2,v3,s)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "No trip detacted: " + e.getMessage());
            return 0;

        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public long forHowLongAVehicleIsUnlocked(String s) {
        prepare();
        TripAPI tripAPI = company.getTripAPI();
        Trip trip = null;
        try {
            trip = tripAPI.fetchTripVehicleIsIn(s);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to fetch trip: " + e.getMessage());
        } finally {
            Shutdown.shutdown();
        }
        if (trip == null)
            return 0;
        return trip.getTripDurationMillis() / 1000;
    }

    /**
     * Calculate the shortest Route from one park to another.
     * <p>
     * Basic: Only one shortest Route between two Parks is available. Advanced: More than one Route between two parks
     * are available with different number of POIs (limit to a maximum of two) inbetween and different evelations
     * difference.
     *
     * @param originLatitudeInDegrees Origin latitude in Decimal degrees.
     * @param originLongitudeInDegrees Origin Longitude in Decimal degrees.
     * @param destinationLatitudeInDegrees Destination Park latitude in Decimal degrees.
     * @param destinationLongitudeInDegrees Destination Park Longitude in Decimal degrees.
     * @param numberOfPOIs The number of POIs that should be included in the path. Default can be 0.
     * @param outputFileName Write to the file the Route between two parks according to file output/paths.csv. More than
     * one path may exist. If so, sort routes by the ascending number of points between the parks and by ascending order
     * of elevation difference.
     * @return The distance in meters for the shortest path.
     */
    @Override
    public long shortestRouteBetweenTwoParks(double originLatitudeInDegrees, double originLongitudeInDegrees, double destinationLatitudeInDegrees, double destinationLongitudeInDegrees, int numberOfPOIs, String outputFileName) {
        prepare();
        try {
            return shortestRouteBetweenParksController.shortestRouteBetweenTwoParksFetchByCoordinates(originLatitudeInDegrees, originLongitudeInDegrees, destinationLatitudeInDegrees, destinationLongitudeInDegrees, numberOfPOIs, outputFileName);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return 0; // doesn't say what to return in the documentation
        } finally{
            Shutdown.shutdown();
        }
    }

    /**
     * Calculate the shortest Route from one park to another.
     * <p>
     * Basic: Only one shortest Route between two Parks is available. Advanced: More than one Route between two parks
     * are available with different number of POIs (limit to a maximum of two) inbetween and different evelations
     * difference.
     *
     * @param originParkIdentification Origin Park Identification.
     * @param destinationParkIdentification Destination Park Identification.
     * @param numberOfPOIs The number of POIs that should be included in the path. Default can be 0.
     * @param outputFileName Write to the file the Route between two parks according to file output/paths.csv. More than
     * one path may exist. If so, sort routes by the ascending number of points between the parks and by ascending order
     * of elevation difference.
     * @return The distance in meters for the shortest path.
     */
    @Override
    public long shortestRouteBetweenTwoParks(String originParkIdentification, String destinationParkIdentification, int numberOfPOIs, String outputFileName) {
        prepare();
        try {
            return shortestRouteBetweenParksController.shortestRouteBetweenTwoParksFetchByID(originParkIdentification,destinationParkIdentification, numberOfPOIs, outputFileName);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return 0; // doesn't say what to return in the documentation
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public long shortestRouteBetweenTwoParksForGivenPOIs(String s, String s1, String s2, String s3) {
        prepare();
        try {
            return shortestRouteBetweenParksController.shortestRouteBetweenTwoParksAndGivenPoisFetchById(s, s1, s2, s3);
        } catch (SQLException | InvalidFileDataException | IOException e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return 0; // doesn't say what to return in the documentation
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public long shortestRouteBetweenTwoParksForGivenPOIs(double v, double v1, double v2, double v3, String s, String s1) {
        prepare();
        try {
            return shortestRouteBetweenParksController.shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(v, v1, v2, v3, s, s1);
        } catch (SQLException | InvalidFileDataException | IOException e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return 0; // doesn't say what to return in the documentation
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public long getParkChargingReport(String s, String s1) {
        prepare();
        try {
            return parkChargingReportController.retrieveParkChargingReport(s,s1);
        } catch (SQLException | IOException e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public int suggestRoutesBetweenTwoLocations(String s, String s1, String s2, String s3, String s4, int i, boolean b, String s5, String s6, String s7) {
        prepare();
        try {
            return mostEnergyEfficientRouteController.suggestRoutesBetweenTwoLocations(s,s1,s2,s3,s4,i,b,s5,s6,s7);
        } catch (SQLException | IOException | InvalidFileDataException e) {
            e.printStackTrace();
            return 0;
        } finally{
            Shutdown.shutdown();
        }
    }

    @Override
    public double getInvoiceForMonth(int i, String s, String s1) {
        prepare();
        double totalCost = 0;
        try {
            ParkAPI parkAPI = company.getParkAPI();
            InvoiceAPI invoiceAPI = company.getInvoiceAPI();
            UserAPI userAPI = company.getUserAPI();
            Client client = userAPI.fetchClientByUsername(s);
            if (client == null) {
                LOGGER.log(Level.WARNING, "Client " + s + " does not exist");
                return 0;
            }
            Invoice invoice = invoiceAPI.issueInvoice(i, client.getEmail());
            if (invoice == null) {
                LOGGER.log(Level.INFO, "No invoice for the given month (output generated either way)");
                return 0;
            }
            List<Trip> tripsInDebt = company.getTripAPI().fetchTripsForInvoice(invoice, true);
            Collections.sort(tripsInDebt, new compareTripsByUnlockTime());
            double valueToPay = BigDecimal.valueOf(invoice.getAmountLeftToPay()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            List<String> fileLines = new ArrayList<>();
            fileLines.add(String.format("%s\n" +
                            "Previous points:%d\n" +
                            "Earned points:%d\n" +
                            "Discounted points:%d\n" +
                            "Actual points:%d\n" +
                            "Charged Value:%.2f",
                    s,
                    invoice.getPreviousPoints(),
                    invoice.getEarnedPoints(),
                    invoice.getPointsUsed(),
                    invoice.getPreviousPoints() + invoice.getEarnedPoints() - invoice.getPointsUsed(),
                    valueToPay));
            fileLines.add("vehicle description;vehicle unlock time;vehicle lock time;origin park latitude;origin park longitude;destination park latitude;destination park longitude;total time spent in seconds;charged value");
            for (Trip trip : tripsInDebt) {
                Park startPark = parkAPI.fetchParkById(trip.getStartParkId());
                Park endPark = parkAPI.fetchParkById(trip.getEndParkId());
                Coordinates startParkCoordinates = startPark.getCoordinates();
                Coordinates endParkCoordinates = endPark.getCoordinates();
                long unlockTimeMilli = trip.getStartTime().getTime();
                long lockTimeMilli = trip.getEndTime().getTime();
                int tripDuration = Math.toIntExact((lockTimeMilli - unlockTimeMilli) / 1000);
                double tripCost = BigDecimal.valueOf(trip.calculateTripCost()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                totalCost += tripCost;
                fileLines.add(String.format("%s;%d;%d;%s;%s;%s;%s;%d;%.2f",
                        trip.getVehicleDescription(),
                        unlockTimeMilli,
                        lockTimeMilli,
                        formatInputCoordinate(startParkCoordinates.getLatitude()),
                        formatInputCoordinate(startParkCoordinates.getLongitude()),
                        formatInputCoordinate(endParkCoordinates.getLatitude()),
                        formatInputCoordinate(endParkCoordinates.getLongitude()),
                        tripDuration,
                        tripCost));
            }
            Utils.writeToFile(fileLines, s1);
            // Guarantees that even if data is forcefully inserted into the database, this method will return an accurate total amount to pay
            return totalCost - Utils.pointsToEuros(invoice.calculatePointsToDiscount(invoice.getEarnedPoints() + invoice.getPreviousPoints()));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.INFO, "Failed to get invoice for month: " + e.getMessage());
            return 0;
        } finally {
            Shutdown.shutdown();
        }
    }
}