package lapr.project.model.register;

import lapr.project.data.DataHandler;
import lapr.project.model.Company;
import lapr.project.model.vehicles.ScooterType;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.model.vehicles.VehicleType;

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
            if (rs.getInt(3) == 1)
                available = true;
            else
                available = false;

            vehicle = new Vehicle(vehicleId, rs.getDouble( 4), rs.getDouble(5), rs.getInt(6), rs.getFloat(8), rs.getFloat(9), rs.getInt(7), available, VehicleType.parseVehicleType(rs.getString(2)));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.getErrorCode();
        }

        return vehicle;
    }

//    public boolean addBicycle(double latitude, double longitude, int altitude, float aerodynamic_coefficient, float frontal_area, int weight, boolean available, VehicleType type, int size, String description) {
//        try {
//            "with prod as(    \n" +
//                    "    INSERT INTO public.product(product_name,color,listprice)\n" +
//                    "    VALUES('3D printer','green',560)\n" +
//                    "    RETURNING productid\n" +
//                    "    ),\n" +
//                    " pers as (\n" +
//                    "    INSERT INTO public.salesperson(territoryid,salesquota,bonus)\n" +
//                    "    VALUES(56,5000,100)\n" +
//                    "    RETURNING salespersonid\n" +
//                    "    )\n" +
//                    "INSERT INTO public.salesorderheader (salespersonid,productid,orderdate,shipdate,status)\n" +
//                    "SELECT   prod.productid,\n" +
//                    "         pers.salespersonid,\n" +
//                    "         now() as orderdate,\n" +
//                    "         now() - INTERVAL '7 day' as shipdate,\n" +
//                    "         1 as status\n" +
//                    "FROM prod,pers"
//            PreparedStatement ps = dh.prepareStatement(
//                    "with vehicle as(" +
//                                "INSERT INTO vehicles(vehicle_type_name, available, latitude, longitude," +
//                                " altitude_m, weight, aerodynamic_coefficient, frontal_area) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING vehicle_id" +
//                                ")" +
//                            "INSERT INTO bicycles VALUES (?, ?, ?)");
//            dh.setString(ps, 1, type.name().toLowerCase());
//            int availableInt;
//            if (available)
//                availableInt = 1;
//            else
//                availableInt = 0;
//            dh.setInt(ps, 2, availableInt);
//            dh.setDouble(ps, 4, latitude);
//            dh.setDouble(ps, 5, longitude);
//            dh.setInt(ps, 6, altitude);
//            dh.setInt(ps, 7, weight);
//            dh.setFloat(ps, 8, aerodynamic_coefficient);
//            dh.setFloat(ps, 9, frontal_area);
//            int changes = dh.executeUpdate(ps);
//            if (changes == 0)
//                return false;
//            return true;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return false;
//        }
//    }

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
