/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.SQLException;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Ignore;

import lapr.project.data.Bootstrap;
import lapr.project.model.Coordinates;
import lapr.project.model.park.Park;
import static org.junit.jupiter.api.Assertions.*;

import lapr.project.data.Shutdown;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 *
 * Test for ParkRegistor classe
 */
public class ParkRegisterTest {

    @BeforeAll
    static void prepare() {
        try {
            Bootstrap.boot();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    @AfterAll
    static void end() {
        Shutdown.shutdown();
    }

    /**
     * Test of removeParkById method, of class ParkRegistor.
     */
    @Ignore
    public void testRemoveParkById() {
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        boolean result=parkRegistor.removeParkById(1);
        assertEquals(true, result);
    }

    /**
     * Test of fetchParkById method, of class ParkRegistor.
     */
    @Ignore
    public void testFetchParkById() {
        Company company=Company.getInstance();
        ParkRegister parkRegistor=new ParkRegister(company.getDataHandler());
        Park parkResult=parkRegistor.fetchParkById(0);
        Coordinates coordinatesResult=parkResult.getCoords();
        int parkIdResult=parkResult.getParkId();
        Coordinates coordinatesExpected=new Coordinates(18.222, 20.12, 10);
        int parkIdExpected=0;
        assertEquals(coordinatesExpected, coordinatesResult);
        assertEquals(parkIdExpected, parkIdResult);
    }

}
