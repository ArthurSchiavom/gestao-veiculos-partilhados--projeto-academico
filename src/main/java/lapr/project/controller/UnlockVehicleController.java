package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.data.registers.ParkAPI;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.Path;
import lapr.project.model.Trip;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.ElectricScooter;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UnlockVehicleController {
    private final Company company;

    public UnlockVehicleController(Company company) {
        this.company = company;
    }

    /**
     * Unlocks a vehicle given it's description
     *
     * @param username           the user that requested the unlock
     * @param vehicleDescription the description of the vehicle that was requested
     * @throws SQLException if any errors happen
     */
    public void startTrip(String username, String vehicleDescription) throws SQLException {
        company.getTripAPI().startTrip(username, vehicleDescription);
    }

    /**
     * Unlocks the vehicle with the most battery in a park
     *
     * @param park           Park Identification where to unlock escooter.
     * @param username       User that requested the unlock.
     * @param exportFileName Write the unlocked vehicle information to a file,
     *                       according to file output/escooters.csv.
     */
    public void startTripPark(String username, String park, String exportFileName)
            throws SQLException {
        //TODO: Implement file export
        String vehicleDesc = company.getParkAPI().fetchScooterHighestBattery(park);
        company.getTripAPI().startTrip(username, vehicleDesc);
    }

    /**
     * Unlocks any escooter at one park that allows travelling to the
     * destination.
     *
     * @param park                      Park Identification where to unlock escooter.
     * @param username                  User that requested the unlock.
     * @param destinyLatitudeInDegrees  Destiny latitude in Decimal Degrees.
     * @param destinyLongitudeInDegrees Destiny longitude in Decimal Degrees.
     * @param outputFileName            Write the unlocked vehicle information to a file,
     *                                  according to file output/escooters.csv.
     */
    public void startTripParkDest(String park, String username,
                                  double destinyLatitudeInDegrees, double destinyLongitudeInDegrees,
                                  String outputFileName) throws SQLException {
		Park origPark = Company.getInstance().getParkAPI().fetchParkById(park);
		Park destPark = Company.getInstance().getParkAPI().fetchParkByCoordinates(destinyLatitudeInDegrees,
				destinyLongitudeInDegrees);
        List<ElectricScooter> availableVehiclesAtPark = Company.getInstance().getParkAPI().fetchVehiclesAtPark(park, ElectricScooter.class);
		LinkedList<PointOfInterest> shortestPathPOI = new LinkedList<>();
        MapGraphAlgorithms.shortestPath(Company.getInstance().getMapGraphDistance(), origPark,  destPark, shortestPathPOI);
		LinkedList<Path> shortestPath = MapGraphAlgorithms.convertNodeListToEdgeList(Company.getInstance().getMapGraphDistance(), shortestPathPOI);
        List<ElectricScooter> filteredVehiclesAtPark = Trip.filterScootersWithAutonomy(availableVehiclesAtPark, shortestPath);
        String vehicleDesc = filteredVehiclesAtPark.get(0).getDescription();
        company.getTripAPI().startTrip(username, vehicleDesc);
		//TODO: Implement file export
    }
}
