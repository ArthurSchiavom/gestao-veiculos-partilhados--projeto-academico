/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.register;

import lapr.project.register.ParkRegister;
import java.util.List;
import lapr.project.model.Company;
import lapr.project.model.Coordinates;
import lapr.project.model.Park.Park;
import static org.junit.jupiter.api.Assertions.*;
import lapr.project.bootstrap.Bootstrap;
import lapr.project.shutdown.Shutdown;
import org.junit.jupiter.api.Test;

/**
 *
 * Test for ParkRegistor classe
 */
public class ParkRegisterTest {
    
    /**
     * Test of addPark method, of class ParkRegistor.
     */
    @Test
    public void testAddPark() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        boolean expResult=true;
        Coordinates cord=new Coordinates(37.819722,-122.478611, 0);
        boolean result=parkRegistor.addPark("Parque das Camelias", cord);
        Shutdown.shutdown();
        assertEquals(expResult,result);
    }

    /**
     * Test of removeParkById method, of class ParkRegistor.
     */
    @Test
    public void testRemoveParkById() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        boolean result=parkRegistor.removeParkById(1);
        Shutdown.shutdown();
        assertEquals(true, result);
    }

    /**
     * Test of fetchParkByName method, of class ParkRegistor.
     */
    @Test
    public void testFetchParkByName() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        List<Park> listaParques=parkRegistor.fetchParkByName("Parque do Arthur");
        int sizeResult=listaParques.size();
        int expectedSize=2;
        Shutdown.shutdown();
        assertEquals(expectedSize,sizeResult);
    }

    /**
     * Test of fetchParkById method, of class ParkRegistor.
     */
    @Test
    public void testFetchParkById() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        Park parkResult=parkRegistor.fetchParkById(0);
        Coordinates coordinatesResult=parkResult.getCoords();
        String nameResult=parkResult.getName();
        int parkIdResult=parkResult.getParkId();
        Coordinates coordinatesExpected=new Coordinates(18.222, 20.12, 10);
        String nameExpected="Parque do Diogo";
        int parkIdExpected=0;
        Shutdown.shutdown();
        assertEquals(coordinatesExpected, coordinatesResult);
        assertEquals(nameExpected, nameResult);
        assertEquals(parkIdExpected, parkIdResult);
    }
    
}
