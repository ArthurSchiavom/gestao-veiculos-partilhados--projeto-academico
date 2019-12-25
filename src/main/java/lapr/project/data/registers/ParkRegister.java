/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data.registers;

import java.sql.*;
import java.util.*;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.Coordinates;
import lapr.project.model.point.of.interest.park.Capacity;
import lapr.project.model.point.of.interest.park.Park;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;

/**
 * This class registers and manipulates the data of all parks in data base
 */
public class ParkRegister {
    private final DataHandler dataHandler;

    public ParkRegister(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     *
     * @param id
     * @param description
     * @param coord
     * @param parkInputVoltage
     * @param parkInputCurrent
     * @param maxEletricScooters
     * @param maxBicycles
     * @return number of lines affected
     * @throws SQLException
     */
    private void addParkNoCommit(String id, String description, Coordinates coord, float parkInputVoltage, float parkInputCurrent, int maxEletricScooters, int maxBicycles) throws SQLException {
        try (CallableStatement cs = dataHandler.prepareCall("{call register_park(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) { // ensure everything is done with one request to the database => ensure database information consistency
            cs.setString(1, id);
            cs.setDouble(2, coord.getLatitude());
            cs.setDouble(3, coord.getLongitude());
            cs.setInt(4, coord.getAltitude());
            cs.setString(5, description);
            cs.setFloat(6, parkInputVoltage);
            cs.setFloat(7, parkInputCurrent);
            cs.setInt(8, maxEletricScooters);
            cs.setInt(9, maxBicycles);
            dataHandler.execute(cs);
        } catch (SQLException e) {
            throw new SQLException("Failed to add park (no commit).");
        }
    }

    public void registerParks(List<String> id, List<String> description, List<Coordinates> coord, List<Float> parkInputVoltage, List<Float> parkInputCurrent, List<Integer> maxEletricScooters, List<Integer> maxBicycles) throws SQLException {
        if (!(id.size() == description.size() && description.size() == coord.size() && coord.size() == parkInputVoltage.size() && parkInputVoltage.size() == parkInputCurrent.size())) {
            throw new IllegalArgumentException("Lists have different sizes.");
        }

        try {
            for (int i = 0; i < id.size(); i++) {
                addParkNoCommit(id.get(i), description.get(i), coord.get(i), parkInputVoltage.get(i), parkInputCurrent.get(i), maxEletricScooters.get(i), maxBicycles.get(i));
            }
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to batch register parks.", e.getSQLState(), e.getErrorCode());
        }
    }

    /**
     * Removes a park from database
     *
     * @param id a given id
     * @return return true if successfully removed and false otherwise
     */
    public void setAvailability(String id, boolean newStatus) throws SQLException {
        try (PreparedStatement ps = dataHandler.prepareStatement("UPDATE parks SET available = ? WHERE park_id = ?")) {
            int newStatusInt = newStatus ? 1 : 0;
            ps.setInt(1, newStatusInt);
            ps.setString(2, id);
            dataHandler.executeUpdate(ps);
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to change park's availability", e.getSQLState(), e.getErrorCode());
        }
    }

    /**
     * Fetch a parkk by id
     *
     * @param id park id
     * @return return a park
     */
    public Park fetchParkById(String id) throws SQLException {
        Coordinates coord;
        double latitude;
        double longitude;
        int altitude;
        float parkInputVoltage;
        float parkInputCorrent;
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement ps = dataHandler.prepareStatement("Select * from parks p, points_of_interest poi where park_id=? AND p.latitude = poi.latitude AND p.longitude = poi.longitude");
            closeableManager.addAutoCloseable(ps);
            ps.setString(1, id);
            ResultSet rs = dataHandler.executeQuery(ps);
            closeableManager.addAutoCloseable(rs);
            if (rs == null || !rs.next()) {
                return null;
            }
            latitude = rs.getDouble("latitude");
            longitude = rs.getDouble("longitude");
            altitude = rs.getInt("altitude_m");
            coord = new Coordinates(latitude, longitude, altitude);
            parkInputVoltage = rs.getFloat("park_input_voltage");
            parkInputCorrent = rs.getFloat("park_input_current");
            return new Park(id, parkInputVoltage, parkInputCorrent, getListOfCapacities(id), rs.getString("poi_description"), coord);
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch park by id.", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    public Park fetchParkByCoordinates(double lat, double lon) throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement ps = dataHandler.prepareStatement("select * from PARKS p, points_of_interest poi where p.LATITUDE = ? AND p.LONGITUDE = ? " +
                    "AND p.latitude = poi.latitude AND p.longitude = poi.longitude");
            ps.setDouble(1, lat);
            ps.setDouble(2, lon);
            closeableManager.addAutoCloseable(ps);
            ResultSet rs = dataHandler.executeQuery(ps);
            closeableManager.addAutoCloseable(rs);

            rs.next();
            String parkId = rs.getString("park_id");
            return new Park(parkId, rs.getFloat("park_input_voltage"), rs.getFloat("park_input_current"),
                    getListOfCapacities(parkId), rs.getString("poi_description"), new Coordinates(lat, lon, rs.getInt("altitude_m")));
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch park by coordinates.", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    /**
     * @param parkId park id
     * @return return a list of capacitys (of different types)
     */
    private List<Capacity> getListOfCapacities(String parkId) throws SQLException {
        VehicleType vehicleType;
        List<Capacity> capacity = new ArrayList<>();
        int parkCapacity;
        int amountOccupied;
        ResultSet rs = null;
        PreparedStatement ps = null;
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            ps = dataHandler.prepareStatement("Select * from park_capacity where park_id=?");
            closeableManager.addAutoCloseable(ps);
            ps.setString(1, parkId);
            rs = dataHandler.executeQuery(ps);
            closeableManager.addAutoCloseable(rs);
            if (rs == null) {
                return null;
            }
            while (rs.next()) {
                String vehicleTypeName = rs.getString("vehicle_type_name");
                parkCapacity = rs.getInt("park_capacity");
                amountOccupied = rs.getInt("amount_occupied");
                vehicleType = VehicleType.parseVehicleType(vehicleTypeName);
                capacity.add(new Capacity(parkCapacity, amountOccupied, vehicleType));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to get park capacities.", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
        return capacity;
    }

    public void updateParkId(String currentId, String newId) throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement psParks;
            PreparedStatement psParkCapacity;
            PreparedStatement psTripsStart;
            PreparedStatement psTripsEnd;
            PreparedStatement psParkVehicle;

            psParks = dataHandler.prepareStatement("UPDATE parks SET park_id = ? where park_id = ?");
            closeableManager.addAutoCloseable(psParks);
            psParks.setString(1, newId);
            psParks.setString(2, currentId);
            dataHandler.executeUpdate(psParks);

            psParkCapacity = dataHandler.prepareStatement("UPDATE park_capacity SET park_id = ? where park_id = ?");
            closeableManager.addAutoCloseable(psParkCapacity);
            psParkCapacity.setString(1, newId);
            psParkCapacity.setString(2, currentId);
            dataHandler.executeUpdate(psParkCapacity);

            psTripsStart = dataHandler.prepareStatement("UPDATE trips SET start_park_id = ? where start_park_id = ?");
            closeableManager.addAutoCloseable(psTripsStart);
            psTripsStart.setString(1, newId);
            psTripsStart.setString(2, currentId);
            dataHandler.executeUpdate(psTripsStart);

            psTripsEnd = dataHandler.prepareStatement("UPDATE trips SET end_park_id = ? where end_park_id = ?");
            closeableManager.addAutoCloseable(psTripsEnd);
            psTripsEnd.setString(1, newId);
            psTripsEnd.setString(2, currentId);
            dataHandler.executeUpdate(psTripsEnd);

            psParkVehicle = dataHandler.prepareStatement("UPDATE park_vehicle SET park_id = ? where park_id = ?");
            closeableManager.addAutoCloseable(psParkVehicle);
            psParkVehicle.setString(1, newId);
            psParkVehicle.setString(2, currentId);
            dataHandler.executeUpdate(psParkVehicle);

            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to update park's ID", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    public void updateParkDescription(String parkId, String newDescription) throws SQLException {
        double longitude;
        double latitude;
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement getLatLonPS = dataHandler.prepareStatement("SELECT latitude, longitude from parks where park_id = ?");
            closeableManager.addAutoCloseable(getLatLonPS);
            getLatLonPS.setString(1, parkId);
            ResultSet latLonRS = dataHandler.executeQuery(getLatLonPS);
            if (!latLonRS.next())
                throw new SQLException("No such park");
            closeableManager.addAutoCloseable(latLonRS);
            latitude = latLonRS.getDouble("latitude");
            longitude = latLonRS.getDouble("longitude");

            PreparedStatement updateDescriptionPS = dataHandler.prepareStatement("UPDATE points_of_interest SET poi_description = ? WHERE latitude = ? AND longitude = ?");
            closeableManager.addAutoCloseable(updateDescriptionPS);
            updateDescriptionPS.setString(1, newDescription);
            updateDescriptionPS.setDouble(2, latitude);
            updateDescriptionPS.setDouble(3, longitude);
            dataHandler.executeUpdate(updateDescriptionPS);

            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to update park's description");
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    public void updateParkInputVoltage(String parkId, float newInputVoltage) throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement ps = dataHandler.prepareStatement("UPDATE parks SET park_input_voltage = ? where park_id = ?");
            closeableManager.addAutoCloseable(ps);
            ps.setFloat(1, newInputVoltage);
            ps.setString(2, parkId);
            dataHandler.executeUpdate(ps);
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to update park's description");
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    public void updateParkInputCurrent(String parkId, float newInputCurrent) throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement ps = dataHandler.prepareStatement("UPDATE parks SET park_input_current = ? where park_id = ?");
            closeableManager.addAutoCloseable(ps);
            ps.setFloat(1, newInputCurrent);
            ps.setString(2, parkId);
            dataHandler.executeUpdate(ps);
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to update park's input current", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    public void updateParkCapacity(String parkId, VehicleType vehicleType, int newCapacity) throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement ps = dataHandler.prepareStatement("UPDATE park_capacity SET park_capacity = ? where park_id = ? AND vehicle_type_name = ?");
            closeableManager.addAutoCloseable(ps);
            ps.setInt(1, newCapacity);
            ps.setString(2, parkId);
            ps.setString(3, vehicleType.getSQLName());
            dataHandler.executeUpdate(ps);
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to update park's capacity", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    /**
     * Loads a list of parks that exist in sql table
     *
     * @return list of all parks that exist in sql table 'Parks'
     */
    private List<Park> fetchAllParks() throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        List<Park> parkList = new ArrayList<>();
        double latitude;
        double longitude;
        int altitude;
        Coordinates coord;
        float parkInputVoltage;
        float parkInputCorrent;
        String parkId;
        try {
            PreparedStatement stm = dataHandler.prepareStatement("Select p.park_id, p.latitude, p.longitude, p.park_input_voltage, p.park_input_current, p.available, poi.altitude_m, poi.poi_description from parks p, points_of_interest poi WHERE p.latitude = poi.latitude AND p.longitude = poi.longitude");
            closeableManager.addAutoCloseable(stm);
            ResultSet rs = dataHandler.executeQuery(stm);
            closeableManager.addAutoCloseable(rs);

            while (rs.next()) {
                parkId = rs.getString("park_id");
                latitude = rs.getDouble("latitude");
                longitude = rs.getDouble("longitude");
                altitude = rs.getInt("altitude_m");
                coord = new Coordinates(latitude, longitude, altitude);
                parkInputVoltage = rs.getFloat("park_input_voltage");
                parkInputCorrent = rs.getFloat("park_input_current");
                parkList.add(new Park(parkId, parkInputVoltage, parkInputCorrent, getListOfCapacities(parkId), rs.getString("poi_description"), coord));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch all parks", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
        return parkList;
    }

    /**
     * Gets the nearest parks from a certain coordinate and returns their availability
     *
     * <h1></h1>We are running through every park in the sql table instead of parsing it to a tree because, it has a smaller complexity initially
     * and if we were to need the tree again, it could be outdated by then, so we would have to again load the tree with all the information
     * in the table, making it higher complexity than simply iterating all of them</h1>
     *
     * @param coords
     * @param radius
     * @return hashmap containing parks and their corresponding capacities
     */
    public HashMap<Park, List<Capacity>> getNearestParksAndAvailability(Coordinates coords, double radius) throws SQLException {
        HashMap<Park, List<Capacity>> nearestParksAvailability = new HashMap<>();
        for (Park park : fetchAllParks()) {
            if (coords.distance(park.getCoordinates()) <= radius) {
                nearestParksAvailability.put(park, getListOfCapacities(park.getId()));
            }
        }
        return nearestParksAvailability;
    }

    /**
     * Return True if returned the vehicle to the park with sucess, false otherwise
     *
     * @param parkId    Park id
     * @param vehicleDescription Vehicle id
     * @return
     */
    public void putVehicleInPark(String parkId, String vehicleDescription) throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        try {
            PreparedStatement ps = dataHandler.prepareStatement("Insert into park_vehicle(park_id, vehicle_description) values (?,?)");
            closeableManager.addAutoCloseable(ps);
            ps.setString(1, parkId);
            ps.setString(2, vehicleDescription);
            ps.executeUpdate();
            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to return vehicle to park", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }
    }

    public <T> List<T> fetchVehiclesAtPark(String parkId, Class<T> vehicleClassType) throws SQLException {
        AutoCloseableManager closeableManager = new AutoCloseableManager();
        List<T> result = new ArrayList<>();

        try {
            PreparedStatement ps = dataHandler.prepareStatement("Select * from PARK_VEHICLE where PARK_ID = ?");
            ps.setString(1, parkId);
            closeableManager.addAutoCloseable(ps);
            ResultSet rs = dataHandler.executeQuery(ps);
            closeableManager.addAutoCloseable(rs);

            VehicleRegister vehicleRegister = Company.getInstance().getVehicleRegister();
            while (rs.next()) {
                Vehicle vehicle = vehicleRegister.fetchVehicle(rs.getString("vehicle_description"));
                if (vehicle.getClass() == vehicleClassType) {
                    result.add(vehicleClassType.cast(vehicle));
                }
            }

            dataHandler.commitTransaction();
        } catch (SQLException e) {
            try {dataHandler.rollbackTransaction(); } catch (SQLException e2) {};
            throw new SQLException("Failed to fetch vehicles at park", e.getSQLState(), e.getErrorCode());
        } finally {
            closeableManager.closeAutoCloseables();
        }

        return result;
    }
}
