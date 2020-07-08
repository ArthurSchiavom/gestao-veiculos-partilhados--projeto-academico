/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class GetScootersWithNotEnoughCapacityController {

    private final Company company;

    public GetScootersWithNotEnoughCapacityController(Company company) {
        this.company = company;
    }

    public void writeOutputFile(int km, String filePath) throws SQLException, IOException {
         List<Vehicle> allScooters =  company.getVehicleAPI().fetchAllVehicles(VehicleType.ELECTRIC_SCOOTER);
         List<ElectricScooter> scootersUncapable = new ArrayList<>();
         ElectricScooter scooter;
         
         for (Vehicle vehicle : allScooters) {
            scooter = (ElectricScooter) vehicle;
            if (!scooter.hasAutonomyFlat(km) && scooter.isAvailable() == true) {
                scootersUncapable.add(scooter);
            }
         }
         
         List<String> fileContent = new ArrayList<>();
        fileContent.add("Scooters with not enough autonomy to make " + km + "km:");
        
       for (ElectricScooter scooterUncapable : scootersUncapable) {
           fileContent.add("scooter " + scooterUncapable.getUniqueNumber());
       }
      
       Utils.writeToFile(fileContent, filePath);
    }
}
