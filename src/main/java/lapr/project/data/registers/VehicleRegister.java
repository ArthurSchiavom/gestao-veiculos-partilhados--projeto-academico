package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.vehicles.*;

import java.sql.*;

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

        VehicleType vehicleType = VehicleType.parseVehicleType(rs.getString(2));
        String childTableName = null;
        assert vehicleType != null;
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
                vehicle = new Bicycle(vehicleId, rs.getFloat("aerodynamic_coefficient"),
                        rs.getFloat("frontal_area"), rs.getInt("weight"),
                        rs.getBoolean("available"), rs2.getInt("bicycle_size"),
                        rs2.getString("bicycle_description"));
                break;
            case ELECTRIC_SCOOTER:
                vehicle = new ElectricScooter(vehicleId, rs.getFloat("aerodynamic_coefficient"),
                        rs.getFloat("frontal_area"), rs.getInt("weight"),
                        rs.getBoolean("available"), ElectricScooterType.parseScooterType(
                        rs2.getString("electric_scooter_type_name")),
                        rs2.getInt("actual_battery_capacity"), rs2.getFloat("max_battery_capacity"),
                        rs2.getString("electric_scooter_description"), rs2.getInt("engine_power"));
        }
        return vehicle;
    }

    public void registerBicycle(float aerodynamicCoefficient, float frontalArea,
                                int weight, boolean available, int size,
                                String description) throws SQLException {

        CallableStatement cs = dh.prepareCall(
                "{call register_bicycle(?, ?, ?, ?, ?, ?)}");
        int availableInt = available ? 1 : 0;
        cs.setInt(1, availableInt);
        cs.setInt(2, weight);
        cs.setFloat(3, aerodynamicCoefficient);
        cs.setFloat(4, frontalArea);
        cs.setInt(5, size);
        cs.setString(6, description);
        dh.execute(cs);
        dh.commitTransaction();
    }

    public void registerEletricScooter(float aerodynamicCoefficient, float frontalArea,
                                       int weight, boolean available,
                                       ElectricScooterType type, String description,
                                       float maxBatteryCapacity,
                                       int actualBatteryCapacity,
                                       int enginePower) throws SQLException {
        CallableStatement cs = dh.prepareCall(
                "{call register_electric_scooter(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        int availableInt = available ? 1 : 0;
        cs.setInt(1, availableInt);
        cs.setInt(2, weight);
        cs.setFloat(3, aerodynamicCoefficient);
        cs.setFloat(4, frontalArea);
        cs.setString(5, type.getSQLName());
        cs.setString(6, description);
        cs.setFloat(7, maxBatteryCapacity);
        cs.setInt(8, actualBatteryCapacity);
        cs.setInt(9, enginePower);

        dh.execute(cs);

        dh.commitTransaction();
    }

}
