package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

public class ShortestRouteBetweenParksController {
    private final Company company;
    private static final int POI_LAT = 0;
    private static final int POI_LON = 1;
    private static final int POI_ELEVATION = 2;
    private static final int POI_DESCRIPTION = 3;
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
    public long shortestRouteBetweenTwoParksFetchByCoordinates(double originLatitudeInDegrees, double originLongitudeInDegrees, double destinationLatitudeInDegrees, double destinationLongitudeInDegrees, String outputFileName) throws SQLException {
        Park originPark = company.getParkAPI().fetchParkByCoordinates(originLatitudeInDegrees,originLongitudeInDegrees);
        Park endPark = company.getParkAPI().fetchParkByCoordinates(destinationLatitudeInDegrees,destinationLongitudeInDegrees);
        LinkedList<PointOfInterest> path = new LinkedList<>();
        return Math.round(MapGraphAlgorithms.shortestPath(company.getMapGraphDistance(),originPark,endPark,path)*1000); // km to meters
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
    public long shortestRouteBetweenTwoParksAndGivenPoisFetchByCoordinates(double originLatitudeInDegrees, double originLongitudeInDegrees, double destinationLatitudeInDegrees, double destinationLongitudeInDegrees, String inputPOIs, String outputFileName) throws SQLException, FileNotFoundException, InvalidFileDataException {
        Park originPark = company.getParkAPI().fetchParkByCoordinates(originLatitudeInDegrees,originLongitudeInDegrees);
        Park endPark = company.getParkAPI().fetchParkByCoordinates(destinationLatitudeInDegrees,destinationLongitudeInDegrees);
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
        return Math.round(MapGraphAlgorithms.shortestPathWithConstraints(company.getMapGraphDistance(),originPark,endPark,pois,paths)*1000); // km to meters
    }
    /**
     * Returns the distance in meters of the shortest route between 2 parks
     * @param originParkIdentification origin park id
     * @param destinationParkIdentification destination park id
     * @param outputFileName name of the output file
     * @return distance in meters
     * @throws SQLException exception that might occur when accessing the sql oracle database
     */
    public long shortestRouteBetweenTwoParksFetchById(String originParkIdentification, String destinationParkIdentification, String outputFileName) throws SQLException{
        Park originPark = company.getParkAPI().fetchParkById(originParkIdentification);
        Park endPark = company.getParkAPI().fetchParkById(destinationParkIdentification);
        LinkedList<PointOfInterest> path = new LinkedList<>();
        return Math.round(MapGraphAlgorithms.shortestPath(company.getMapGraphDistance(),originPark,endPark,path)*1000); // km to meters
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
    public long shortestRouteBetweenTwoParksAndGivenPoisFetchById(String originParkIdentification, String destinationParkIdentification, String inputPOIs, String outputFileName) throws SQLException, FileNotFoundException, InvalidFileDataException {
        Park originPark = company.getParkAPI().fetchParkById(originParkIdentification);
        Park endPark = company.getParkAPI().fetchParkById(destinationParkIdentification);
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
        LinkedList<PointOfInterest> path = new LinkedList<>();
        List<LinkedList<PointOfInterest>> paths = new LinkedList<>();
        return Math.round(MapGraphAlgorithms.shortestPathWithConstraints(company.getMapGraphDistance(),originPark,endPark,pois,paths)*1000); // km to meters
    }
}
