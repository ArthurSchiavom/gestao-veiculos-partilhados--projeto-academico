package lapr.project.controller;

import lapr.project.data.registers.Company;

import javax.mail.MessagingException;
import java.sql.SQLException;

public class LockVehicleController {
    private final Company company;

    public LockVehicleController(Company company) {
        this.company = company;
    }

    public void lockVehicle(String parkId, String vehicleDescription, boolean sendEmail) throws SQLException, MessagingException {
        company.getTripAPI().lockVehicle(parkId, vehicleDescription, sendEmail, true);
    }

    public void lockVehicle(double parkLatitude, double parkLongitude, String vehicleDescription, boolean sendEmail) throws SQLException, MessagingException {
        String parkId = company.getParkAPI().fetchParkByCoordinates(parkLatitude, parkLongitude).getId();
        lockVehicle(parkId, vehicleDescription, sendEmail);
    }
}
