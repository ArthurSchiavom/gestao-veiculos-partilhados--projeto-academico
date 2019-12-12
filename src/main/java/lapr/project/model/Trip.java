/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Calendar;
import lapr.project.model.Users.Client;
import lapr.project.model.Vehicles.Vehicle;
import lapr.project.model.Park.Park;

/**
 * Class that represents a trip
 *
 * @author Jose
 */
public class Trip {

    private Calendar startTime;
    private Calendar endTime;
    private int clientId;
    private int startParkId;
    private int endParkId;
    private int vehicleId;

    /**
     * Constructor that instantiates a trip
     *
     * @param startTime - the start time of the trip
     * @param endTime - the end time of the trip
     * @param client - the client that is making the trip
     * @param startPark - the starting point (park) for the trip
     * @param endPark - the ending point (park) for the trip
     * @param vehicle - the vehicle being used
     */
    public Trip(Calendar startTime, Calendar endTime, int client, int startPark, int endPark, int vehicle) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.clientId = client;
        this.startParkId = startPark;
        this.endParkId = endPark;
        this.vehicleId = vehicle;
    }

    /**
     * Constructor that instantiates a trip without the end park defined
     *
     * @param startTime - the start time of the trip
     * @param endTime - the end time of the trip
     * @param client - the client that is making the trip
     * @param startPark - the starting point (park) for the trip
     * @param vehicle - the vehicle being used
     */
    public Trip(Calendar startTime, Calendar endTime, int client, int startPark, int vehicle) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.clientId = client;
        this.startParkId = startPark;
        this.vehicleId = vehicle;
    }

    /**
     * Returns the start time of the current trip.
     *
     * @return the start time of the trip
     */
    public Calendar getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the end time of the current trip.
     *
     * @return the end time of the trip
     */
    public Calendar getEndTime() {
        return this.endTime;
    }

    /**
     * Returns the client doing the current trip.
     *
     * @return the client doing the trip
     */
    public int getClientId() {
        return this.clientId;
    }

    /**
     * Returns the starting point (park) of the current trip.
     *
     * @return
     */
    public int getStartParkId() {
        return this.startParkId;
    }

    /**
     * Returns the ending point (park) of the current trip.
     *
     * @return the ending point (park) of the trip
     */
    public int getEndParkId() {
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

}
