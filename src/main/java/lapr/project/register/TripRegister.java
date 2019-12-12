package lapr.project.register;

import lapr.project.model.Trip;

import java.util.Calendar;

public class TripRegister {
    public Trip createNewTrip(Calendar startTime, int clientId, int startParkId, int endParkId, int vehicleId) {
        return new Trip(startTime, clientId, startParkId, endParkId, vehicleId);
    }

    public Trip createNewTrip(Calendar startTime, int clientId, int startParkId, int vehicleId) {
        return new Trip(startTime, clientId, startParkId, vehicleId);
    }

    public Trip createNewTrip(Calendar startTime, Calendar endTime, int clientId, int startParkId, int endParkId, int vehicleId) {
        return new Trip(startTime, endTime, clientId, startParkId, endParkId, vehicleId);
    }
}
