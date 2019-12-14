package lapr.project.model.register;

import lapr.project.data.DataHandler;
import lapr.project.model.Company;
import lapr.project.model.vehicles.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleRegister {
    private final DataHandler dh;

    public VehicleRegister(DataHandler dh) {
        this.dh = dh;
    }

    public Vehicle fetchVehicle(int vehicleId) throws SQLException {
        Vehicle vehicle = null;
        DataHandler handler = Company.getInstance().getDataHandler();

        PreparedStatement stm = handler.prepareStatement("select * from vehicles where vehicle_id = ?");
        stm.setInt(1, vehicleId);
        ResultSet rs = handler.executeQuery(stm);
        if (!rs.next())
            return null;

        boolean available;
        available = (rs.getInt(3) == 1) ? true : false;
        VehicleType vehicleType = VehicleType.parseVehicleType(rs.getString(2));
        String childTableName = null;
        switch (vehicleType) {
            case BICYCLE:
                childTableName = "bicycles";
                break;
            case ELECTRIC_SCOOTER:
                childTableName = "electric_scooters";
        }

        PreparedStatement stm2 = handler.prepareStatement("select * from " + childTableName + " where vehicle_id = ?");
        stm2.setInt(1, vehicleId);
        ResultSet rs2 = handler.executeQuery(stm2);
        if (!rs2.next())
            throw new SQLException("Incorrectly filled data in the database");

        switch (vehicleType) {
            case BICYCLE:
                vehicle = new Bicycle(vehicleId, rs.getDouble(4), rs.getDouble(5), rs.getInt(6), rs.getFloat(8), rs.getFloat(9), rs.getInt(7), available, vehicleType, rs2.getInt(2), rs2.getString(3));
                break;
            case ELECTRIC_SCOOTER:
                vehicle = new ElectricScooter(vehicleId, rs.getDouble(4), rs.getDouble(5), rs.getInt(6), rs.getFloat(8), rs.getFloat(9), rs.getInt(7), available, vehicleType, ElectricScooterType.parseScooterType(rs2.getString(2)), rs2.getInt(3), rs2.getFloat(4), rs2.getString(5));
        }

        return vehicle;
    }

    public void addBicycle(double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, int size, String description) throws SQLException {

        CallableStatement cs = dh.prepareCall(
                "{call register_bicycle(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        int availableInt = available ? 1 : 0;
        cs.setInt(1, availableInt);
        cs.setDouble(2, latitude);
        cs.setDouble(3, longitude);
        cs.setInt(4, altitude);
        cs.setInt(5, weight);
        cs.setFloat(6, aerodynamic_coefficient);
        cs.setFloat(7, frontal_area);
        cs.setInt(8, size);
        cs.setString(9, description);

        dh.execute(cs);
    }

    public void addEletricScooter(double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, ElectricScooterType type, String description, float maxBatteryCapacity, int actualBatteryCapacity) throws SQLException {
        CallableStatement cs = dh.prepareCall(
                "{call register_electric_scooter(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        int availableInt = available ? 1 : 0;
        cs.setDouble(2, latitude);
        cs.setInt(1, availableInt);
        cs.setDouble(3, longitude);
        cs.setInt(4, altitude);
        cs.setInt(5, weight);
        cs.setFloat(6, aerodynamic_coefficient);
        cs.setFloat(7, frontal_area);
        cs.setString(8, type.getSQLName());
        cs.setString(9, description);
        cs.setFloat(10, maxBatteryCapacity);
        cs.setInt(11, actualBatteryCapacity);

        dh.execute(cs);
    }

}
