/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

/**
 *
 * @author kevin
 */
public class ParkCapacity {
    private int capacity;
    private int amount_occupied;
    
    /**
     * park capacity created
     * @param capacity
     * @param amount_occupied 
     */
    public ParkCapacity(int capacity,int amount_occupied){
        this.capacity = capacity;
        this.amount_occupied = amount_occupied;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAmount_occupied() {
        return amount_occupied;
    }

    public void setAmount_occupied(int amount_occupied) {
        this.amount_occupied = amount_occupied;
    }
    
    
}
