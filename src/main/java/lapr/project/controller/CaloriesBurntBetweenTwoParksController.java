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
 *
 * 
 */
public class CaloriesBurntBetweenTwoParksController {
       private final Company company;
       
       
       public CaloriesBurntBetweenTwoParksController(Company company) {
            this.company = company;
       }
       
       
       public double predictCaloriesBurnt(String username, String vehicleDescription, double latA, double lonA, double latB, double lonB) throws SQLException, IOException {


            return TripAPI.predictCalories(username, vehicleDescription, latA, lonA,latB, lonB);
       }
}
