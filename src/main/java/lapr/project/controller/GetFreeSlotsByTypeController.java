package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.VehicleType;
import java.sql.SQLException;

public class GetFreeSlotsByTypeController {

    private final Company company;

    public GetFreeSlotsByTypeController(Company company) {
        this.company = company;
    }

    /**
     * returns the amount of free slots for an electric scooter at a certain park
     * @param parkIdentification park id
     * @param username username who requested to see the amount of free slots
     * @param vehicleType type of vehicle (bike / electric scooter)
     * @return the amount of free slots for the specified vehicle type
     */
    public int getFreeSlotsByType(String parkIdentification, String username, VehicleType vehicleType) throws SQLException {
        return company.getParkRegister().fetchParkById(parkIdentification).getAmountSlotsFreeByType(vehicleType);
    }
}
