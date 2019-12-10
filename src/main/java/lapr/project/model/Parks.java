/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

/**
 *
 * @author kevin
// */
public class Parks {
    private int park_id;
    private String name;
    private Coordinates cord;
    private ParkCapacity bikeCapacity;
    private ParkCapacity scooterOffroadCapacity;
    private ParkCapacity scooterUrbanCapacity;
    
    public Parks (int park_id,String name,Coordinates cord){
        this.park_id = park_id;
        this.name = name;
        this.cord = cord;
    }

    public int getPark_id() {
        return park_id;
    }

    public void setPark_id(int park_id) {
        this.park_id = park_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCord() {
        return cord;
    }

    public void setCord(Coordinates cord) {
        this.cord = cord;
    }
    
}
