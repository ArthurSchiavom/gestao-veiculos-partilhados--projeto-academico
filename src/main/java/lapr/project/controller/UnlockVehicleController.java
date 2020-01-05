package lapr.project.controller;

import lapr.project.data.registers.Company;

import java.sql.SQLException;

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
	 * @param exportFileName Write the unlocked vehicle information to a file,
	 *                       according to file output/escooters.csv.
	 */
	public void startTripPark(String username, String park, String exportFileName)
			throws SQLException {
		//TODO: Implement file export
		String vehicleDesc = company.getParkAPI().fetchScooterHighestBattery(park);
		company.getTripAPI().startTrip(username, vehicleDesc);
	}

	/**
	 * Unlocks any escooter at one park that allows travelling to the
	 * destination.
	 *
	 * @param parkIdentification        Park Identification where to unlock escooter.
	 * @param username                  User that requested the unlock.
	 * @param destinyLatitudeInDegrees  Destiny latitude in Decimal Degrees.
	 * @param destinyLongitudeInDegrees Destiny longitude in Decimal Degrees.
	 * @param outputFileName            Write the unlocked vehicle information to a file,
	 *                                  according to file output/escooters.csv.
	 */
	public void startTripParkDest(String parkIdentification, String username,
								  double destinyLatitudeInDegrees, double destinyLongitudeInDegrees,
								  String outputFileName) throws SQLException {

	}
}
