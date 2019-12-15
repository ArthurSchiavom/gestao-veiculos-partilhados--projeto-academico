/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.util.List;
import jdk.nashorn.internal.ir.annotations.Ignore;

import lapr.project.bootstrap.Bootstrap;
import lapr.project.model.Coordinates;
import lapr.project.model.park.Park;
import static org.junit.jupiter.api.Assertions.*;
import lapr.project.shutdown.Shutdown;

/**
 *
 * Test for ParkRegistor classe
 */
public class ParkRegisterTest {

    /**
     * Test of addPark method, of class ParkRegistor.
     */
    @Ignore
    public void testAddPark() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        boolean expResult=true;
        Coordinates cord=new Coordinates(37.819722,-122.478611, 0);
        //boolean result=parkRegistor.addPark("Parque das Camelias", cord,"Grande e pequeno",(float)1.75,(float)3.2);
        Shutdown.shutdown();
        //assertEquals(expResult,result);
    }

    /**
     * Test of removeParkById method, of class ParkRegistor.
     */
    @Ignore
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
    @Ignore
    public void testFetchParkByName() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        List<Park> listaParques=parkRegistor.fetchParkByDescription("Parque do Arthur");
        int sizeResult=listaParques.size();
        int expectedSize=2;
        Shutdown.shutdown();
        assertEquals(expectedSize,sizeResult);
    }

    /**
     * Test of fetchParkById method, of class ParkRegistor.
     */
    @Ignore
    public void testFetchParkById() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        Park parkResult=parkRegistor.fetchParkById(0);
        Coordinates coordinatesResult=parkResult.getCoords();
        //String nameResult=parkResult.getName();
        int parkIdResult=parkResult.getParkId();
        Coordinates coordinatesExpected=new Coordinates(18.222, 20.12, 10);
        String nameExpected="Parque do Diogo";
        int parkIdExpected=0;
        Shutdown.shutdown();
        assertEquals(coordinatesExpected, coordinatesResult);
        //assertEquals(nameExpected, nameResult);
        assertEquals(parkIdExpected, parkIdResult);
    }

    /**
     * Test of  UpdatePark method, of class ParkRegistor
     */
    @Ignore
    public void updatePark(){
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.boot();
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        //int nmrUpdLines=parkRegistor.UpdatePark(name, cord, vehicleCapacities, 0, description, 0, 0);
        int expectedResult=1;
        //assertEquals(expectedResult, nmrUpdLines);
    }

}
