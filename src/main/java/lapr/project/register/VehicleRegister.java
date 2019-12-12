package lapr.project.register;

import lapr.project.data.DataHandler;
import lapr.project.model.Company;
import lapr.project.model.Vehicles.Vehicle;
import lapr.project.model.Vehicles.VehicleType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleRegister {
    public Vehicle fetchVehicle(int vehicleId) {
        Vehicle vehicle = null;
        DataHandler handler = Company.getInstance().getDataHandler();
        try {
            PreparedStatement stm = handler.prepareStatement("select * from vehicles where vehicle_id = ?");
            handler.setInt(stm, 1, vehicleId);
            ResultSet rs = handler.executeQuery(stm);

            boolean available;
            if (handler.getInt(rs, 3) == 1)
                available = true;
            else
                available = false;

            vehicle = new Vehicle(vehicleId, handler.getString(rs, 1), handler.getDouble(rs, 4), handler.getDouble(rs, 5), handler.getDouble(rs, 6), available, VehicleType.parseVehicleType(handler.getString(rs, 2)));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.getErrorCode();
        }

        return vehicle;
    }
}
