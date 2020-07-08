package lapr.project.data;

import lapr.project.model.point.of.interest.PointOfInterest;

import java.util.Comparator;

/**
 * Orders in descending order by height
 */
public class SortByHeightDescending implements Comparator<PointOfInterest> {
    @Override
    public int compare(PointOfInterest pointOfInterest, PointOfInterest t1) {
        return t1.getCoordinates().getAltitude()-pointOfInterest.getCoordinates().getAltitude();
    }
}
