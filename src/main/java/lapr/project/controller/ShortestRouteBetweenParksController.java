package lapr.project.controller;

import lapr.project.data.SortByHeightDescending;
import lapr.project.data.registers.Company;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ShortestRouteBetweenParksController {
    private final Company company;
    private static final int POI_LAT = 0;
    private static final int POI_LON = 1;
//    private static final int POI_ELEVATION = 2;
//    private static final int POI_DESCRIPTION = 3;
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
     * @param outputFileName name of the output file
     * @return distance in meters
     * @throws SQLException exception that might occur when accessing the sql oracle database
     */
    public long shortestRouteBetweenTwoParksFetchByCoordinates(double originLatitudeInDegrees, double originLongitudeInDegrees, double destinationLatitudeInDegrees, double destinationLongitudeInDegrees, String outputFileName) throws SQLException, IOException {
        PointOfInterest originPark = company.getPoiAPI().fetchPoi(originLatitudeInDegrees,originLongitudeInDegrees);
        PointOfInterest endPark = company.getPoiAPI().fetchPoi(destinationLatitudeInDegrees,destinationLongitudeInDegrees);
        LinkedList<PointOfInterest> path = new LinkedList<>();
        long distance = Math.round(MapGraphAlgorithms.shortestPath(company.getMapGraphDistance(),originPark,endPark,path)*1000); // km to meters
        List<String> output = new LinkedList<>();
        getOutputPath(path,output,distance,originPark.getCoordinates().getAltitude()-endPark.getCoordinates().getAltitude(),1 );
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
//        List<Integer> elevation = new ArrayList<>();
//        List<String> description = new ArrayList<>();

        int i=0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);

                lat.add(Double.parseDouble(line[POI_LAT]));
                lon.add(Double.parseDouble(line[POI_LON]));
//                elevation.add(Integer.parseInt(line[POI_ELEVATION]));
//                description.add(line[POI_DESCRIPTION]);
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
        sort(paths);
        Utils.writeToFile(getOutputPaths(paths,distance,originPark.getCoordinates().getAltitude()-endPark.getCoordinates().getAltitude()), outputFileName);
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
        getOutputPath(path,output,distance,origin.getCoordinates().getAltitude()-end.getCoordinates().getAltitude(),1 );
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
//        List<Integer> elevation = new ArrayList<>();
//        List<String> description = new ArrayList<>();

        int i=0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);

                lat.add(Double.parseDouble(line[POI_LAT]));
                lon.add(Double.parseDouble(line[POI_LON]));
//                elevation.add(Integer.parseInt(line[POI_ELEVATION]));
//                description.add(line[POI_DESCRIPTION]);
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
        sort(paths);
        Utils.writeToFile(getOutputPaths(paths,distance,originPark.getCoordinates().getAltitude()-endPark.getCoordinates().getAltitude()), outputFileName);
        return distance;
    }

    /**
     * Orders ascending by numbers of poi's, and then descending by height
     * @param paths list that contains list of paths
     */
    private void sort(List<LinkedList<PointOfInterest>> paths){
        Collections.sort(paths, new Comparator<LinkedList<PointOfInterest>>() {
            @Override
            public int compare(LinkedList<PointOfInterest> pointOfInterests, LinkedList<PointOfInterest> t1) {
                return pointOfInterests.size()-t1.size();
            }
        });
        for(LinkedList<PointOfInterest> p : paths){
            sortPois(p);
        }
    }

    /**
     * Orders the poi's according to the output/paths.csv file
     * @param path path containing the poi's
     */
    private void sortPois(LinkedList<PointOfInterest> path){
        Collections.sort(path, new SortByHeightDescending());
    }

    /**
     * Puts the information of the parks and poi's in a list so it can be printed
     * @param paths all the possible shortest paths
     * @param distance overall distance
     * @param elevation overall elevation
     * @return list of strings ready to be outputed with the information given in the output/path.csv
     */
    private List<String> getOutputPaths(List<LinkedList<PointOfInterest>> paths, long distance, int elevation){
        List<String> output = new LinkedList<>();
        int pathNumber = 1;
        for(LinkedList<PointOfInterest> path : paths){
            getOutputPath(path,output,distance,elevation,pathNumber);
        }
        return output;
    }

    /**
     * Orders by individual path
     * @param path path to output
     * @param output list of strings containing the output
     * @param distance overall distance
     * @param elevation overall elevation
     * @param pathNumber path number
     */
    private void getOutputPath(LinkedList<PointOfInterest> path, List<String> output, long distance, int elevation,int pathNumber){
        String line= String.format("Path %03d", pathNumber)+"\n";
        line += "total distance: "+distance+"\nelevation: "+ elevation+"\n";
        for(PointOfInterest poi : path) {
            line+=poi.getCoordinates().getLatitude()+";"+poi.getCoordinates().getLongitude()+"\n";
        }
        output.add(line);
    }
}
