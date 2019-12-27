package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.data.registers.ParkRegister;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetNumberOfVehiclesAtParkController {
    private final Company company;

    public GetNumberOfVehiclesAtParkController(Company company) {
        this.company = company;
    }

    public <T extends Vehicle> List<T> getVehiclesAtPark(String parkId, Class<T> classType) throws SQLException {
        return company.getParkRegister().fetchVehiclesAtPark(parkId, classType);
    }

    public <T extends Vehicle> List<T> getVehiclesAtPark(double lat, double lon, Class<T> classType) throws SQLException {
        ParkRegister parkRegister = company.getParkRegister();
        return getVehiclesAtPark(parkRegister.fetchParkByCoordinates(lat, lon).getId(), classType);
    }

    public <T extends Vehicle> void writeOutputFile(List<T> vehicles, String filePath) throws IOException {
        Collections.sort(vehicles, new VehicleDescriptionComparator());
        List<String> fileContent = new ArrayList<>();
        fileContent.add("bicycle description");
        for (T vehicle : vehicles) {
            fileContent.add(vehicle.getDescription());
        }
        Utils.writeToFile(fileContent, filePath);
    }
}
