package lapr.project.data.registers;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.*;

import javax.mail.MessagingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleAPI {
    private final DataHandler dataHandler;

    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String UNIQUE_NUMBER_FIELD_NAME = "unique_number";
    private static final String VEHICLE_TYPE_FIELD_NAME = "vehicle_type_name";
    private static final String AVAILABLE_FIELD_NAME = "available";
    private static final String WEIGHT_FIELD_NAME = "weight";
    private static final String AERO_COEFFICIENT_FIELD_NAME = "aerodynamic_coefficient";
    private static final String FRONTAL_AREA_FIELD_NAME = "frontal_area";
    private static final String BICYCLE_SIZE_FIELD_NAME = "bicycle_size";
    private static final String ESCOOTER_TYPE_FIELD_NAME = "electric_scooter_type_name";
    private static final String ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME = "actual_battery_capacity";
    private static final String ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME = "max_battery_capacity";
    private static final String ESCOOTER_ENGINE_POWER_FIELD_NAME = "engine_power";

    public VehicleAPI(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Fetches all vehicles from the database of the type vehicle type
     * @param type vehicle type
     * @return all vehicles of that type
     * @throws SQLException in case of an sql exception
     */
    public List<Vehicle> fetchAllVehicles(VehicleType type) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        PreparedStatement ps2;
        ResultSet rs2;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            ps = dataHandler.prepareStatement("select * from VEHICLES where VEHICLE_TYPE_NAME = ?");
            ps.setString(1,type.getSQLName());
            autoCloseableManager.addAutoCloseable(ps);
            rs = dataHandler.executeQuery(ps);
            autoCloseableManager.addAutoCloseable(rs);

            while (rs.next()) {
                String description = rs.getString(DESCRIPTION_FIELD_NAME);
                switch (type) {
                    case BICYCLE:
                        ps2 = dataHandler.prepareStatement("SELECT * from bicycles where VEHICLE_DESCRIPTION = ?");
                        ps2.setString(1,description);
                        autoCloseableManager.addAutoCloseable(ps2);
                        rs2 = dataHandler.executeQuery(ps2);
                        autoCloseableManager.addAutoCloseable(rs2);
                        rs2.next();
                        vehicles.add(new Bicycle(rs.getInt(UNIQUE_NUMBER_FIELD_NAME), description, rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                                rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME), rs.getBoolean(AVAILABLE_FIELD_NAME),
                                rs2.getInt(BICYCLE_SIZE_FIELD_NAME)));
                        break;
                    case ELECTRIC_SCOOTER:
                        ps2 = dataHandler.prepareStatement("SELECT * from ELECTRIC_SCOOTERS where VEHICLE_DESCRIPTION = ?");
                        ps2.setString(1,description);
                        autoCloseableManager.addAutoCloseable(ps2);
                        rs2 = dataHandler.executeQuery(ps2);
                        autoCloseableManager.addAutoCloseable(rs2);
                        rs2.next();
                        vehicles.add(new ElectricScooter(rs.getInt(UNIQUE_NUMBER_FIELD_NAME), description, rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                                rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME), rs.getBoolean(AVAILABLE_FIELD_NAME),
                                ElectricScooterType.parseScooterType(rs2.getString(ESCOOTER_TYPE_FIELD_NAME)),
                                rs2.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME), rs2.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME),
                                rs2.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME)));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to access the database", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }

        return vehicles;
    }

    /**
     * Fetch vehicle by vehicle description
     * @param vehicleDescription description of the vehicle
     * @return the vehicle from the database
     * @throws SQLException in case of an sql exception
     */
    public Vehicle fetchVehicle(String vehicleDescription) throws SQLException {
        Vehicle vehicle = null;

        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement stm = dataHandler.prepareStatement("select * from vehicles where description like ?");
            autoCloseableManager.addAutoCloseable(stm);
            stm.setString(1, vehicleDescription);
            ResultSet rs = dataHandler.executeQuery(stm);
            autoCloseableManager.addAutoCloseable(rs);
            if (!rs.next())
                return null;

            VehicleType vehicleType = VehicleType.parseVehicleType(rs.getString(VEHICLE_TYPE_FIELD_NAME));
            String childTableName = null;
            if (vehicleType == null)
                throw new IllegalStateException("Unregistered vehicle type");

            switch (vehicleType) {
                case BICYCLE:
                    childTableName = "bicycles";
                    break;
                case ELECTRIC_SCOOTER:
                    childTableName = "electric_scooters";
            }

            @SuppressWarnings("SqlResolve")
            PreparedStatement stm2 = dataHandler.prepareStatement("select * from " + childTableName + " where vehicle_description like ?");
            autoCloseableManager.addAutoCloseable(stm2);
            stm2.setString(1, vehicleDescription);
            ResultSet rs2 = dataHandler.executeQuery(stm2);
            autoCloseableManager.addAutoCloseable(rs2);
            if (!rs2.next())
                throw new SQLException("Incorrectly filled data in the database");

            switch (vehicleType) {
                case BICYCLE:
                    vehicle = new Bicycle(rs.getInt(UNIQUE_NUMBER_FIELD_NAME), rs.getString(DESCRIPTION_FIELD_NAME),
                            rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                            rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME),
                            rs.getBoolean(AVAILABLE_FIELD_NAME), rs2.getInt(BICYCLE_SIZE_FIELD_NAME));
                    break;
                case ELECTRIC_SCOOTER:
                    vehicle = new ElectricScooter(rs.getInt(UNIQUE_NUMBER_FIELD_NAME), rs.getString(DESCRIPTION_FIELD_NAME),
                            rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                            rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME),
                            rs.getBoolean(AVAILABLE_FIELD_NAME), ElectricScooterType.parseScooterType(
                            rs2.getString(ESCOOTER_TYPE_FIELD_NAME)),
                            rs2.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME), rs2.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME),
                            rs2.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch vehicle from the database.", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return vehicle;
    }

    /**
     * registers a batch of vehicles
     */
    private void registerBicycleNoCommit(float aerodynamicCoefficient, float frontalArea,
                                         int weight, int size,
                                         String description, double parkLatitude, double parkLongitude) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            CallableStatement cs = dataHandler.prepareCall(
                    "{call register_bicycle(?, ?, ?, ?, ?, ?, ?)}");
            autoCloseableManager.addAutoCloseable(cs);
            cs.setInt(1, weight);
            cs.setFloat(2, aerodynamicCoefficient);
            cs.setFloat(3, frontalArea);
            cs.setInt(4, size);
            cs.setString(5, description);
            cs.setDouble(6, parkLatitude);
            cs.setDouble(7, parkLongitude);
            dataHandler.execute(cs);
        } catch (SQLException e) {
            throw new SQLException("Failed to register bicycle into the database.", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     * registers a batch of eletric scooters in one transaction
     */
    private void registerEletricScooterNoCommit(float aerodynamicCoefficient, float frontalArea,
                                                int weight,
                                                ElectricScooterType type, String description,
                                                float maxBatteryCapacity,
                                                int actualBatteryCapacity,
                                                int enginePower, double parkLatitude, double parkLongitude) throws SQLException, MessagingException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement vehiclesInsert = dataHandler.prepareStatement("INSERT INTO vehicles(description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");
            autoCloseableManager.addAutoCloseable(vehiclesInsert);
            vehiclesInsert.setString(1, description);
            vehiclesInsert.setString(2, VehicleType.ELECTRIC_SCOOTER.getSQLName());
            vehiclesInsert.setInt(3, 1);
            vehiclesInsert.setInt(4, weight);
            vehiclesInsert.setFloat(5, aerodynamicCoefficient);
            vehiclesInsert.setFloat(6, frontalArea);
            dataHandler.execute(vehiclesInsert);

            PreparedStatement eScootersInsert = dataHandler.prepareStatement(
                    "insert into electric_scooters(vehicle_description, electric_scooter_type_name, max_battery_capacity, " +
                            "actual_battery_capacity, engine_power) " +
                            "values(?, ?, ?, ?, ?)");
            autoCloseableManager.addAutoCloseable(eScootersInsert);
            eScootersInsert.setString(1, description);
            eScootersInsert.setString(2, type.getSQLName());
            eScootersInsert.setFloat(3, maxBatteryCapacity);
            eScootersInsert.setInt(4, actualBatteryCapacity);
            eScootersInsert.setInt(5, enginePower);
            dataHandler.execute(eScootersInsert);

            Company company = Company.getInstance();
            Park park = company.getParkAPI().fetchParkByCoordinates(parkLatitude, parkLongitude);
            company.getTripAPI().lockVehicle(park.getId(), description);
        } catch (SQLException e) {
            throw new SQLException("Failed to insert new electric scooter into the database.", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     * registers a batch of bicycles in one transaction
     */
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
            try {
                dataHandler.rollbackTransaction();
            } catch (SQLException e2) {
            }
            throw new SQLException("failed to batch register bicycles.", e.getSQLState(), e.getErrorCode());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("All the parameter lists must have the same size.");
        }
    }

    /**
     * registers a batch of electric scooters in one transaction
     */
    public void registerElectricScooters(List<Float> aerodynamicCoefficient, List<Float> frontalArea,
                                         List<Integer> weight,
                                         List<ElectricScooterType> type, List<String> description,
                                         List<Float> maxBatteryCapacity,
                                         List<Integer> actualBatteryCapacity,
                                         List<Integer> enginePower, List<Double> parkLatitude, List<Double> parkLongitude)
                                        throws SQLException, MessagingException {
        try {
            for (int i = 0; i < aerodynamicCoefficient.size(); i++) {
                registerEletricScooterNoCommit(aerodynamicCoefficient.get(i), frontalArea.get(i), weight.get(i),
                        type.get(i), description.get(i), maxBatteryCapacity.get(i),
                        actualBatteryCapacity.get(i), enginePower.get(i), parkLatitude.get(i), parkLongitude.get(i));
            }
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {
                dataHandler.rollbackTransaction();
            } catch (SQLException e2) {
            }
            throw new SQLException("Failed to batch register electric scooters.", e.getSQLState(), e.getErrorCode());
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("All the parameter lists must have the same size.");
        }
    }

    /**
     * Fetch vehicle by vehicle specs
     * @param isBicycle true = bicycle, false = scooter
     * @param vehicleSpecs specs of the vehicle
     * @return the vehicle from the database
     * @throws SQLException in case of an sql exception
     */
    public Vehicle fetchVehicleBySpecs(Boolean isBicycle, String vehicleSpecs) throws SQLException {
        Vehicle vehicle = null;
        PreparedStatement stm2=null;
        PreparedStatement stm1=null;
        int wheelSize;
        try {
            if (isBicycle) {
                wheelSize = Integer.parseInt(vehicleSpecs);
                stm2 = dataHandler.prepareStatement("select * from BICYCLES where BICYCLE_SIZE = ?");
                stm2.setInt(1,wheelSize);
            }else{
                if(vehicleSpecs.equalsIgnoreCase("city")){
                    stm2 = dataHandler.prepareStatement("select * from ELECTRIC_SCOOTERS where lower(ELECTRIC_SCOOTER_TYPE_NAME) like 'urban' fetch first row only");
                }else if(vehicleSpecs.equalsIgnoreCase("off-road")){
                    stm2 = dataHandler.prepareStatement("select * from ELECTRIC_SCOOTERS where lower(ELECTRIC_SCOOTER_TYPE_NAME) like 'offroad' fetch first row only");
                }else{
                    return null;
                }
            }
        }catch(NumberFormatException e){
            return null;
        }
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            autoCloseableManager.addAutoCloseable(stm2);
            ResultSet rs2 = dataHandler.executeQuery(stm2);
            autoCloseableManager.addAutoCloseable(rs2);
            if (!rs2.next())
                return null;

            stm1 = dataHandler.prepareStatement("select * from VEHICLES where "+DESCRIPTION_FIELD_NAME+" like ?");
            stm1.setString(1,rs2.getString("vehicle_description"));


            autoCloseableManager.addAutoCloseable(stm1);
            ResultSet rs = dataHandler.executeQuery(stm1);
            autoCloseableManager.addAutoCloseable(rs);
            if (!rs.next())
                return null;

            if(isBicycle){
                vehicle = new Bicycle(rs.getInt(UNIQUE_NUMBER_FIELD_NAME), rs.getString(DESCRIPTION_FIELD_NAME),
                        rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                        rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME),
                        rs.getBoolean(AVAILABLE_FIELD_NAME), rs2.getInt(BICYCLE_SIZE_FIELD_NAME));
            }else{
                vehicle = new ElectricScooter(rs.getInt(UNIQUE_NUMBER_FIELD_NAME), rs.getString(DESCRIPTION_FIELD_NAME),
                        rs.getFloat(AERO_COEFFICIENT_FIELD_NAME),
                        rs.getFloat(FRONTAL_AREA_FIELD_NAME), rs.getInt(WEIGHT_FIELD_NAME),
                        rs.getBoolean(AVAILABLE_FIELD_NAME), ElectricScooterType.parseScooterType(
                        rs2.getString(ESCOOTER_TYPE_FIELD_NAME)),
                        rs2.getInt(ESCOOTER_ACTUAL_BATTERY_CAPACITY_FIELD_NAME), rs2.getFloat(ESCOOTER_MAX_BATTERY_CAPACITY_FIELD_NAME),
                        rs2.getInt(ESCOOTER_ENGINE_POWER_FIELD_NAME));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch vehicle from the database.", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return vehicle;
    }
}
