/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lapr.project.data.registers.Company;
import lapr.project.data.registers.TripAPI;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.users.Client;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;
import lapr.project.utils.physics.calculations.PhysicsMethods;

/**
 *
 * 
 */
public class CaloriesBurntBetweenTwoParksController {
       private final Company company;
       
       
       public CaloriesBurntBetweenTwoParksController(Company company) {
            this.company = company;
       }
       
       
       public double predictCaloriesBurnt(String username, String vehicleDescription, String startParkId, String endParkId, double latA, double lonA, double latB, double lonB, String filePath) throws SQLException, IOException {
           Client client = company.getUserAPI().fetchClientByUsername(username);
           Bicycle vehicle = (Bicycle) company.getVehicleAPI().fetchVehicle(vehicleDescription);
            PointOfInterest poiA = company.getPoiAPI().fetchPoi(latA, lonA);
            PointOfInterest poiB = company.getPoiAPI().fetchPoi(latB, lonB);

            return TripAPI.predictCalories(client, vehicle, poiA, poiB, filePath);
       }
}
