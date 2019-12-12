/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.Park.Capacity;
import lapr.project.model.Park.Park;
import lapr.project.model.Vehicles.VehicleType;

/**
 *  This class regist and manipulates the data of all parks in data base
 */

public class ParkRegistor {

    DataHandler dataHandler;

    public ParkRegistor(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     *
     * Adds a park to the database
     * @param name Park name
     * @param cord Park coordinates
     * @return true if added with success the park, false otherwise
     */
    public boolean addPark(String name, Coordinates cord){
       try{ 
        double latitude = cord.getLatitude();
        double longitude = cord.getLongitude();
        double altitude = cord.getAltitude();
        PreparedStatement statement = dataHandler.prepareStatement("Insert into parks(park_name, latitude, longitude, altitude) values(?,?,?,?)");
        dataHandler.setString(statement, 1, name);
        dataHandler.setDouble(statement, 2, latitude);
        dataHandler.setDouble(statement, 3, longitude);
        dataHandler.setDouble(statement, 4, altitude);
       }catch(SQLException e){
           System.out.println(e.getMessage());
           return false;    
       } 
       return true;
    }
    
    /**
     * Removes a park from database
     * @param id  a given id
     * @return return true if successfully removed and false otherwise
     */
    public boolean removeParkById(int id) {
        //falta validar
       try{ 
        PreparedStatement statement = dataHandler.prepareStatement("DELETE FROM park WHERE park_id=?");
        dataHandler.setInt(statement, 1, id);
       }catch(SQLException e){
           System.out.println(e.getMessage());
           return false;
       } 
       return true;
    }
    
    /**
     * Fetch a parks with the name given by parameter 
     * @param name Name of park
     * @return Returns a list of parks with the same name (or just one)
     */
    public List<Park> fetchParkByName(String name) {
        String parkName = name.toLowerCase().trim();
        List<Park> parksSameNameOrNot = new ArrayList<>();
       try{ 
        PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where lower(park_name)=?");
        dataHandler.setString(statement, 1, name);
        ResultSet result = dataHandler.executeQuery(statement);
        while (result.next()) {
            //dados retirados da tabela 
            int parkId = dataHandler.getInt(result, 1);
            parkName = dataHandler.getString(result, 2);
            double latitude = dataHandler.getDouble(result, 3);
            double longitude = dataHandler.getDouble(result, 4);
            double altitude = dataHandler.getDouble(result, 5);
            Set<Capacity> listOfCapacitys=getListOfCapacitys(parkId);
            parksSameNameOrNot.add(new Park(parkName, new Coordinates(latitude, longitude, altitude), listOfCapacitys, parkId));
        }
       }catch(SQLException e){
           System.out.println(e.getMessage());
           return null;
       }
       return parksSameNameOrNot;
    }
    
    /**
     * Fetch a parkk by id 
     * @param id Park id
     * @return return a Park
     */
    public Park fetchParkById(int id){
      String name;  
      Coordinates cord; 
      try{  
        PreparedStatement statement = dataHandler.prepareStatement("Select * from parks where park_id=?");
        dataHandler.setInt(statement, 1, id);
        ResultSet resultSet = dataHandler.executeQuery(statement);
        name = dataHandler.getString(resultSet, 1);
        cord = new Coordinates(dataHandler.getDouble(resultSet, 2), dataHandler.getDouble(resultSet, 3), dataHandler.getDouble(resultSet, 4));
        return new Park(name, cord, getListOfCapacitys(id), id);
      }catch(SQLException e){
          System.out.println(e.getMessage());
          return null;
      }
    }
    
    /**
     * 
     * @param parkId Park id 
     * @return return a list of capacitys (of different types)
     */
    private Set<Capacity> getListOfCapacitys (int parkId){
      VehicleType vehicleType;
      Set<Capacity> capacity = new HashSet<>();
      try{  
        PreparedStatement statement = dataHandler.prepareStatement("Select * from park_capacity where park_id=?");
            dataHandler.setDouble(statement, 1, parkId);
            ResultSet resultForCapacity = dataHandler.executeQuery(statement);
            while (resultForCapacity.next()) {
                String vehicle_type_name = dataHandler.getString(resultForCapacity, 1);
                int park_capacity = dataHandler.getInt(resultForCapacity, 2);
                int amount_occupied = dataHandler.getInt(resultForCapacity, 3);
                if (vehicle_type_name.trim().equalsIgnoreCase("bicycle")) {
                    vehicleType = VehicleType.BICYCLE;
                } else {
                    vehicleType = VehicleType.ELECTRIC_SCOOTER;
                }
                capacity.add(new Capacity(park_capacity,amount_occupied,vehicleType));
            }
      }catch(SQLException e){
        System.out.println(e.getMessage());
        return null;
      }      
        return capacity;    
    }
}
