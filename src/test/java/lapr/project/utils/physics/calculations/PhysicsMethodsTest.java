/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils.physics.calculations;

import java.util.ArrayList;
import java.util.List;
import lapr.project.model.Coordinates;
import lapr.project.model.Path;
import lapr.project.model.users.Client;
import lapr.project.model.users.CreditCard;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.model.vehicles.Vehicle;
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
        double cosApparentWindAngle = 0.948;
        Double expResult = 0.574844;

        Double result = PhysicsMethods.calculatePowerAirDrag(velocity, windSpeed, aerodynamicCoefficient, frontalArea, cosApparentWindAngle);
        assertEquals(expResult, result, 0.001);
        Double notExpResult = 0.40;
        assertNotEquals(notExpResult, result, 0.0);
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
     * Test of calculateEnergySpent method, of class PhysicsMethods.
     */
    @Test
    public void testCalculateEnergySpent() {
        double velocity = 3.0;
        double windSpeed = 1.0;
        double kineticCoefficient = 0.002;
        double aerodynamicCoefficient = 1.10;
        double frontalArea = 0.3;
        double distanceMade = 300.0;
        int personMass = 75;
        int vehicleMass = 20;
        Double expResult = 249193.13;
        Double result = PhysicsMethods.calculateEnergySpent(velocity, windSpeed, kineticCoefficient, aerodynamicCoefficient, frontalArea, distanceMade, personMass, vehicleMass, new Coordinates(0.0, 0.0, 100), new Coordinates(1.0, 1.0, 300), 90);
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

    /**
     * Test of calculateAngleWindSpeedRiderSpeed method, of class
     * PhysicsMethods.
     */
    @Test
    public void testCalculateAngleWindSpeedRiderSpeed() {
        double pathInitX = 0.0;
        double pathInitY = 0.0;
        double pathFinX = 1.0;
        double pathFinY = 1.0;
        double windAngleRad = 1.57;
        double windSpeed = 1.0;
        Double expResult = 1.14372;
        Double result = PhysicsMethods.calculateAngleWindSpeedRiderSpeed(pathInitX, pathInitY, pathFinX, pathFinY, windAngleRad, windSpeed);
        assertEquals(expResult, result, 0.001);

        double pathInitX2 = 1.0;
        double pathInitY2 = 1.0;
        double pathFinX2 = 0.0;
        double pathFinY2 = 0.0;
        double windAngleRad2 = 1.57;
        Double expResult2 = 1.14372;
        Double result2 = PhysicsMethods.calculateAngleWindSpeedRiderSpeed(pathInitX, pathInitY, pathFinX, pathFinY, windAngleRad2, windSpeed);
        assertEquals(expResult2, result2, 0.01);

        Double notExpResult = 50000.0;
        assertNotEquals(notExpResult, result);

    }

    /**
     * Test of calculateApparentWindSpeed method, of class PhysicsMethods.
     */
    @Test
    public void testCalculateApparentWindSpeed() {
        double windAngleRad = 1.57;
        double riderSpeed = 3.0;
        double windSpeed = 1.0;
        Double expResult = 3.16303;
        Double result = PhysicsMethods.calculateApparentWindSpeed(windAngleRad, riderSpeed, windSpeed);
        assertEquals(expResult, result, 0.001);

        Double notExpResult = 50000.0;
        assertNotEquals(notExpResult, result);

    }

    /**
     * Test of calculateCosApparentWindAngle method, of class PhysicsMethods.
     */
    @Test
    public void testCalculateCosApparentWindAngle() {
        double riderSpeed = 3.0;
        double windSpeed = 1.0;
        double apparentWindSpeed = 3.16303;
        double windAngleRad = 1.57;
        Double expResult = 0.948;
        Double result = PhysicsMethods.calculateCosApparentWindAngle(riderSpeed, windSpeed, apparentWindSpeed, windAngleRad);
        assertEquals(expResult, result, 0.001);

        Double notExpResult = 50000.0;
        assertNotEquals(notExpResult, result);

    }

    /**
     * Test of convertDegreesToRadian method, of class PhysicsMethods.
     */
    @Test
    public void testConvertDegreesToRadian() {

        int angle = 90;
        Double expResult = 1.57;
        Double result = PhysicsMethods.convertDegreesToRadian(angle);
        assertEquals(expResult, result, 0.001);

        int angle2 = 180;
        Double expResult2 = Math.PI;
        Double result2 = PhysicsMethods.convertDegreesToRadian(angle2);
        assertEquals(expResult2, result2, 0.001);
    }

    /**
     * Test of convertJouleToCal method, of class PhysicsMethods.
     */
    @Test
    public void testConvertJouleToCal() {
        double energyJoule = 249193.12;
        Double expResult = 59558.65;
        Double result = PhysicsMethods.convertJouleToCal(energyJoule);
        assertEquals(expResult, result);
    }

    /**
     * Test of predictEnergySpent method, of class PhysicsMethods.
     */
    @Test
    public void testPredictEnergySpent() {
        Client client = new Client("a@a.a", "peleila", "password", 170, 75, 'M', 3.0f, false, new CreditCard("1111111111111111"));
        Path path1 = new Path(new Coordinates(0.0, 0.0, 100), new Coordinates(0.0018, 0.002, 300), 0.002, 90, 1.0);
        List<Path> trip = new ArrayList<>();
        trip.add(path1);
        Vehicle vehicle = new Bicycle(1, "PT001", 1.10f, 0.3f, 20, true, 15);
        double expResult = 251309.92;
        double result = PhysicsMethods.predictEnergySpent(client, trip, vehicle);
        assertEquals(expResult, result, 0.0);

        Vehicle vehicle2 = new ElectricScooter(2, "PT002", 1.10f, 0.3f, 20, true, ElectricScooterType.URBAN, 75, 1, 1000);
        expResult = 251312.95;
        result = PhysicsMethods.predictEnergySpent(client, trip, vehicle2);
        assertEquals(expResult, result, 0.0);

        Vehicle vehicle3 = new ElectricScooter(2, "PT002", 1.10f, 0.3f, 20, true, ElectricScooterType.OFFROAD, 75, 1, 1000);
        expResult = 251312.95;
        result = PhysicsMethods.predictEnergySpent(client, trip, vehicle3);
        assertEquals(expResult, result, 0.0);
    }

}
