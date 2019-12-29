package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.Path;
import lapr.project.model.Trip;
import lapr.project.model.vehicles.ElectricScooter;

import java.util.List;

public class FilterScootersWithAutonomyController {

    public List<ElectricScooter> filterScootersWithAutonomy (List<ElectricScooter> scooters, List<Path>trip){
       return Trip. filterScootersWithAutonomy(scooters,trip);
    }
}
