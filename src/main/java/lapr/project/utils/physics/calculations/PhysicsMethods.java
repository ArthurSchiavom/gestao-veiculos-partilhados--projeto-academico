/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils.physics.calculations;

import java.util.List;
import lapr.project.model.Coordinates;
import lapr.project.model.Path;
import lapr.project.model.users.Client;
import lapr.project.model.vehicles.Bicycle;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;
import static lapr.project.model.vehicles.VehicleType.BICYCLE;

/**
 * Class that has the methods to calculate the calories burnt between two points
 * and the autonomy of the scooter
 *
 */
public class PhysicsMethods {

    private static final double AIR_DENSITY = 1.225;
    private static final double GRAVITATIONAL_CONSTANT = 9.8;
    private static final double CONVERT_JOULE_CAL = 0.239006;   // 1 Joule = 0.239006 cal;
    private static final double SCOOTER_MAX_SPEED = 20;
    private static final double SCOOTER_EFICIENCY = 0.70;

    /**
     * Returns the time spent to go from one point to another
     *
     * @param velocity - average speed
     * @param distanceMade - distance between the starting and ending points
     * @return time spent to go from one point to another
     */
    public static Double calculateTimeSpent(double velocity, double distanceMade) {
        return distanceMade / velocity;
    }

    /**
     * Returns the power rolling resistance
     *
     * @param velocity - average speed
     * @param mass - mass of the person + vehicle
     * @param kineticCoefficient - kinetic coefficient
     * @return the power rolling resistance
     */
    public static Double calculatePowerRollingResistance(double velocity, double mass, double kineticCoefficient) {
        return velocity * mass * GRAVITATIONAL_CONSTANT * kineticCoefficient;
    }

    /**
     * Returns the power that is need to climb
     *
     * @param velocity - average speed
     * @param mass - mass of the person + vehicle
     * @param slope - slope between the starting and ending points
     * @return the power that is need to climb
     */
    public static Double calculateClimbingPower(double velocity, double mass, double slope) {
        return velocity * mass * GRAVITATIONAL_CONSTANT * slope;
    }

    /**
     * Returns the value of the angle made between the vector of the wind speed
     * and the rider speed
     *
     * @param pathInitX - initial x value of the path
     * @param pathInitY - initial y value of the path
     * @param pathFinX - final x value of the path
     * @param pathFinY - final y value of the path
     * @param windAngleRad - the angle made between the wind speed direction and
     * the North pole
     * @param windSpeed - the value of the wind speed
     * @return the value of the angle made between the vector of the wind speed
     * and the rider speed
     */
    public static Double calculateAngleWindSpeedRiderSpeed(double pathInitX, double pathInitY, double pathFinX, double pathFinY, double windAngleRad, double windSpeed) {
        double vWindX = windSpeed * Math.sin(windAngleRad);
        double vWindY = windSpeed * Math.cos(windAngleRad);

        double vBicycleX = pathFinX - pathInitX;
        double vBicycleY = pathFinY - pathInitY;

        double numerator = (vWindX * vBicycleX) + (vWindY * vBicycleY);
        double denominator = Math.sqrt(Math.pow(vWindX, 2) + Math.pow(vWindY, 2)) + Math.sqrt(Math.pow(vBicycleX, 2) + Math.pow(vBicycleY, 2));
        double cosAngle = numerator / denominator;

        return Math.acos(cosAngle);
    }
    

    /**
     * Returns the value of the apparent wind speed
     *
     * @param windAndVehicleAngleRad - - the angle made between the wind speed direction and the vehicle
     * @param riderSpeed - the rider average speed
     * @param windSpeed - the wind speed
     * @return the value of the apparent wind speed
     */
    public static Double calculateApparentWindSpeed(double windAndVehicleAngleRad, double riderSpeed, double windSpeed) {
        return Math.sqrt(Math.pow(riderSpeed + (windSpeed * Math.cos(windAndVehicleAngleRad)), 2) + Math.pow((windSpeed * Math.sin(windAndVehicleAngleRad)), 2));
    }

    /**
     * Returns the cosine of the angle made between the apparent wind speed and
     * the speed of the rider
     *
     * @param riderSpeed - the rider average speed
     * @param windSpeed - the wind speed
     * @param apparentWindSpeed - the apparent wind speed
     * @param windAndRiderAngleRad - the angle made between the wind speed direction and the vehicle
     * 
     * @return the cosine of the angle made between the apparent wind speed and
     * the speed of the rider
     */
    public static Double calculateCosApparentWindAngle(double riderSpeed, double windSpeed, double apparentWindSpeed, double windAndRiderAngleRad) {
        return (riderSpeed + (windSpeed * Math.cos(windAndRiderAngleRad))) / apparentWindSpeed;
    }

    /**
     * Returns the power that needs to surpasse the air drag
     * <url> https://www.sheldonbrown.com/brandt/wind.html </url>
     *
     * @param velocity - average speed
     * @param windSpeed - wind speed
     * @param aerodynamicCoefficient - aerodynamic coefficient
     * @param frontalArea - frontal area of the vehicle
     * @param cosApparentWindAngle - the angle that the wind makes with the North pole
     * @return the power that needs to surpasse the air drag
     */
    public static Double calculatePowerAirDrag(double velocity, double windSpeed, double aerodynamicCoefficient, double frontalArea, double cosApparentWindAngle) {
        return 0.5 * AIR_DENSITY * Math.pow(windSpeed, 2) * velocity * cosApparentWindAngle * aerodynamicCoefficient * frontalArea;
    }

    /**
     * The slop between the starting and ending points
     *
     * @param height - height difference between the points
     * @param distanceTraveled - distance between the points
     * @return the slop between the starting and ending point
     */
    public static Double calculateSlope(int height, double distanceTraveled) {
        double cateto = Math.sqrt(Math.pow(distanceTraveled, 2) - Math.pow(height, 2));
        double slope = height / cateto;
        return Math.round(slope * 100.0) / 100.0;
    }

    /**
     * Method that calculates the amount of calories burnt between two points
     * <url> https://en.wikipedia.org/wiki/Bicycle_performance </url>
     *
     *
     * @param velocity - average speed of a person (m/s)
     * @param windSpeed - wind speed (m/s)
     * @param kineticCoefficient - kinetic coefficient between two points
     * @param aerodynamicCoefficient - aerodynamic coefficient
     * @param frontalArea - frontal area of the vehicle (m^2)
     * @param distanceMade - distance between the two points (m)
     * @param personMass - mass of the person (kg)
     * @param vehicleMass - mass of the vehicle (kg)
     * @param startPoint - the starting point of the path that the rider is going
     * @param endPoint - the ending point of the path that the rider is going
     * @param windAngle - the angle made between the wind speed and the north pole
     * @return the amount of calories burnt between two points
     */
    public static Double calculateEnergySpent(double velocity, double windSpeed, double kineticCoefficient, double aerodynamicCoefficient, double frontalArea, double distanceMade, int personMass, int vehicleMass, Coordinates startPoint, Coordinates endPoint, int windAngle) {

        double time = calculateTimeSpent(velocity, distanceMade);
        double totalMass = personMass + vehicleMass;
        int heightDifference = endPoint.getAltitude() - startPoint.getAltitude();
        double slope = calculateSlope(heightDifference, distanceMade);
        double windAngleRad = convertDegreesToRadian(windAngle);
        double angleWindSpeedRiderSpeed = calculateAngleWindSpeedRiderSpeed(startPoint.getCoordinateX(), startPoint.getCoordinateY(), endPoint.getCoordinateX(), endPoint.getCoordinateY(), windAngleRad, windSpeed);
        double apparentWindSpeed = calculateApparentWindSpeed(angleWindSpeedRiderSpeed, velocity, windSpeed);
        double cosineApparentWind = calculateCosApparentWindAngle(velocity, windSpeed, apparentWindSpeed, angleWindSpeedRiderSpeed);

        double powerRollingResistance = calculatePowerRollingResistance(velocity, totalMass, kineticCoefficient);
        double powerClimbing = calculateClimbingPower(velocity, totalMass, slope);
        double powerAirDrag = calculatePowerAirDrag(velocity, windSpeed, aerodynamicCoefficient, frontalArea, cosineApparentWind);

        double totalPower = powerRollingResistance + powerClimbing + powerAirDrag;

        double energySpent = totalPower * time;

        return Math.round(energySpent * 100.0) / 100.0;
    }

    /**
     * Returns the actual battery of the scooter in W.h
     *
     * @param maxBatteryCapacity - The max battery of the scooter
     * @param percentageBattery - The percentage of the actual battery
     * @return the actual battery of the scooter in W.h
     */
    public static Double calculateActualBatteryWattHrs(Double maxBatteryCapacity, int percentageBattery) {
        return ((maxBatteryCapacity * percentageBattery) / 100) * 1000;
    }

    /**
     * Returns the time that the battery of the scooter will take to discharge
     * in hours
     *
     * @param maxBatteryCapacity - The max battery of the scooter
     * @param percentageBattery - The percentage of the actual battery
     * @param motorPower - The power of the motor
     * @return the time that the battery of the scooter will take to discharge
     * in hours
     */
    public static Double calculateTimeByBatteryChargeAndMotorPowerHrs(Double maxBatteryCapacity, int percentageBattery, int motorPower) {
        Double batteryCapacity = calculateActualBatteryWattHrs(maxBatteryCapacity, percentageBattery);
        return batteryCapacity / motorPower;
    }

    /**
     * Returns the real scooter autonomy in Km
     *
     * @param maxBatteryCapacity - The max battery of the scooter
     * @param percentageBattery - The percentage of the actual battery
     * @param motorPower - The power of the motor
     * @return the real scooter autonomy in Km
     */
    public static Double calculateScooterAutonomyKM(Double maxBatteryCapacity, int percentageBattery, int motorPower) {
        Double timeDuration = calculateTimeByBatteryChargeAndMotorPowerHrs(maxBatteryCapacity, percentageBattery, motorPower);
        Double fullAutonomy = timeDuration * SCOOTER_MAX_SPEED;
        Double realAutonomy = fullAutonomy * SCOOTER_EFICIENCY;
        return Math.round(realAutonomy * 100.0) / 100.0;
    }
    
    /**
     * Converts an angle in degrees to radians
     * 
     * @param angle - the angle in degrees
     * @return an angle in radians
     */
    public static Double convertDegreesToRadian(int angle) {
        return (angle*Math.PI)/180;
    }

    /**
     * Converts the value of the energy in joule to calories
     * 
     * @param energyJoule - the energy in joules
     * @return the value of the energy in joule to calories
     */
    public static Double convertJouleToCal(double energyJoule) {
        return Math.round(energyJoule * CONVERT_JOULE_CAL * 100.0) /100.0;
    }
    
    
    /**
     * Predicts the energy spent in a trip between two parks
     * 
     * @param client - the client 
     * @param trip - the trip
     * @param vehicle - the vehicle in use
     * @return  - the energy spent in a trip between two parks
     */
    public static double predictEnergySpent(Client client, List<Path> trip, Vehicle vehicle) {
           double energySpent = 0.0;
           double averageSpeed = 0.0;
           double distanceMade = 0.0;
           if (vehicle.getType() == VehicleType.BICYCLE) {
               averageSpeed = (double) client.getCyclingAverageSpeed();
           } else if (vehicle.getType() == VehicleType.ELECTRIC_SCOOTER) {
               averageSpeed = SCOOTER_MAX_SPEED;
           }
           for (Path path : trip) {
               distanceMade = path.getStartingPoint().getCoordinates().distance(path.getEndingPoint().getCoordinates()) * 1000;
               energySpent += PhysicsMethods.calculateEnergySpent(averageSpeed, path.getWindSpeed(), path.getKineticCoefficient(), vehicle.getAerodynamicCoefficient(), vehicle.getFrontalArea(), distanceMade, client.getWeight(), vehicle.getWeight(), path.getStartingPoint().getCoordinates(), path.getEndingPoint().getCoordinates(), path.getWindDirectionDegrees());
           } 
           return energySpent;
       }
}
