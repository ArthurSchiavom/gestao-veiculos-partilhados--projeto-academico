package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ChargeTimeComparator;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ScooterDescriptionComparator;
import lapr.project.utils.Utils;
import lapr.project.utils.physics.calculations.PhysicsMethods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkChargingReportController {
    /**
     * Get a report for the escooter charging status at a given park.
     *
     * @param parkIdentification Park Identification.
     * @param outputFileName     Path to file where vehicles information should be
     *                           written, according to file output/chargingReport
     *                           .csv.
     *                           Sort items by descending order of time to finish
     *                           charge in seconds and secondly by ascending
     *                           escooter description order.
     * @return The number of escooters charging at the moment that are not
     * 100% fully charged.
     */
    public long retrieveParkChargingReport(String parkIdentification, String outputFileName) throws SQLException, IOException {
        final String FAIL_MESSAGE = "Could not find any scooter.";
        List<String> fileLines = new ArrayList<>();
        List<ElectricScooter> scooterList = Company.getInstance().getParkAPI().fetchVehiclesAtPark(parkIdentification
                , ElectricScooter.class);
        if (scooterList.isEmpty()) {
            fileLines.add(FAIL_MESSAGE);
            Utils.writeToFile(fileLines, outputFileName);
            return 0;
        }
        Park park = Company.getInstance().getParkAPI().fetchParkById(parkIdentification);
        Collections.sort(scooterList, new ChargeTimeComparator(park).thenComparing(new ScooterDescriptionComparator()));
        fileLines.add("escooter description;actual battery capacity;time to finish charge in seconds");
        List<ElectricScooter> scootersFullyCharged = new ArrayList<>();
        for (ElectricScooter scooter : scooterList) {
            fileLines.add(scooter.getDescription() + ";" + scooter.getActualBatteryCapacity() + ";" + PhysicsMethods.timeToChargeInSeconds(scooter, park));
            if(scooter.getActualBatteryCapacity()==100) {
                scootersFullyCharged.add(scooter);
            }
        }
        scooterList.removeAll(scootersFullyCharged);
        Utils.writeToFile(fileLines, outputFileName);
        return scooterList.size();
    }
}
