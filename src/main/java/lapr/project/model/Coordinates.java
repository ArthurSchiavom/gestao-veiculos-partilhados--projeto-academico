package lapr.project.model;

import java.util.Objects;

/**
 * A coordinates class holds information about coordinates lat and lon in
 * decimal degrees
 */
public class Coordinates {

    private double lat;
    private double lon;
    private int altitude;

    /**
     * Creates a set of coordinates
     *
     * @param lat      the lat parameter
     * @param lon      the lon parameter
     * @param altitude the altitude of the coordinates, in meters, 0 is sea
     *                 level
     */
    public Coordinates(double lat, double lon, int altitude) {
        if (lon > 180) {
            lon = (lon % 180) - 180;
        }
        if (lon <= -180) {
            lon = 180 + (lon % 180);
        }
        if (lat <= -180) {
            lat = 180 + (lat % 180);
        }
        if (lat >= 180) {
            lat = -(lat % 180);
        }
        if (lat < -90) {
            lat = -(90 + (lat % 90));
        }
        if (lat > 90) {
            lat = 90 - (lat % 90);
        }
        this.lat = lat;
        this.lon = lon;
        this.altitude = altitude;
    }

    /**
     * Returns the lat
     *
     * @return the lat
     */
    public double getLatitude() {
        return lat;
    }

    /**
     * Returns the lon
     *
     * @return the lon
     */
    public double getLongitude() {
        return lon;
    }

    /**
     * Returns the altitude
     *
     * @return the altitude
     */
    public int getAltitude() {
        return altitude;
    }

    /**
     * Calculates distance to other coords using haversine distance and ignoring the points height. Calculated
     * according with <url>https://stackoverflow.com/a/16794680</url>
     *
     * @param other secondary coords to calculate distance
     * @return the distance between the two coordinates in kilometers
     */
    public double distanceIgnoringHeight(Coordinates other) {
        if ((lat == other.getLatitude()) && (lon == other.getLongitude())) {
            return 0;
        } else {
            final int R = 6371; // Radius of the earth
            double latDistance = Math.toRadians(other.getLatitude() - lat);
            double lonDistance = Math.toRadians(other.getLongitude() - lon);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(other.getLatitude()))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c;
            return distance;
        }
    }

    /**
     * Calculates distance to other coords using haversine distance Calculated
     * according with <url>https://stackoverflow.com/a/16794680</url>
     *
     * @param other secondary coords to calculate distance
     * @return the distance between the two coordinates in kilometers
     */
    public double distance(Coordinates other) {
        if ((lat == other.getLatitude()) && (lon == other.getLongitude()) && (altitude == other.getAltitude())) {
            return 0;
        } else {
            double height = ((double) altitude - other.getAltitude()) / 1000;

            double distance = Math.pow(distanceIgnoringHeight(other), 2) + Math.pow(height, 2);
            return Math.sqrt(distance);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null
                || getClass()
                != o.getClass()) {
            return false;
        }
        Coordinates that = (Coordinates) o;
        return Double.compare(that.lat, lat) == 0
                && Double.compare(that.lon, lon) == 0
                && Integer.compare(that.altitude,
                altitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, altitude);
    }

    @Override
    public String toString() {
        return "Coordinates{"
                + "latitude="
                + lat
                + ", longitude="
                + lon
                + ", altitude="
                + altitude
                + '}';
    }

    /**
     * Returns the corresponding cartesian value of x to this coordinate in m
     *
     * @return the corresponding cartesian value of x to this coordinate in m
     */
    public double getCoordinateX() {
        final int R = 6371; // Radius of the earth
        return R * Math.cos(lat) * Math.cos(lon) * 1000;
    }

    /**
     * Returns the corresponding cartesian value of y to this coordinate in m
     *
     * @return the corresponding cartesian value of y to this coordinate in m
     */
    public double getCoordinateY() {
        final int R = 6371; // Radius of the earth
        return R * Math.cos(lat) * Math.sin(lon) * 1000;
    }

    /**
     * Returns the corresponding cartesian value of z to this coordinate in m
     *
     * @return the corresponding cartesian value of z to this coordinate in m
     */
    public double getCoordinateZ() {
        final int R = 6371; // Radius of the earth
        return R * Math.sin(lat) * 1000;
    }

}
