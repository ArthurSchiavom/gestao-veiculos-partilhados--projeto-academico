package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.Vehicle;

import java.sql.SQLException;
import java.util.List;

public class VisualizeAvailableVehiclesAtParkController {
    private final Company company;

    public VisualizeAvailableVehiclesAtParkController(Company company) {
        this.company = company;
    }

    public List<Vehicle> fetchAvailableVehiclesAtPark(String parkId) throws SQLException {
        return company.getParkAPI().fetchAvailableVehiclesAtPark(parkId, Vehicle.class);
    }
}
