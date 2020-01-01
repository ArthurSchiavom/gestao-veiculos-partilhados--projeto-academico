package lapr.project.controller;

import lapr.project.data.registers.Company;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class GetListOfVehiclesNotAvailableController {
    private final Company company;

    public GetListOfVehiclesNotAvailableController(Company company){this.company = company;}

    public void getListOfVehiclesNotAvailable(LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        company.getTripAPI().getListOfVehiclesNotAvailable(startTime,endTime);
    }
}
