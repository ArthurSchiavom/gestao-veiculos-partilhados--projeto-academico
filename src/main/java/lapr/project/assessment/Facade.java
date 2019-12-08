package lapr.project.assessment;

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

//    @Override
//    public int getFreeSlotsAtPArk(double v, double v1, String s) {
//        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * Distance is returns in metres, rounded to the unit e.g. (281,58 rounds
//     * to 282);
//     *
//     * @param v  Latitude in degrees.
//     * @param v1 Longitude in degrees.
//     * @param s  Filename for output.
//     */
//    @Override
//    public void getNearestParks(double v, double v1, String s) {
//        BufferedWriter writer = null;
//        try {
//            writer = new BufferedWriter(new FileWriter(s));
//            writer.write("41.152712,-8.609297,494");
//            writer.newLine();
//            writer.write("41.145883,-8.610680,282");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int distanceTo(double v, double v1, double v2, double v3) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public long unlockBicycle(String s, String s1) {
//        return 0;
//    }
//
//    @Override
//    public long lockBicycle(String s, double v, double v1) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public double getUserCurrentDebt(String s, String s1) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public double getUserCurrentPoints(String s, String s1) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public long unlockAnyBicycleAtPark(double v, double v1, String s,
//                                       String s1) {
//        return 0;
//    }
//
//    @Override
//    public long unlockAnyElectricBicycleAtPark(double v, double v1, String s,
//                                               String s1) {
//        return 0;
//    }
//
//    @Override
//    public double calculateElectricalEnergyToTravelFromOneLocationToAnother(
//            double v, double v1, double v2, double v3, String s) {
//        return 0;
//    }
//
//    @Override
//    public int suggestElectricalBicyclesToGoFromOneParkToAnother(double v,
//                                                                 double v1,
//                                                                 double v2,
//                                                                 double v3,
//                                                                 String s,
//                                                                 String s1) {
//        return 0;
//    }
//
//    @Override
//    public long forHowLongWasTheBicycleUnlocked(String s) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public long shortestRouteBetweenTwoParks(double v, double v1, double v2,
//                                             double v3, String s) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public long mostEnergyEfficientRouteBetweenTwoParks(double v, double v1,
//                                                        double v2, double v3,
//                                                        String s, String s1,
//                                                        String s2) {
//        return 0;
//    }
//
//    @Override
//    public long shortestRouteBetweenTwoParksForGivenPOIs(double v, double v1,
//                                                         double v2, double v3,
//                                                         String s, String s1) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public long getParkChargingReportForPark(double v, double v1, String s) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public int suggestRoutesBetweenTwoLocations(double v, double v1, double v2,
//                                                double v3, String s, String s1,
//                                                int i, boolean b, String s2,
//                                                String s3, String s4) {
//        return 0;
//    }
//
//    @Override
//    public double getInvoiceForMonth(int i, String s, String s1) {
//        throw new UnsupportedOperationException();
//    }
}
