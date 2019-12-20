package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.vehicles.*;

import java.sql.*;
import java.util.List;

public class VehicleRegister {
    private final DataHandler dataHandler;

    public VehicleRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public Vehicle fetchVehicle(String vehicleDescription) throws SQLException {
        Vehicle vehicle = null;
        DataHandler handler = Company.getInstance().getDataHandler();

        PreparedStatement stm = handler.prepareStatement("select * from vehicles where description = ?");
        stm.setString(1, vehicleDescription);
        ResultSet rs = handler.executeQuery(stm);
        if (!rs.next())
            return null;

        VehicleType vehicleType = VehicleType.parseVehicleType(rs.getString("vehicle_type_name"));
        String childTableName = null;
        assert vehicleType != null;
        switch (vehicleType) {
            case BICYCLE:
                childTableName = "bicycles";
                break;
            case ELECTRIC_SCOOTER:
                childTableName = "electric_scooters";
        }

        @SuppressWarnings("SqlResolve")
        PreparedStatement stm2 = handler.prepareStatement("select * from " + childTableName + " where vehicle_description = ?");
        stm2.setString(1, vehicleDescription);
        ResultSet rs2 = handler.executeQuery(stm2);
        if (!rs2.next())
            throw new SQLException("Incorrectly filled data in the database");

        switch (vehicleType) {
            case BICYCLE:
                vehicle = new Bicycle(rs.getString("description"), rs.getFloat("aerodynamic_coefficient"),
                        rs.getFloat("frontal_area"), rs.getInt("weight"),
                        rs.getBoolean("available"), rs2.getInt("bicycle_size"));
                break;
            case ELECTRIC_SCOOTER:
                vehicle = new ElectricScooter(rs.getString("description"), rs.getFloat("aerodynamic_coefficient"),
                        rs.getFloat("frontal_area"), rs.getInt("weight"),
                        rs.getBoolean("available"), ElectricScooterType.parseScooterType(
                        rs2.getString("electric_scooter_type_name")),
                        rs2.getInt("actual_battery_capacity"), rs2.getFloat("max_battery_capacity"),
                        rs2.getInt("engine_power"));
        }
        return vehicle;
    }

    /**
     * Registers a bicycle in the database.
     *
     * @param aerodynamicCoefficient
     * @param frontalArea
     * @param weight
     * @param size
     * @param description
     * @param parkLatitude
     * @param parkLongitude
     * @return
     * @throws SQLException
     */
    private void registerBicycleNoCommit(float aerodynamicCoefficient, float frontalArea,
                                         int weight, int size,
                                         String description, double parkLatitude, double parkLongitude) throws SQLException {

        CallableStatement cs = dataHandler.prepareCall(
                "{call register_bicycle(?, ?, ?, ?, ?, ?, ?)}");
        cs.setInt(1, weight);
        cs.setFloat(2, aerodynamicCoefficient);
        cs.setFloat(3, frontalArea);
        cs.setInt(4, size);
        cs.setString(5, description);
        cs.setDouble(6, parkLatitude);
        cs.setDouble(7, parkLongitude);
        dataHandler.execute(cs);
    }

    private void registerEletricScooterNoCommit(float aerodynamicCoefficient, float frontalArea,
                                                int weight,
                                                ElectricScooterType type, String description,
                                                float maxBatteryCapacity,
                                                int actualBatteryCapacity,
                                                int enginePower, double parkLatitude, double parkLongitude) throws SQLException {
        CallableStatement cs = dataHandler.prepareCall(
                "{call register_electric_scooter(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        cs.setInt(1, weight);
        cs.setFloat(2, aerodynamicCoefficient);
        cs.setFloat(3, frontalArea);
        cs.setString(4, type.getSQLName());
        cs.setString(5, description);
        cs.setFloat(6, maxBatteryCapacity);
        cs.setInt(7, actualBatteryCapacity);
        cs.setInt(8, enginePower);
        cs.setDouble(9, parkLatitude);
        cs.setDouble(10, parkLongitude);
        dataHandler.execute(cs);
    }

    public void registerBicycles(List<Float> aerodynamicCoefficient, List<Float> frontalArea,
                                  List<Integer> weight, List<Integer> size,
                                  List<String> description, List<Double> parkLatitude, List<Double> parkLongitude) throws SQLException {
        try {
            for (int i = 0; i < aerodynamicCoefficient.size(); i++) {
                registerBicycleNoCommit(aerodynamicCoefficient.get(i), frontalArea.get(i), weight.get(i),
                        size.get(i), description.get(i), parkLatitude.get(i), parkLongitude.get(i));
            }
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try { dataHandler.rollbackTransaction(); } catch (SQLException e2) {}
            throw e;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("All the parameter lists must have the same size.");
        }
    }

    public void registerElectricScooters(List<Float> aerodynamicCoefficient, List<Float> frontalArea,
                                        List<Integer> weight,
                                        List<ElectricScooterType> type, List<String> description,
                                        List<Float> maxBatteryCapacity,
                                        List<Integer> actualBatteryCapacity,
                                        List<Integer> enginePower, List<Double> parkLatitude, List<Double> parkLongitude) throws SQLException {
        try {
            for (int i = 0; i < aerodynamicCoefficient.size(); i++) {
                registerEletricScooterNoCommit(aerodynamicCoefficient.get(i), frontalArea.get(i), weight.get(i),
                        type.get(i), description.get(i), maxBatteryCapacity.get(i),
                        actualBatteryCapacity.get(i), enginePower.get(i), parkLatitude.get(i), parkLongitude.get(i));
            }
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try { dataHandler.rollbackTransaction(); } catch (SQLException e2) {}
            throw e;
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("All the parameter lists must have the same size.");
        }
    }
}
