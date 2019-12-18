/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils.physics.calculations;


/**
 * Class that has the methods to calculate the calories burnt between two points
 *
 */
public class CaloriesCalculation {

    private static final double AIR_DENSITY = 1.225;
    private static final double GRAVITATIONAL_CONSTANT = 9.8;
    private static final double CONVERT_JOULE_CAL = 0.239006;   // 1 Joule = 0.239006 cal;

    /**
     * Returns the time spent to go from one point to another
     * @param velocity - average speed 
     * @param distanceMade - distance between the starting and ending points
     * @return time spent to go from one point to another
     */
    public static Double calculateTimeSpent(double velocity, double distanceMade) {
        return distanceMade / velocity;
    }

    /**
     * Returns the power rolling resistance
     * @param velocity - average speed
     * @param mass - mass of the person + bicycle
     * @param kineticCoefficient - kinetic coefficient
     * @return the power rolling resistance
     */
    public static Double calculatePowerRollingResistance(double velocity, double mass, double kineticCoefficient) {
        return velocity * mass * GRAVITATIONAL_CONSTANT * kineticCoefficient;
    }

    /**
     * Returns the power that is need to climb
     * @param velocity - average speed
     * @param mass - mass of the person + bicycle
     * @param slope - slope between the starting and ending points
     * @return the power that is need to climb
     */
    public static Double calculateClimbingPower(double velocity, double mass, double slope) {
        return velocity * mass * GRAVITATIONAL_CONSTANT * slope;
    }

    /**
     * Returns the power that needs to surpasse the air drag
     * @param velocity - average speed 
     * @param windSpeed - wind speed
     * @param aerodynamicCoefficient - aerodynamic coefficient
     * @param frontalArea - frontal area of the vehicle
     * @return  the power that needs to surpasse the air drag
     */
    public static Double calculatePowerAirDrag(double velocity, double windSpeed, double aerodynamicCoefficient, double frontalArea) {
        return 0.5 * AIR_DENSITY * Math.pow(windSpeed, 2) * velocity  * aerodynamicCoefficient * frontalArea;
    }
    
    /**
     * The slop between the starting and ending points
     * @param height - height difference between the points
     * @param DistanceTraveled - distance between the points
     * @return the slop between the starting and ending point
     */
    public static Double calculateSlop(int height, double distanceTraveled) {
        double cateto = Math.sqrt(Math.pow(distanceTraveled, 2) - Math.pow(height, 2));
        double slop = height/cateto;
        return Math.round(slop * 100.0)/100.0;
    }

    /**
     * Method that calculates the ammount of calories burnt between two points
     * <url> https://en.wikipedia.org/wiki/Bicycle_performance </url>
     *
     *
     * @param velocity - average speed of a person (m/s)
     * @param windSpeed - wind speed (m/s)
     * @param kineticCoefficient - kinetic coefficient between two points
     * @param aerodynamicCoefficient - aerodynamic coefficient
     * @param frontalArea - frontal area of the bicycle (m^2)
     * @param distanceMade - distance between the two points (m)
     * @param personMass - mass of the person (m)
     * @param bicycleMass - mass of the bicycle (m)
     * @param startAltitude - altitude of the starting point (m)
     * @param endAltitude - altitude of the ending point (m)
     * @return
     */
    public static Double calculateCaloriesBurnt(double velocity, double windSpeed, double kineticCoefficient, double aerodynamicCoefficient, double frontalArea, double distanceMade, double personMass, double bicycleMass, int startAltitude, int endAltitude) {

        double time = calculateTimeSpent(velocity, distanceMade);
        double totalMass = personMass + bicycleMass;
        int heightDifference = endAltitude - startAltitude;
        double slope = calculateSlop(heightDifference, distanceMade);

        double powerRollingResistance = calculatePowerRollingResistance(velocity, totalMass, kineticCoefficient);
        double powerClimbing = calculateClimbingPower(velocity, totalMass, slope);
        double powerAirDrag = calculatePowerAirDrag(velocity, windSpeed, aerodynamicCoefficient, frontalArea);

        double totalPower = powerRollingResistance + powerClimbing + powerAirDrag;

        double energySpent = totalPower * time;
        double energySpentCal = energySpent * CONVERT_JOULE_CAL;

        return Math.round(energySpentCal*100.0)/100.0;
    }
}
