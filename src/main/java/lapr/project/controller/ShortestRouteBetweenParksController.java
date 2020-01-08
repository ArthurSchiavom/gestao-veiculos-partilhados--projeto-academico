package lapr.project.controller;

import lapr.project.data.SortByHeightDescending;
import lapr.project.data.registers.Company;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.Path;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;
import lapr.project.utils.physics.calculations.PhysicsMethods;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ShortestRouteBetweenParksController {
    private final Company company;
    private static final int POI_LAT = 0;
    private static final int POI_LON = 1;
    private static final String HEADER = "latitude;longitude;elevation;poi description";
    private static final String LINE_SEPARATOR = ";";
    private static final String COMMENT_TAG = "#";

    public ShortestRouteBetweenParksController(Company company) {
        this.company = company;
    }

    /**
     * Returns the distance in meters of the shortest route between 2 parks
     * @param originLatitudeInDegrees lat of the origin park
     * @param originLongitudeInDegrees lon of the origin park
     * @param destinationLatitudeInDegrees lat of the end park
     * @param destinationLongitudeInDegrees lon of the end park
     * @return distance in meters
     * @throws SQLException exception that might occur when accessing the sql oracle database
     */
    public int shortestRouteBetweenTwoParksFetchByCoordinates(double originLatitudeInDegrees, double originLongitudeInDegrees, double destinationLatitudeInDegrees, double destinationLongitudeInDegrees) throws SQLException {
        PointOfInterest originPark = company.getPoiAPI().fetchPoi(originLatitudeInDegrees,originLongitudeInDegrees);
        PointOfInterest endPark = company.getPoiAPI().fetchPoi(destinationLatitudeInDegrees,destinationLongitudeInDegrees);
        LinkedList<PointOfInterest> path = new LinkedList<>();
        return (int) Math.round(MapGraphAlgorithms.shortestPath(company.getMapGraphDistance(),originPark,endPark,path)*1000); // km to meters
    }
    /**
     * Returns the distance in meters of the shortest route between 2 parks
     * @param originLatitudeInDegrees lat of the origin park
     * @param originLongitudeInDegrees lon of the origin park
     * @param destinationLatitudeInDegrees lat of the end park
     * @param destinationLongitudeInDegrees lon of the end park
     * @param outputFileName name of the output file
     * @return distance in meters
     * @throws SQLException exception that might occur when accessing the sql oracle database
     */
    public long shortestRouteBetweenTwoParksFetchByCoordinates(double originLatitudeInDegrees, double originLongitudeInDegrees, double destinationLatitudeInDegrees, double destinationLongitudeInDegrees, int numberOfPois, String outputFileName) throws SQLException, IOException {
        PointOfInterest origin = company.getPoiAPI().fetchPoi(originLatitudeInDegrees,originLongitudeInDegrees);
        PointOfInterest end = company.getPoiAPI().fetchPoi(destinationLatitudeInDegrees,destinationLongitudeInDegrees);
        List<String> output = new LinkedList<>();
        if(origin == null || end == null){
            output.add("Não existem pontos de interesse com as coordenadas fornecidas");
            Utils.writeToFile(output,outputFileName);
            return 0;
        }
        ArrayList<LinkedList<PointOfInterest>> paths = MapGraphAlgorithms.allPaths(company.getMapGraphDistance(),origin,end);
        LinkedList<PointOfInterest> choosenPath = null;
        for(LinkedList<PointOfInterest> path : paths){
            if((path.size()-2) == numberOfPois){
                choosenPath = path;
                break;
            }
        }

        if(choosenPath == null){
            output.add("Não existem caminhos com esse tamanho");
            Utils.writeToFile(output,outputFileName);
            return 0;
        }
        //dummy vehicle and client because they're not given----------------------
        ElectricScooter dummyVehicle = new ElectricScooter(12345, "PT596",5.3F,3.4F,
                500,true, ElectricScooterType.URBAN,75,
                1f, 1500);

        Client dummyClient = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',22.3F,
                true, new CreditCard("12341234123412"));
        //------------------------------------------------------------------------
        long distance = Utils.calculateDistanceInMeters(choosenPath);
        double energy = PhysicsMethods.predictEnergySpent(dummyClient,MapGraphAlgorithms.convertNodeListToEdgeList(company.getMapGraphDistance(),choosenPath),dummyVehicle)/ 3600000; //Joule to KwH
        Utils.getOutputPath(choosenPath,output,distance,energy,origin.getCoordinates().getAltitude()-end.getCoordinates().getAltitude(),1 );
        Utils.writeToFile(output,outputFileName);
        return distance;
    }

    /**
     * Returns the distance in meters of the shortest route between 2 parks
     * @param originLatitudeInDegrees lat of the origin park
     * @param originLongitudeInDegrees lon of the origin park
     * @param destinationLatitudeInDegrees lat of the end park
     * @param destinationLongitudeInDegrees lon of the end park
     * @param inputPOIs path constraints
     * @param outputFileName name of the output file
     * @return distance in meters
     * @throws SQLException exception that might occur when accessing the sql oracle database
     */
    public long shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(double originLatitudeInDegrees, double originLongitudeInDegrees, double destinationLatitudeInDegrees, double destinationLongitudeInDegrees, String inputPOIs, String outputFileName) throws SQLException, IOException, InvalidFileDataException {
        PointOfInterest originPark = company.getPoiAPI().fetchPoi(originLatitudeInDegrees,originLongitudeInDegrees);
        PointOfInterest endPark = company.getPoiAPI().fetchPoi(destinationLatitudeInDegrees,destinationLongitudeInDegrees);
        List<String[]> parsedData = Utils.parseDataFileAndValidateHeader(inputPOIs, LINE_SEPARATOR, COMMENT_TAG, HEADER);
        String[] line;
        List<Double> lat = new ArrayList<>();
        List<Double> lon = new ArrayList<>();

        int i=0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);
                lat.add(Double.parseDouble(line[POI_LAT]));
                lon.add(Double.parseDouble(line[POI_LON]));
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + inputPOIs);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + inputPOIs);
        }
        Set<PointOfInterest> pois = new HashSet<>();
        for(i = 0; i<lat.size(); i++) {
            pois.add(company.getPoiAPI().fetchPoi(lat.get(i),lon.get(i)));
        }
        List<LinkedList<PointOfInterest>> paths = new LinkedList<>();
        long distance = Math.round(MapGraphAlgorithms.shortestPathWithConstraints(company.getMapGraphDistance(),originPark,endPark,pois,paths)*1000); // km to meters
        Utils.sort(paths);

        if(paths.isEmpty()){
            return 0;
        }
        //dummy vehicle and client because they're not given----------------------
        ElectricScooter dummyVehicle = new ElectricScooter(12345, "PT596",5.3F,3.4F,
                500,true, ElectricScooterType.URBAN,75,
                1f, 1500);

        Client dummyClient = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',22.3F,
                true, new CreditCard("12341234123412"));
        //------------------------------------------------------------------------
        Utils.writeToFile(Utils.getOutputPaths(paths,distance,originPark.getCoordinates().getAltitude()-endPark.getCoordinates().getAltitude(),dummyClient,dummyVehicle), outputFileName);
        return distance;
    }
    /**
     * Returns the distance in meters of the shortest route between 2 parks
     * @param originParkIdentification origin park id
     * @param destinationParkIdentification destination park id
     * @param outputFileName name of the output file
     * @return distance in meters
     * @throws SQLException exception that might occur when accessing the sql oracle database
     */
    public long shortestRouteBetweenTwoParksFetchById(String originParkIdentification, String destinationParkIdentification, String outputFileName) throws SQLException, IOException {
        Park originPark = company.getParkAPI().fetchParkById(originParkIdentification);
        Park endPark = company.getParkAPI().fetchParkById(destinationParkIdentification);
        PointOfInterest origin = new PointOfInterest(originPark.getDescription(), originPark.getCoordinates());
        PointOfInterest end = new PointOfInterest(endPark.getDescription(), endPark.getCoordinates());
        LinkedList<PointOfInterest> path = new LinkedList<>();
        long distance = Math.round(MapGraphAlgorithms.shortestPath(company.getMapGraphDistance(),origin,end,path)*1000); // km to meters
        List<String> output = new LinkedList<>();
        //dummy vehicle and client because they're not given----------------------
        ElectricScooter dummyVehicle = new ElectricScooter(12345, "PT596",5.3F,3.4F,
                500,true, ElectricScooterType.URBAN,75,
                1f, 1500);

        Client dummyClient = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',22.3F,
                true, new CreditCard("12341234123412"));
        //------------------------------------------------------------------------
        double energy = PhysicsMethods.predictEnergySpent(dummyClient,MapGraphAlgorithms.convertNodeListToEdgeList(company.getMapGraphDistance(),path),dummyVehicle);
        Utils.getOutputPath(path,output,distance,energy,origin.getCoordinates().getAltitude()-end.getCoordinates().getAltitude(),1 );
        Utils.writeToFile(output,outputFileName);
        return distance;
    }

    /**
     * Returns the distance in meters of the shortest route between 2 parks
     * @param originParkIdentification origin park id
     * @param destinationParkIdentification destination park id
     * @param inputPOIs path constrains
     * @param outputFileName name of the output file
     * @return distance in meters
     * @throws SQLException exception that might occur when accessing the sql oracle database
     */
    public long shortestRouteBetweenTwoParksAndGivenPoisFetchById(String originParkIdentification,
                                                                  String destinationParkIdentification,
                                                                  String inputPOIs, String outputFileName) throws SQLException, IOException, InvalidFileDataException {
        Park originPark = company.getParkAPI().fetchParkById(originParkIdentification);
        Park endPark = company.getParkAPI().fetchParkById(destinationParkIdentification);
        PointOfInterest origin = new PointOfInterest(originPark.getDescription(), originPark.getCoordinates());
        PointOfInterest end = new PointOfInterest(endPark.getDescription(), endPark.getCoordinates());
        List<String[]> parsedData = Utils.parseDataFileAndValidateHeader(inputPOIs, LINE_SEPARATOR, COMMENT_TAG, HEADER);
        String[] line;
        List<Double> lat = new ArrayList<>();
        List<Double> lon = new ArrayList<>();

        int i=0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);
                lat.add(Double.parseDouble(line[POI_LAT]));
                lon.add(Double.parseDouble(line[POI_LON]));
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + inputPOIs);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + inputPOIs);
        }
        Set<PointOfInterest> pois = new HashSet<>();
        for(i = 0; i<lat.size(); i++) {
            pois.add(company.getPoiAPI().fetchPoi(lat.get(i),lon.get(i)));
        }
        List<LinkedList<PointOfInterest>> paths = new LinkedList<>();
        long distance = Math.round(MapGraphAlgorithms.shortestPathWithConstraints(company.getMapGraphDistance(),origin,end,pois,paths)*1000); // km to meters
        Utils.sort(paths);

        if(paths.isEmpty()){
            return 0;
        }
        //dummy vehicle and client because they're not given----------------------
        ElectricScooter dummyVehicle = new ElectricScooter(12345, "PT596",5.3F,3.4F,
                500,true, ElectricScooterType.URBAN,75,
                1f, 1500);

        Client dummyClient = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',22.3F,
                true, new CreditCard("12341234123412"));
        //------------------------------------------------------------------------
        Utils.writeToFile(Utils.getOutputPaths(paths,distance,originPark.getCoordinates().getAltitude()-endPark.getCoordinates().getAltitude(),dummyClient,dummyVehicle), outputFileName);
        return distance;
    }

/**
 * Returns the distance in meters of the shortest route between 2 parks
 * @param originParkIdentification description of the origin park
 * @param destinationParkIdentification description of the end park
 * @param outputFileName name of the output file
 * @return distance in meters
 * @throws SQLException exception that might occur when accessing the sql oracle database
 */
    public long shortestRouteBetweenTwoParksFetchByID(String originParkIdentification, String destinationParkIdentification, int numberOfPOIs, String outputFileName) throws IOException {
        PointOfInterest origin = company.getPoiAPI().fetchPoiByDescription(originParkIdentification);
        PointOfInterest end = company.getPoiAPI().fetchPoiByDescription(destinationParkIdentification);
        List<String> output = new LinkedList<>();
        if(origin == null || end == null){
            output.add("Não existem pontos de interesse com as coordenadas fornecidas");
            Utils.writeToFile(output,outputFileName);
            return 0;
        }
        ArrayList<LinkedList<PointOfInterest>> paths = MapGraphAlgorithms.allPaths(company.getMapGraphDistance(),origin,end);
        LinkedList<PointOfInterest> choosenPath = null;
        for(LinkedList<PointOfInterest> path : paths){
            if((path.size()-2) == numberOfPOIs){
                choosenPath = path;
                break;
            }
        }

        if(choosenPath == null){
            output.add("Não existem caminhos com esse tamanho");
            Utils.writeToFile(output,outputFileName);
            return 0;
        }
        //dummy vehicle and client because they're not given----------------------
        ElectricScooter dummyVehicle = new ElectricScooter(12345, "PT596",5.3F,3.4F,
                500,true, ElectricScooterType.URBAN,75,
                1f, 1500);

        Client dummyClient = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',22.3F,
                true, new CreditCard("12341234123412"));
        //------------------------------------------------------------------------
        long distance = Utils.calculateDistanceInMeters(choosenPath);
        double energy = PhysicsMethods.predictEnergySpent(dummyClient,MapGraphAlgorithms.convertNodeListToEdgeList(company.getMapGraphDistance(),choosenPath),dummyVehicle)/ 3600000; //Joule to KwH
        Utils.getOutputPath(choosenPath,output,distance,energy,origin.getCoordinates().getAltitude()-end.getCoordinates().getAltitude(),1 );
        Utils.writeToFile(output,outputFileName);
        return distance;
    }
}
