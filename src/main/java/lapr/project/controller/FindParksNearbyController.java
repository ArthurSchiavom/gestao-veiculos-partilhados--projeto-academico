package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Park;

import java.sql.SQLException;
import java.util.List;

public class FindParksNearbyController {
    private final Company company;

    public FindParksNearbyController(Company company) {
        this.company = company;
    }

    /**
     * Retrieves the parks within a certain distance from a given point.
     *
     * <h1></h1><b>Important: </b>We are running through every park in the sql table instead of parsing it to a tree because, it has a smaller complexity initially
     * and if we were to need the tree again, it could be outdated by then, so we would have to again load the tree with all the information
     * in the table, making it higher complexity than simply iterating all of them</h1>
     *
     * @param lat latitude of the point
     * @param lon longitude of the point
     * @param radius the max distance from the point to a park, use 0 for the default value
     * @return parks within the given radius
     * @throws SQLException in case a database access error occurs
     */
    public List<Park> findParksNearby(double lat, double lon, double radius) throws SQLException {
        // Note: the height is not used for distance calculations between parks
        return company.getParkRegister().retrieveParksInRadius(new Coordinates(lat, lon, 0), radius);
    }
}
