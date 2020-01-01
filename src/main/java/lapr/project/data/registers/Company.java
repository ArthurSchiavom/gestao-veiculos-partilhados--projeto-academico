package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.mapgraph.Graph;
import lapr.project.model.Path;
import lapr.project.model.point.of.interest.PointOfInterest;

/**
 * Represents a company
 */
public class Company {
    private static Company instance = null;
    private final DataHandler dataHandler;
    private final ParkAPI parkAPI;
    private final UserAPI userAPI;
    private final TripAPI tripAPI;
    private final VehicleAPI vehicleAPI;
    private Graph<PointOfInterest, Path> mapGraph;
    private final PathAPI pathAPI;
    private final PoiAPI poiAPI;

    /**
     * Represents a Singleton of the class Company
     */
    private Company(DataHandler dataHandler) {
        instance = this;
        this.dataHandler = dataHandler;
        this.parkAPI =new ParkAPI(dataHandler);
        this.userAPI = new UserAPI(dataHandler);
        this.tripAPI = new TripAPI(dataHandler);
        this.vehicleAPI = new VehicleAPI(dataHandler);
        mapGraph = null;
        this.pathAPI = new PathAPI(dataHandler);
        this.poiAPI = new PoiAPI(dataHandler);
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

    public UserAPI getUserAPI() {
        return userAPI;
    }

    public TripAPI getTripAPI() {
        return tripAPI;
    }

    public VehicleAPI getVehicleAPI() {
        return vehicleAPI;
    }
    
    public PathAPI getPathAPI() {
        return pathAPI;
    }
    
    public PoiAPI getPoiAPI() {
        return poiAPI;
    }

    /**
     * Initializes a graph with the information about poi's and path's
     * @return a map graph
     */
    public Graph<PointOfInterest,Path> initializeGraph(){
        mapGraph = new Graph<>(true);
        for(PointOfInterest poi : poiAPI.fetchAllPois()){
            mapGraph.insertVertex(poi);
        }

//        for(Path path : pathRegister.fetchAllPaths()){
//            mapGraph.insertEdge(path.getStartingPoint(),path.getEndingPoint(),0);
//        }
        return null;
    }
}
