package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.Path;
import lapr.project.model.Trip;
import lapr.project.model.vehicles.ElectricScooter;

import java.util.List;

public class FilterScootersWithAutonomyController {

    /**
     * Given a list of scooters and a trip, checks which scooters can make the trip
     * @param scooters the list of scooters
     * @param trip the trip to take
     * @return the scooters that have enough autonomy to make the whole trip
     */
    public List<ElectricScooter> filterScootersWithAutonomy (List<ElectricScooter> scooters, List<Path>trip){
       return Trip.filterScootersWithAutonomy(scooters,trip);
    }
}
