package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.data.registers.ParkAPI;
import lapr.project.model.VehicleDescriptionComparator;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Note: Vehicles registered to be on a park are always available
 */
public class VisualizeVehiclesAtParkController {
    private final Company company;

    public VisualizeVehiclesAtParkController(Company company) {
        this.company = company;
    }

    public <T extends Vehicle> List<T> getVehiclesAtPark(String parkId, Class<T> classType) throws SQLException {
        return company.getParkAPI().fetchVehiclesAtPark(parkId, classType);
    }

    public <T extends Vehicle> List<T> getVehiclesAtPark(double lat, double lon, Class<T> classType) throws SQLException {
        ParkAPI parkAPI = company.getParkAPI();
        return getVehiclesAtPark(parkAPI.fetchParkByCoordinates(lat, lon).getId(), classType);
    }

    public void writeOutputFileBicycle(List<Bicycle> vehicles, String filePath) throws IOException {
        Collections.sort(vehicles, new VehicleDescriptionComparator());
        List<String> fileLines = new ArrayList<>();
        fileLines.add("bicycle description;wheel size");
        for (Bicycle vehicle : vehicles) {
            fileLines.add(String.format("%s;%d\"",vehicle.getDescription(), vehicle.getSize()));
        }
        Utils.writeToFile(fileLines, filePath);
    }

    public void writeOutputFileElectricScooter(List<ElectricScooter> vehicles, String filePath) throws IOException {
        Collections.sort(vehicles, new VehicleDescriptionComparator());
        List<String> fileLines = new ArrayList<>();
        fileLines.add("escooter description;type;actual battery capacity");
        for (ElectricScooter vehicle : vehicles) {
            fileLines.add(String.format("%s;%s;%d", vehicle.getDescription(), vehicle.getElectricScooterType().getFileName(), vehicle.getActualBatteryCapacity()));
        }
        Utils.writeToFile(fileLines, filePath);
    }
}
