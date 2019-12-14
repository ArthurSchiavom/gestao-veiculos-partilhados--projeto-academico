package lapr.project.model.register;

import lapr.project.data.DataHandler;
import lapr.project.model.Company;
import lapr.project.model.vehicles.ScooterType;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleRegister {
    private final DataHandler dh;

    public VehicleRegister(DataHandler dh) {
        this.dh = dh;
    }

    public Vehicle fetchVehicle(int vehicleId) {
        Vehicle vehicle = null;
        DataHandler handler = Company.getInstance().getDataHandler();
        try {
            PreparedStatement stm = handler.prepareStatement("select * from vehicles where vehicle_id = ?");
            stm.setInt(1, vehicleId);
            ResultSet rs = handler.executeQuery(stm);

            boolean available;
            available = (rs.getInt(3) == 1) ? true : false;

            vehicle = new Vehicle(vehicleId, rs.getDouble( 4), rs.getDouble(5), rs.getInt(6), rs.getFloat(8), rs.getFloat(9), rs.getInt(7), available, VehicleType.parseVehicleType(rs.getString(2)));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.getErrorCode();
        }

        return vehicle;
    }

    public boolean addBicycle(double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, VehicleType type, int size, String description) {
        try {
            CallableStatement ps = dh.prepareCall(
                    "{call register_bicycle(?, ?, ?, ?, ?, ?, ?, ?)}");
            ps.setString(1, type.name().toLowerCase());
            int availableInt;
            if (available)
                availableInt = 1;
            else
                availableInt = 0;
            ps.setInt(2, availableInt);
            ps.setDouble(4, latitude);
            ps.setDouble(5, longitude);
            ps.setInt(6, altitude);
            ps.setInt(7, weight);
            ps.setFloat(8, aerodynamic_coefficient);
            ps.setFloat(9, frontal_area);
            int changes = dh.executeUpdate(ps);
            if (changes == 0)
                return false;
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addScooter(int id, double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, VehicleType type, ScooterType scooterType, int actual_battery_capacity, float max_battery_capacity, String description) {
        try {
            PreparedStatement ps = dh.prepareStatement("INSERT INTO vehicles(latitude, longitude, altitude, " +
                    "aerodynamic_coefficient, frontal_area, weight, available, type, scooterType, actual_battery_capacity, " +
                    "max_battery_capacity, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?) returning vehicle_id");


            int changes = dh.executeUpdate(ps);
            if (changes == 0)
                return false;
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
