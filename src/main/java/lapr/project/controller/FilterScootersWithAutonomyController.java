package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.data.registers.ParkAPI;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.Path;
import lapr.project.model.Trip;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilterScootersWithAutonomyController {

    /**
     * Given a list of scooters and a trip, checks which scooters can make the trip
     *
     * @param scooters the list of scooters
     * @param trip     the trip to take
     * @return the scooters that have enough autonomy to make the whole trip
     */
    public List<ElectricScooter> filterScootersWithAutonomy(List<ElectricScooter> scooters, List<Path> trip) {
        return Trip.filterScootersWithAutonomy(scooters, trip);
    }

    /**
     * Suggest escooters with enough energy + 10% to go from one
     * Park to another.
     *
     * @param park       Park Identification where to unlock escooter.
     * @param username   User that requested the information.
     * @param destLat    Destiny latitude in Decimal Degrees.
     * @param destLon    Destiny longitude in Decimal Degrees.
     * @param outputFile Write the unlocked vehicle information to a file,
     *                   according to file output/escooters.csv.
     * @return The number of suggested vehicles.
     */
    public int suggestScootersBetweenParks(String park, String username, double destLat, double destLon, String outputFile) throws SQLException, IOException {
        final String FAIL_MESSAGE = "Couldn't find scooters to suggest between the two parks.";
        List<String> fileLines = new ArrayList<>();
        List<ElectricScooter> filteredScooters = ParkAPI.filterScootersWithAutonomy(username, park, destLat, destLon);
        if(filteredScooters.isEmpty()) {
            fileLines.add(FAIL_MESSAGE);
            Utils.writeToFile(fileLines, outputFile);
            return 0;
        }
        fileLines.add("escooter description;type;actual battery capacity");
        for(ElectricScooter scooter : filteredScooters) {
            fileLines.add(scooter.generateExportString());
        }
        Utils.writeToFile(fileLines, outputFile);
        return filteredScooters.size();
    }
}
