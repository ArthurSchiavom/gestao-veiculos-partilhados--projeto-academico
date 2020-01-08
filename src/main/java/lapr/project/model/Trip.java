/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.data.registers.Company;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.utils.physics.calculations.PhysicsMethods;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Class that represents a trip
 */
public class Trip {

    private final Timestamp startTime;
    private Timestamp endTime = null;
    private final String clientEmail;
    private final String startParkId;
    private String endParkId = null;
    private final String vehicleDescription;

    /**
     * Constructor that instantiates a trip
     *
     * @param startTime          - the start time of the trip
     * @param endTime            - the end time of the trip
     * @param clientEmail        - the email of the client that is taking the trip
     * @param startParkId        - the starting point (park) for the trip
     * @param endParkId          - the ending point (park) for the trip
     * @param vehicleDescription description of the vehicle in use
     */
    public Trip(Timestamp startTime, Timestamp endTime, String clientEmail, String startParkId, String endParkId, String vehicleDescription) {
        if (startTime == null || clientEmail == null || clientEmail.isEmpty() || startParkId == null || startParkId.isEmpty())
            throw new IllegalArgumentException("Null or empty elements are not allowed");
        this.startTime = new Timestamp(startTime.getTime());
        if(endTime == null) {
            this.endTime = null;
        } else {
            this.endTime = new Timestamp(endTime.getTime());
        }
        this.clientEmail = clientEmail;
        this.startParkId = startParkId;
        this.endParkId = endParkId;
        this.vehicleDescription = vehicleDescription;
    }

    /**
     * Constructor that instantiates a trip without the end park and the end
     * time defined
     *
     * @param startTime          - the start time of the trip
     * @param clientEmail        - the email of the client that is taking the trip
     * @param startParkId        - the starting point (park) for the trip
     * @param vehicleDescription - the vehicle being used
     */
    public Trip(Timestamp startTime, String clientEmail, String startParkId, String vehicleDescription) {
        this.startTime = new Timestamp(startTime.getTime());
        this.clientEmail = clientEmail;
        this.startParkId = startParkId;
        this.vehicleDescription = vehicleDescription;
    }

    /**
     * Returns the start time of the current trip.
     *
     * @return the start time of the trip
     */
    public Timestamp getStartTime() {
        return new Timestamp(startTime.getTime());
    }

    /**
     * Returns the end time of the current trip.
     *
     * @return (1) trip end time or (2) null if the trip hasn't ended yet
     */
    public Timestamp getEndTime() {
        if(endTime == null) {
            return null;
        }
        return new Timestamp(endTime.getTime());
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
     * @return (1) trip's end park (2) null if the trip hasn't ended yet
     */
    public String getEndParkId() {
        return this.endParkId;
    }

    public String getVehicleDescription() {
        return vehicleDescription;
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

    /**
     * @param listScooters all the scooters in the park
     * @param trip         the path of the trip
     * @return a list of scooters which are able to comply the trip and keep 10% of the battery capacity
     */
    public static List<ElectricScooter> filterScootersWithAutonomy(List<ElectricScooter> listScooters, List<Path> trip) {
        List<ElectricScooter> scooters = new ArrayList<>();
        for (ElectricScooter electricScooter : listScooters) {
            if (electricScooter.hasAutonomy(trip)) {
                scooters.add(electricScooter);
            }
        }
        return scooters;
    }

    /**
     * Calculates this trip's cost
     *
     * @return trip's cost; 0 if it didn't end yet
     */
    public double calculateTripCost() {
        if (endTime == null)
            return 0;

        double cost = (((double) (endTime.getTime() - startTime.getTime()) / 1000) / 60 - 60) * 0.025; // 0.025 = 1.5/60 = preço por minuto dado que o custo é 1.5 euros por hora
        if (cost <= 0)
            return 0;

        return cost;
    }

    /**
     * Calculates the trip's duration at the moment.
     *
     * @return trip duration at the moment, if the trip hasn't ended, the duration will vary according to when it's called.
     */
    public long getTripDurationMillis() {
        if (endTime != null)
            return endTime.getTime() - startTime.getTime();

        Calendar cal = Calendar.getInstance();
        long val = cal.getTimeInMillis() - startTime.getTime();
        return val;
    }
}
