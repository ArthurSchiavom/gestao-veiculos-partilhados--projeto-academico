/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils.physics.calculations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 */
public class PhysicsMethodsTest {

    public PhysicsMethodsTest() {
    }

    /**
     * Test of calculateTimeSpent method, of class PhysicsMethods.
     */
    @Test
    public void calculateTimeSpentTest() {
        double velocity = 3.0;
        double distanceMade = 300.0;
        Double expResult = 100.0;
        Double result = PhysicsMethods.calculateTimeSpent(velocity, distanceMade);
        assertEquals(expResult, result);
        Double notExpResult = 20.0;
        assertNotEquals(notExpResult, result);
    }

    /**
     * Test of calculatePowerRollingResistance method, of class PhysicsMethods.
     */
    @Test
    public void calculatePowerRollingResistanceTest() {
        double velocity = 3.0;
        double mass = 95.0;
        double kineticCoefficient = 0.002;
        Double expResult = 5.586;
        Double result = PhysicsMethods.calculatePowerRollingResistance(velocity, mass, kineticCoefficient);
        assertEquals(expResult, result);
        Double notExpResult = 5.10;
        assertNotEquals(notExpResult, result);
    }

    /**
     * Test of calculateClimbingPower method, of class PhysicsMethods.
     */
    @Test
    public void calculateClimbingPowerTest() {
        double velocity = 3.0;
        double mass = 95.0;
        double slope = 0.89;
        Double expResult = 2485.77;
        Double result = PhysicsMethods.calculateClimbingPower(velocity, mass, slope);
        assertEquals(expResult, result);
        Double notExpResult = 2000.10;
        assertNotEquals(notExpResult, result);
    }

    /**
     * Test of calculatePowerAirDrag method, of class PhysicsMethods.
     */
    @Test
    public void calculatePowerAirDragTest() {
        double velocity = 3.0;
        double windSpeed = 1.0;
        double aerodynamicCoefficient = 1.10;
        double frontalArea = 0.3;
        Double expResult = 0.606375;
        Double result = PhysicsMethods.calculatePowerAirDrag(velocity, windSpeed, aerodynamicCoefficient, frontalArea);
        assertEquals(expResult, result);
        Double notExpResult = 0.40;
        assertNotEquals(notExpResult, result);
    }

    /**
     * Test of calculateSlope method, of class PhysicsMethods.
     */
    @Test
    public void calculateSlopTest() {
        int height = 200;
        double distanceTraveled = 300.0;
        Double expResult = 0.89;
        Double result = PhysicsMethods.calculateSlope(height, distanceTraveled);
        assertEquals(expResult, result);
        Double notExpResult = 0.10;
        assertNotEquals(notExpResult, result);

    }

    /**
     * Test of calculateCaloriesBurnt method, of class PhysicsMethods.
     */
    @Test
    public void calculateCaloriesBurntTest() {
        double velocity = 3.0;
        double windSpeed = 1.0;
        double kineticCoefficient = 0.002;
        double aerodynamicCoefficient = 1.10;
        double frontalArea = 0.3;
        double distanceMade = 300.0;
        double personMass = 75.0;
        double bicycleMass = 20.0;
        int startAltitude = 100;
        int endAltitude = 300;
        Double expResult = 59559.40;
        Double result = PhysicsMethods.calculateCaloriesBurnt(velocity, windSpeed, kineticCoefficient, aerodynamicCoefficient, frontalArea, distanceMade, personMass, bicycleMass, startAltitude, endAltitude);
        assertEquals(expResult, result);
        Double notExpResult = 50000.0;
        assertNotEquals(notExpResult, result);
    }

    /**
     * Test of calculateActualBatteryWattHrs method, of class PhysicsMethods.
     */
    @Test
    public void testCalculateActualBatteryWattHrs() {
        Double maxBatteryCapacity = 1.0;
        int percentageBattery = 35;
        Double expResult = 350.0;
        Double result = PhysicsMethods.calculateActualBatteryWattHrs(maxBatteryCapacity, percentageBattery);
        assertEquals(expResult, result);
        Double notExpResult = 300.0;
        assertNotEquals(notExpResult, result);
    }

    /**
     * Test of calculateTimeByBatteryChargeAndMotorPowerHrs method, of class
     * PhysicsMethods.
     */
    @Test
    public void testCalculateTimeByBatteryChargeAndMotorPowerHrs() {
        Double maxBatteryCapacity = 1.0;
        int percentageBattery = 35;
        int motorPower = 250;
        Double expResult = 1.4;
        Double result = PhysicsMethods.calculateTimeByBatteryChargeAndMotorPowerHrs(maxBatteryCapacity, percentageBattery, motorPower);
        assertEquals(expResult, result);
        Double notExpResult = 1.5;
        assertNotEquals(notExpResult, result);
    }

    /**
     * Test of calculateScooterAutonomyKM method, of class PhysicsMethods.
     */
    @Test
    public void testCalculateScooterAutonomyKM() {
        Double maxBatteryCapacity = 1.0;
        int percentageBattery = 35;
        int motorPower = 250;
        Double expResult = 19.6;
        Double result = PhysicsMethods.calculateScooterAutonomyKM(maxBatteryCapacity, percentageBattery, motorPower);
        assertEquals(expResult, result);
        Double notExpResult = 19.56;
        assertNotEquals(notExpResult, result);
    }

}
