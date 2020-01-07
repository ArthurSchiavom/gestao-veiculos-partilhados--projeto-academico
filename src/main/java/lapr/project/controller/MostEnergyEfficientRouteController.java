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
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for most energy efficient route between 2 parks
 */
public class MostEnergyEfficientRouteController {

    private final Company company;
    private final VehicleAPI vehicleAPI;

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
}
