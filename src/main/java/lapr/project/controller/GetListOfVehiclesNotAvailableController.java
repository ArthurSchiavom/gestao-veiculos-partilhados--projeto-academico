package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.Trip;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class GetListOfVehiclesNotAvailableController {
    private final Company company;

    public GetListOfVehiclesNotAvailableController(Company company){this.company = company;}

    public List<Trip> getListOfVehiclesNotAvailable(LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        return company.getTripAPI().getListOfVehiclesNotAvailable(startTime,endTime);
    }
}
