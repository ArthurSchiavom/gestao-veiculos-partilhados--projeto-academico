package lapr.project.model.vehicles;

import java.util.Comparator;

public class ScooterDescriptionComparator implements Comparator<ElectricScooter> {
    @Override
    public int compare(ElectricScooter o1, ElectricScooter o2) {
        return o1.getDescription().compareTo(o2.getDescription());
    }
}
