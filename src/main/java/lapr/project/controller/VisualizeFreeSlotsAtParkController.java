package lapr.project.controller;

import lapr.project.data.registers.*;
import lapr.project.model.Trip;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.User;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;
import lapr.project.utils.UnregisteredDataException;

import java.sql.SQLException;

public class VisualizeFreeSlotsAtParkController {

    private final Company company;

    public VisualizeFreeSlotsAtParkController(Company company) {
        this.company = company;
    }

    /**
     * returns the amount of free slots for an electric scooter at a certain park
     *
     * @param parkIdentification park id
     * @param vehicleType        type of vehicle (bike / electric scooter)
     * @return the amount of free slots for the specified vehicle type
     */
    public int fetchFreeSlotsAtParkByType(String parkIdentification, VehicleType vehicleType) throws SQLException, UnregisteredDataException {
        ParkAPI parkAPI = company.getParkAPI();
        Park park = parkAPI.fetchParkById(parkIdentification);
        if (park == null)
            throw new UnregisteredDataException("park " + parkIdentification + " does not exist");
        return park.getAmountSlotsFreeByType(vehicleType);
    }

    /**
     * Fetches the amount of free slots at a given park for a user's currently loaned vehicle.
     *
     * @param parkId   park ID
     * @param username user's username
     * @return amount of free slots at a given park for a user's currently loaned vehicle
     */
    public int fetchFreeSlotsAtPark(String parkId, String username) throws SQLException, UnregisteredDataException {
        TripAPI tripAPI = company.getTripAPI();
        UserAPI userAPI = company.getUserAPI();
        VehicleAPI vehicleAPI = company.getVehicleAPI();

        User user = userAPI.fetchClientByUsername(username);
        if (user == null)
            throw new UnregisteredDataException("username " + username + " is not registered on the database");
        String userEmail = user.getEmail();

        Trip trip = tripAPI.fetchUnfinishedTrip(userEmail);
        if (trip == null)
            throw new UnregisteredDataException("user " + username + " is not currently on a trip");
        String vehicleDescription = trip.getVehicleDescription();

        Vehicle vehicle = vehicleAPI.fetchVehicle(vehicleDescription);
        if (vehicle == null)
            throw new IllegalStateException("vehicle " + vehicleDescription + " does not exist");
        VehicleType vehicleType = vehicle.getType();

        return fetchFreeSlotsAtParkByType(parkId, vehicleType);
    }
}
