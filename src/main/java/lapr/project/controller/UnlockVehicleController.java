package lapr.project.controller;

import lapr.project.data.registers.Company;

import java.sql.SQLException;

public class UnlockVehicleController {
    private final Company company;

    public UnlockVehicleController(Company company) {
        this.company = company;
    }

    public void startTrip(String username, String vehicleDescription) throws SQLException {
        company.getTripAPI().startTrip(username, vehicleDescription);
    }
}
