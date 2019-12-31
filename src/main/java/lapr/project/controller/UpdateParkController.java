package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.VehicleType;

import java.sql.SQLException;

public class UpdateParkController {
    private final Company company;

    public UpdateParkController(Company company){this.company = company;}

    public void updateParkId(String currentId, String newId) throws SQLException {
        company.getParkAPI().updateParkId(currentId,newId);
    }

    public void updateParkCapacity(String parkId, VehicleType vehicleType, int newCapacity) throws SQLException {
        company.getParkAPI().updateParkCapacity(parkId,vehicleType,newCapacity);
    }

    public void updateParkInputCurrent(String parkId, float newInputCurrent) throws SQLException {
        company.getParkAPI().updateParkInputCurrent(parkId,newInputCurrent);
    }

    public void updateParkInputVoltage(String parkId, float newInputVoltage) throws SQLException {
        company.getParkAPI().updateParkInputVoltage(parkId,newInputVoltage);
    }

    public void updateParkDescription(String parkId, String newDescription) throws SQLException {
        company.getParkAPI().updateParkDescription(parkId,newDescription);
    }
}
