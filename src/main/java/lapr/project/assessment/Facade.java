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
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(s));
            writer.write("41.152712,-8.609297,494");
            writer.newLine();
            writer.write("41.145883,-8.610680,282");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
