package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.data.registers.VehicleAPI;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Controller for most energy efficient route between 2 parks
 */
public class MostEnergyEfficientRouteController {

    private final Company company;
    private final VehicleAPI vehicleAPI;
    private static final String HEADER = "latitude;longitude;elevation;poi description";
    private static final String LINE_SEPARATOR = ";";
    private static final String COMMENT_TAG = "#";
    private static final int POI_LAT = 0;
    private static final int POI_LON = 1;

    public MostEnergyEfficientRouteController(Company company) {
        this.company = company;
        vehicleAPI = company.getVehicleAPI();
    }

    /**
     *
     * @param originLatitudeInDegrees latitude
     * @param originLongitudeInDegrees longitude
     * @param destinationLatitudeInDegrees latitude end
     * @param destinationLongitudeInDegrees longitude end
     * @param username username of the client
     * @return energy spent in KwH
     * @throws SQLException in case of an sql exception
     */
    public double calculateElectricalEnergyToTravelFromOneLocationToAnother(double originLatitudeInDegrees,double originLongitudeInDegrees, double destinationLatitudeInDegrees,double destinationLongitudeInDegrees,String username) throws SQLException {
        //dummy vehicle and client because they're not given----------------------
        ElectricScooter dummyVehicle = new ElectricScooter(12345, "PT596",5.3F,3.4F,
                500,true, ElectricScooterType.URBAN,75,
                1f, 1500);
        Park parkStart = company.getParkAPI().fetchParkByCoordinates(originLatitudeInDegrees,originLongitudeInDegrees);
        Park parkEnd = company.getParkAPI().fetchParkByCoordinates(destinationLatitudeInDegrees,destinationLongitudeInDegrees);
        if(parkStart == null || parkEnd == null){
            return 0;
        }
        PointOfInterest start = new PointOfInterest(parkStart.getDescription(),parkStart.getCoordinates());
        PointOfInterest end = new PointOfInterest(parkEnd.getDescription(),parkEnd.getCoordinates());
        Client client = company.getUserAPI().fetchClientByUsername(username);
        LinkedList<PointOfInterest> path = new LinkedList<>();
        return MapGraphAlgorithms.shortestPath(company.initializeEnergyGraph(client,dummyVehicle),start,end,path)/3600000; //Joules to KwH
    }

    /**
     * Calculates the most energy efficient route between two parks
     * @param originParkIdentification the origin park description
     * @param destinationParkIdentification the destination park description
     * @param typeOfVehicle the type of vehicle
     * @param vehicleSpecs the specs of the vehicle
     * @param username the username
     * @param outputFileName path and name of the output file name
     * @return list of smallest paths
     * @throws SQLException in case an exception occurs
     */
    public long mostEnergyEfficientRouteBetweenTwoParks(String originParkIdentification,
                                                                    String destinationParkIdentification,
                                                                    String typeOfVehicle,
                                                                    String vehicleSpecs,
                                                                    String username,
                                                                    String outputFileName) throws SQLException, IOException {
        boolean isBicycle;
        if(typeOfVehicle.equalsIgnoreCase("bicycle")){
            isBicycle = true;
        }else if(typeOfVehicle.equalsIgnoreCase("escooter")){
            isBicycle=false;
        }else{
            return 0;
        }

        Vehicle vehicle = vehicleAPI.fetchVehicleBySpecs(isBicycle,vehicleSpecs);
        Park parkStart = company.getParkAPI().fetchParkById(originParkIdentification);
        Park parkEnd = company.getParkAPI().fetchParkById(destinationParkIdentification);
        if(parkStart == null || parkEnd == null || vehicle == null){
            LinkedList<String> output = new LinkedList<>();
            output.add("Não existem pontos de interesse com as coordenadas fornecidas ou/e bicicletas com as specs fornecidas");
            Utils.writeToFile(output,outputFileName);
            return 0;
        }
        PointOfInterest start = new PointOfInterest(parkStart.getDescription(),parkStart.getCoordinates());
        PointOfInterest end = new PointOfInterest(parkEnd.getDescription(),parkEnd.getCoordinates());
        Client client = company.getUserAPI().fetchClientByUsername(username);
        LinkedList<PointOfInterest> path = new LinkedList<>();
        double energy = MapGraphAlgorithms.shortestPath(company.initializeEnergyGraph(client,vehicle),start,end,path)/3600000; //Joules to KwH
        List<String> output = new LinkedList<>();
        long distance = Utils.calculateDistanceInMeters(path);
        Utils.getOutputPath(path,output,distance,energy,start.getCoordinates().getAltitude()-end.getCoordinates().getAltitude(),1 );
        Utils.writeToFile(output,outputFileName);
        return distance;
    }

    /**
     * Calculate the most energetically efficient route from one park to
     * another with sorting options.
     *
     * @param originParkIdentification Origin Park Identification.
     * @param destinationParkIdentification Destination Park
     *                                      Identification.
     *
     * @param typeOfVehicle The type of vehicle required e.g. "bicycle" or
     *                      "escooter".
     * @param vehicleSpecs The specs for the vehicle e.g. "16", "19",
     *                    "27" or any other number for bicyles and "city" or
     *                     "off-road" for any escooter.
     * @param username The user that asked for the routes.
     * @param maxNumberOfSuggestions The maximum number of suggestions to
     *                               provide.
     * @param ascendingOrder If routes should be ordered by ascending or
     *                       descending order
     * @param sortingCriteria The criteria to use for ordering "energy",
     *                        "shortest_distance", "number_of_points".
     * @param inputPOIs Path to file that contains the POIs that the route
     *                  must go through, according to file input/pois.csv.
     *                  By default, the file is empty.
     * @param outputFileName Write to the file the Route between two parks
     *                   according to file output/paths.csv. More than one
     *                   path may exist.
     * @return The number of suggestions
     */
    public int suggestRoutesBetweenTwoLocations(String originParkIdentification,String destinationParkIdentification,String typeOfVehicle,String vehicleSpecs,String username,int maxNumberOfSuggestions,boolean ascendingOrder,String sortingCriteria,String inputPOIs,String outputFileName) throws SQLException, IOException, InvalidFileDataException {
        boolean isBicycle;
        if(typeOfVehicle.equalsIgnoreCase("bicycle")){
            isBicycle = true;
        }else if(typeOfVehicle.equalsIgnoreCase("escooter")){
            isBicycle=false;
        }else{
            return 0;
        }

        Vehicle vehicle = vehicleAPI.fetchVehicleBySpecs(isBicycle,vehicleSpecs);
        Park parkStart = company.getParkAPI().fetchParkById(originParkIdentification);
        Park parkEnd = company.getParkAPI().fetchParkById(destinationParkIdentification);
        if(parkStart == null || parkEnd == null || vehicle == null){
            LinkedList<String> output = new LinkedList<>();
            output.add("Não existem pontos de interesse com as coordenadas fornecidas ou/e bicicletas com as specs fornecidas");
            Utils.writeToFile(output,outputFileName);
            return 0;
        }

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
        PointOfInterest start = new PointOfInterest(parkStart.getDescription(),parkStart.getCoordinates());
        PointOfInterest end = new PointOfInterest(parkEnd.getDescription(),parkEnd.getCoordinates());
        Client client = company.getUserAPI().fetchClientByUsername(username);

        List<LinkedList<PointOfInterest>> allPaths = new LinkedList<>();
        MapGraphAlgorithms.shortestPathWithConstraints(company.initializeEnergyGraph(client,vehicle),start,end,pois,allPaths);

        List<LinkedList<PointOfInterest>> allPathsFiltered = new LinkedList<>();

        for(LinkedList<PointOfInterest> path : allPaths){
            if(allPathsFiltered.size() >= maxNumberOfSuggestions){
                break;
            }
            allPathsFiltered.add(path);
        }


        long distance = Utils.calculateDistanceInMeters(allPathsFiltered.get(0));
        Utils.writeToFile(Utils.getOutputPaths(allPathsFiltered, distance, start.getCoordinates().getAltitude() - end.getCoordinates().getAltitude(), client, vehicle), outputFileName);

        return allPathsFiltered.size();
    }
}
