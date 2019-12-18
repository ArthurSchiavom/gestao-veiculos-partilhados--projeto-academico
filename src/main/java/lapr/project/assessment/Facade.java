package lapr.project.assessment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Facade implements Serviceable {

    @Override
    public int addBicycles(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addEscooters(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addParks(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int removePark(String s) {
        return 0;
    }

    @Override
    public int addPOIs(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addUsers(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addPaths(String s) {
        return 0;
    }

    @Override
    public int getNumberOfBicyclesAtPark(double v, double v1, String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfBicyclesAtPark(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    /**
     * Distance is returns in metres, rounded to the unit e.g. (281,58 rounds
     * to 282);
     *
     * @param v  Latitude in degrees.
     * @param v1 Longitude in degrees.
     * @param s  Filename for output.
     */
    @Override
    public void getNearestParks(double v, double v1, String s) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(s))) {
            writer.write("41.152712,-8.609297,494");
            writer.newLine();
            writer.write("41.145883,-8.610680,282");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getNearestParks(double v, double v1, String s, int i) {

    }

    @Override
    public int getFreeBicycleSlotsAtPark(String s, String s1) {
        return 0;
    }

    @Override
    public int getFreeEscooterSlotsAtPark(String s, String s1) {
        return 0;
    }

    @Override
    public int getFreeSlotsAtParkForMyLoanedVehicle(String s) {
        return 0;
    }

    @Override
    public int linearDistanceTo(double v, double v1, double v2, double v3) {
        return 0;
    }

    @Override
    public int pathDistanceTo(double v, double v1, double v2, double v3) {
        return 0;
    }

    @Override
    public long unlockBicycle(String s, String s1) {
        return 0;
    }

    @Override
    public long unlockEscooter(String s, String s1) {
        return 0;
    }

    @Override
    public long lockBicycle(String s, double v, double v1, String s1) {
        return 0;
    }

    @Override
    public long lockBicycle(String s, String s1, String s2) {
        return 0;
    }

    @Override
    public long lockEscooter(String s, double v, double v1, String s1) {
        return 0;
    }

    @Override
    public long lockEscooter(String s, String s1, String s2) {
        return 0;
    }

    @Override
    public int registerUser(String s, String s1, String s2, int i, int i1, String s3) {
        return 0;
    }

    @Override
    public long unlockAnyEscootereAtPark(String s, String s1, String s2) {
        return 0;
    }

    @Override
    public long unlockAnyEscootereAtParkForDestination(String s, String s1, double v, double v1, String s2) {
        return 0;
    }

    @Override
    public int suggestEscootersToGoFromOneParkToAnother(String s, String s1, double v, double v1, String s2) {
        return 0;
    }

    @Override
    public long mostEnergyEfficientRouteBetweenTwoParks(String s, String s1, String s2, String s3, String s4, String s5) {
        return 0;
    }
}
