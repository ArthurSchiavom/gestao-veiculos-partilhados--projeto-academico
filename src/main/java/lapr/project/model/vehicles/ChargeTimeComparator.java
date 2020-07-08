package lapr.project.model.vehicles;

import lapr.project.model.point.of.interest.park.Park;
import lapr.project.utils.physics.calculations.PhysicsMethods;

import java.util.Comparator;

public class ChargeTimeComparator implements Comparator<ElectricScooter> {
    private Park park;

    public ChargeTimeComparator(Park park) {
        this.park = park;
    }

    @Override
    public int compare(ElectricScooter o1, ElectricScooter o2) {
        long val = PhysicsMethods.timeToChargeInSeconds(o2, park) - PhysicsMethods.timeToChargeInSeconds(o1, park);
        if(val>0) {
            return 1;
        } else if(val==0) {
            return 0;
        } else {
            return -1;
        }
    }
}
