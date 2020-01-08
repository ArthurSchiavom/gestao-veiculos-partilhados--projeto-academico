/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.controller;

import java.io.IOException;
import java.sql.SQLException;
import lapr.project.data.registers.Company;
import lapr.project.data.registers.TripAPI;

/**
 * Class that allows to interact with the calories burnt calculation method
 * 
 */
public class CaloriesBurntBetweenTwoParksController {
       private final Company company;
       
       /**
        * Instantiates the controller
        * @param company 
        */
       public CaloriesBurntBetweenTwoParksController(Company company) {
            this.company = company;
       }
       
       /**
        * Predicts the calories burnt between two paths, considering the less energetical path
        * @param username - the username of the client
        * @param vehicleDescription - the description of the vehicle that will be used
        * @param latA -  latitude of the starting point
        * @param lonA - longitude of the starting point
        * @param latB - latitude of the ending point
        * @param lonB - longitude of the ending point
        * @return
        * @throws SQLException
        * @throws IOException 
        */
       public double predictCaloriesBurnt(String username, String vehicleDescription, double latA, double lonA, double latB, double lonB) throws SQLException, IOException {


            return TripAPI.predictCalories(username, vehicleDescription, latA, lonA,latB, lonB);
       }
}
