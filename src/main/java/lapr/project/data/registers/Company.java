package lapr.project.data.registers;

import lapr.project.data.DataHandler;

/**
 * Represents a company
 */
public class Company {
    private static Company instance = null;
    private final DataHandler dataHandler;
    private final ParkAPI parkAPI;
    private final UsersAPI usersAPI;
    private final TripRegister tripRegister;
    private final VehicleAPI vehicleAPI;
    private final PathRegister pathRegister;
    private final PoiRegister poiRegister;

    /**
     * Represents a Singleton of the class Company
     */
    private Company(DataHandler dataHandler) {
        instance = this;
        this.dataHandler = dataHandler;

        this.parkAPI =new ParkAPI(dataHandler);
        this.usersAPI = new UsersAPI(dataHandler);
        this.tripRegister = new TripRegister(dataHandler);
        this.vehicleAPI = new VehicleAPI(dataHandler);
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

    public ParkAPI getParkAPI() {
        return parkAPI;
    }

    public UsersAPI getUsersAPI() {
        return usersAPI;
    }

    public TripRegister getTripRegister() {
        return tripRegister;
    }

    public VehicleAPI getVehicleAPI() {
        return vehicleAPI;
    }
    
    public PathRegister getPathRegister() {
        return pathRegister;
    }
    
    public PoiRegister getPoiRegister() {
        return poiRegister;
    }
}
