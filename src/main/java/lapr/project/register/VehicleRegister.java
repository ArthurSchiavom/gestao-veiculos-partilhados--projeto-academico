package lapr.project.register;

import lapr.project.model.Company;
import lapr.project.model.Vehicles.Vehicle;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VehicleRegister {
    public Vehicle fetchVehicle(int vehicleId) {
        Vehicle vehicle = null;
        try {
            PreparedStatement stm = Company.getInstance().getConnection().prepareStatement("select * from vehicles where vehicle_id = ?");
            stm.setInt(1, vehicleId);
        } catch (SQLException e) {

        }
        return vehicle;
    }
}
