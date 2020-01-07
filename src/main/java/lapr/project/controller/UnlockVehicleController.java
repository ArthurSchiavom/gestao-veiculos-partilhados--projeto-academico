package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.data.registers.ParkAPI;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnlockVehicleController {
    private final Company company;

    public UnlockVehicleController(Company company) {
        this.company = company;
    }

    /**
     * Unlocks a vehicle given it's description
     *
     * @param username           the user that requested the unlock
     * @param vehicleDescription the description of the vehicle that was requested
     * @throws SQLException if any errors happen
     */
    public void startTrip(String username, String vehicleDescription) throws SQLException {
        company.getTripAPI().startTrip(username, vehicleDescription);
    }

    /**
     * Unlocks the vehicle with the most battery in a park
     *
     * @param park           Park Identification where to unlock escooter.
     * @param username       User that requested the unlock.
     * @param outputFileName Write the unlocked vehicle information to a file,
     *                       according to file output/escooters.csv.
     */
    public void startTripPark(String username, String park, String outputFileName) throws SQLException, IOException {
        final String FAIL_MESSAGE = "Could not find any available scooter at desired park.";
        String vehicleDesc = company.getParkAPI().fetchScooterHighestBattery(park);
        List<String> fileLines = new ArrayList<>();
        if (vehicleDesc == null) {  //Checks that a description was found
            fileLines.add(FAIL_MESSAGE);
            Utils.writeToFile(fileLines, outputFileName);
            return;
        }
        ElectricScooter scooter = (ElectricScooter) Company.getInstance().getVehicleAPI().fetchVehicle(vehicleDesc);
        if (scooter == null) {  //Checks that a scooter was found
            fileLines.add(FAIL_MESSAGE);
            Utils.writeToFile(fileLines, outputFileName);
            return;
        }
        fileLines.add("escooter description;type;actual battery capacity");
        fileLines.add(scooter.generateExportString());
        Utils.writeToFile(fileLines, outputFileName);
        company.getTripAPI().startTrip(username, vehicleDesc);
    }

    /**
     * Unlocks any escooter at one park that allows travelling to the
     * destination.
     *
     * @param park                      Park Identification where to unlock escooter.
     * @param username                  User that requested the unlock.
     * @param destinyLatitudeInDegrees  Destiny latitude in Decimal Degrees.
     * @param destinyLongitudeInDegrees Destiny longitude in Decimal Degrees.
     * @param outputFileName            Write the unlocked vehicle information to a file,
     *                                  according to file output/escooters.csv.
     */
    public void startTripParkDest(String park, String username, double destinyLatitudeInDegrees,
                                  double destinyLongitudeInDegrees, String outputFileName) throws SQLException
            , IOException {
        final String FAIL_MESSAGE = "Could not find any scooter with enough energy in the park.";
        List<String> fileLines = new ArrayList<>();
        List<ElectricScooter> scootersWithEnoughEnergy = ParkAPI.filterScootersWithAutonomy(username, park,
                destinyLatitudeInDegrees, destinyLongitudeInDegrees);
        if(scootersWithEnoughEnergy.isEmpty()) {
            fileLines.add(FAIL_MESSAGE);
            Utils.writeToFile(fileLines, outputFileName);
            return;
        }
        ElectricScooter scooter = scootersWithEnoughEnergy.get(0);
        fileLines.add("escooter description;type;actual battery capacity");
        fileLines.add(scooter.generateExportString());
        Utils.writeToFile(fileLines, outputFileName);
        company.getTripAPI().startTrip(username, scootersWithEnoughEnergy.get(0).getDescription());
    }
}
