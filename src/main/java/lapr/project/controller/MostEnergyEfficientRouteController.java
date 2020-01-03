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

    public LinkedList<Path> mostEnergyEfficientRouteBetweenTwoParks(String originParkIdentification,String destinationParkIdentification,String typeOfVehicle,String vehicleSpecs,String username,String outputFileName) throws SQLException {
        Park parkStart = company.getParkAPI().fetchParkById(originParkIdentification);
        Park parkEnd = company.getParkAPI().fetchParkById(destinationParkIdentification);
        Client client = company.getUserAPI().fetchClientByUsername(username);

        return null;
    }

}
