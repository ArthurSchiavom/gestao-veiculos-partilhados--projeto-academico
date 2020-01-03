package lapr.project.model;

import lapr.project.model.vehicles.Vehicle;

import java.util.Comparator;

public class VehicleDescriptionComparator implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle o1, Vehicle o2) {
        return o1.getDescription().compareTo(o2.getDescription());
    }
}
