package lapr.project.controller;

import lapr.project.data.registers.Company;

import java.sql.SQLException;

public class RemoveParkController {
    private final Company company;

    public RemoveParkController(Company company) {
        this.company = company;
    }

    public void removePark(String parkId) throws SQLException {
        company.getParkAPI().setAvailability(parkId, false);
    }
}
