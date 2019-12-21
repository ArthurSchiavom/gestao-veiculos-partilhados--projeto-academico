package lapr.project.data.registers;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lapr.project.data.DataHandler;

/**
 * Represents a company
 */
public class Company {
    private static Company instance = null;
    private final DataHandler dataHandler;
    private final ParkRegister parkRegister;
    private final UsersRegister usersRegister;
    private final TripRegister tripRegister;
    private final VehicleRegister vehicleRegister;
    private final PathRegister pathRegister;
    private final PoiRegister poiRegister;

    /**
     * Represents a Singleton of the class Company
     */
    private Company(DataHandler dataHandler) {
        instance = this;
        this.dataHandler = dataHandler;

        this.parkRegister =new ParkRegister(dataHandler);
        this.usersRegister = new UsersRegister(dataHandler);
        this.tripRegister = new TripRegister(dataHandler);
        this.vehicleRegister = new VehicleRegister(dataHandler);
        this.pathRegister = new PathRegister(dataHandler);
        this.poiRegister = new PoiRegister(dataHandler);
    }

    /**
     * Represents a Singleton of the class Company
     * @return the company
     */
    public static Company createCompany(DataHandler dataHandler){
        if(instance == null){
            new Company(dataHandler);
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public static Company getInstance() {
        return instance;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public ParkRegister getParkRegister() {
        return parkRegister;
    }

    public UsersRegister getUsersRegister() {
        return usersRegister;
    }

    public TripRegister getTripRegister() {
        return tripRegister;
    }

    public VehicleRegister getVehicleRegister() {
        return vehicleRegister;
    }
    
    public PathRegister getPathRegister() {
        return pathRegister;
    }
    
    public PoiRegister getPoiRegister() {
        return poiRegister;
    }
}
