package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.vehicles.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRegister {
    private final DataHandler dataHandler;

    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String TYPE_FIELD_NAME = "vehicle_type_name";
    private static final String AVAILABLE_FIELD_NAME = "available";
    private static final String WEIGHT_FIELD_NAME = "weight";
    private static final String AERO_COEFFICIENT_FIELD_NAME = "aerodynamic_coefficient";
    private static final String FRONTAL_AREA_FIELD_NAME =  "frontal_area";

    private static final String BICYCLE_SIZE_FIELD_NAME =  "bicycle_size";

    private static final String ESCOOTER_TYPE_FIELD_NAME =  "electric_scooter_type_name";
    private static final String ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME =  "actual_battery_capacity";
    private static final String ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME =  "max_battery_capacity";
    private static final String ESCOOTER_ENGINE_POWER_FIELD_NAME =  "engine_power";

    public VehicleRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<Vehicle> fetchAllVehicles(VehicleType type) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        PreparedStatement ps2;
        ResultSet rs2;
        try {
            ps = dataHandler.prepareStatement("select * from VEHICLES where VEHICLE_TYPE_NAME = " + type.getSQLName());
            dataHandler.queueForClose(ps);
            rs = dataHandler.executeQuery(ps);
            dataHandler.queueForClose(rs);

            while (rs.next()) {
                String description = rs.getString(DESCRIPTION_FIELD_NAME);
                switch (type) {
                    case BICYCLE:
                        ps2 = dataHandler.prepareStatement("SELECT * from bicycles where VEHICLE_DESCRIPTION = " + description);
                        dataHandler.queueForClose(ps2);
                        rs2 = dataHandler.executeQuery(ps2);
                        dataHandler.queueForClose(rs2);
                        rs2.next();
                        vehicles.add(new Bicycle(description, rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                                rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME), rs.getBoolean(AVAILABLE_FIELD_NAME),
                                rs2.getInt(BICYCLE_SIZE_FIELD_NAME)));
                        break;
                    case ELECTRIC_SCOOTER:
                        ps2 = dataHandler.prepareStatement("SELECT * from ELECTRIC_SCOOTERS where VEHICLE_DESCRIPTION = " + description);
                        dataHandler.queueForClose(ps2);
                        rs2 = dataHandler.executeQuery(ps2);
                        dataHandler.queueForClose(rs2);
                        rs2.next();
                        vehicles.add(new ElectricScooter(description, rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                                rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME), rs.getBoolean(AVAILABLE_FIELD_NAME),
                                ElectricScooterType.parseScooterType(rs2.getString(ESCOOTER_TYPE_FIELD_NAME)),
                                rs2.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME), rs2.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME),
                                rs2.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME)));
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            dataHandler.closeQueuedAutoCloseables();
        }

        return vehicles;
    }

    public Vehicle fetchVehicle(String vehicleDescription) throws SQLException {
        Vehicle vehicle = null;

        PreparedStatement stm = dataHandler.prepareStatement("select * from vehicles where description = ?");
        stm.setString(1, vehicleDescription);
        ResultSet rs = dataHandler.executeQuery(stm);
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
        PreparedStatement stm2 = dataHandler.prepareStatement("select * from " + childTableName + " where vehicle_description = ?");
        stm2.setString(1, vehicleDescription);
        ResultSet rs2 = dataHandler.executeQuery(stm2);
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
