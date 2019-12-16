/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class that represents a trip
 */
public class Trip {

    private LocalDateTime startTime;
    private LocalDateTime endTime = null;
    private String clientEmail;
    private String startParkId;
    private String endParkId = null;
    private int vehicleId;

    /**
     * Constructor that instantiates a trip
     *
     * @param startTime - the start time of the trip
     * @param endTime - the end time of the trip
     * @param clientEmail - the email of the client that is taking the trip
     * @param startParkId - the starting point (park) for the trip
     * @param endParkId - the ending point (park) for the trip
     * @param vehicle - the vehicle being used
     */
    public Trip(LocalDateTime startTime, LocalDateTime endTime, String clientEmail, String startParkId, String endParkId, int vehicle) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.clientEmail = clientEmail;
        this.startParkId = startParkId;
        this.endParkId = endParkId;
        this.vehicleId = vehicle;
    }

    /**
     * Constructor that instantiates a trip without the end park and the end
     * time defined
     *
     * @param startTime - the start time of the trip
     * @param clientEmail - the email of the client that is taking the trip
     * @param startParkId - the starting point (park) for the trip
     * @param vehicle - the vehicle being used
     */
    public Trip(LocalDateTime startTime, String clientEmail, String startParkId, int vehicle) {
        this.startTime = startTime;
        this.clientEmail = clientEmail;
        this.startParkId = startParkId;
        this.vehicleId = vehicle;
    }

    /**
     * Constructor that instantiates a trip without the end time defined
     *
     * @param startTime - the start time of the trip
     * @param clientEmail - the email of the client that is taking the trip
     * @param startParkId - the starting point (park) for the trip
     * @param endParkId - the ending point (park) for the trip
     * @param vehicle - the vehicle being used
     */
    public Trip(LocalDateTime startTime, String clientEmail, String startParkId, String endParkId, int vehicle) {
        this.startTime = startTime;
        this.clientEmail = clientEmail;
        this.startParkId = startParkId;
        this.endParkId = endParkId;
        this.vehicleId = vehicle;
    }

    /**
     * Returns the start time of the current trip.
     *
     * @return the start time of the trip
     */
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the end time of the current trip.
     *
     * @return the end time of the trip
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**
     * Returns the client doing the current trip.
     *
     * @return the client doing the trip
     */
    public String getClientEmail() {
        return this.clientEmail;
    }

    /**
     * Returns the starting point (park) of the current trip.
     *
     * @return the starting point (park) of the current trip.
     */
    public String getStartParkId() {
        return this.startParkId;
    }

    /**
     * Returns the ending point (park) of the current trip.
     *
     * @return the ending point (park) of the trip
     */
    public String getEndParkId() {
        return this.endParkId;
    }

    /**
     * Returns the vehicle being used o the current trip.
     *
     * @return the vehicle being used on the trip
     */
    public int getVehicleId() {
        return this.vehicleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trip trip = (Trip) o;
        return startTime.equals(trip.startTime)
                && clientEmail.equals(trip.clientEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, clientEmail);
    }
}
