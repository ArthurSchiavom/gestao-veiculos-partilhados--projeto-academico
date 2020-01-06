package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.Path;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Controller for most energy efficient route between 2 parks
 */
public class MostEnergyEfficientRouteController {

    private final Company company;

    public MostEnergyEfficientRouteController(Company company) {
        this.company = company;
    }

    //TODO: teacher has to give the missing variables, or a way to identify the vehicle to perform the required calculations

    /**
     * Calculates the most energy efficient route between two parks
     * @param originParkIdentification the origin park description
     * @param destinationParkIdentification the destination park description
     * @param typeOfVehicle the type of vehicle
     * @param vehicleSpecs the specs of the vehicle
     * @param username the username
     * @param outputFileName
     * @return
     * @throws SQLException
     */
    public LinkedList<Path> mostEnergyEfficientRouteBetweenTwoParks(String originParkIdentification,
                                                                    String destinationParkIdentification,
                                                                    String typeOfVehicle,
                                                                    String vehicleSpecs,
                                                                    String username,
                                                                    String outputFileName) throws SQLException {
        Park parkStart = company.getParkAPI().fetchParkById(originParkIdentification);
        Park parkEnd = company.getParkAPI().fetchParkById(destinationParkIdentification);
        Client client = company.getUserAPI().fetchClientByUsername(username);

        return null;
    }

}
