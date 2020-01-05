package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.mapgraph.Graph;
import lapr.project.model.Path;
import lapr.project.model.point.of.interest.PointOfInterest;
import lapr.project.model.users.Client;
import lapr.project.model.users.User;
import lapr.project.model.vehicles.Vehicle;
import lapr.project.utils.physics.calculations.PhysicsMethods;

import java.util.ArrayList;
import java.util.List;

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
    private Graph<PointOfInterest, Path> mapGraphDistance;
    private Graph<PointOfInterest, Path> mapGraphEnergy;
    private final PathAPI pathAPI;
    private final PoiAPI poiAPI;
    private final InvoiceAPI invoiceAPI;

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
        mapGraphDistance = null;
        mapGraphEnergy = null;
        this.pathAPI = new PathAPI(dataHandler);
        this.poiAPI = new PoiAPI(dataHandler);
        this.invoiceAPI = new InvoiceAPI(dataHandler);
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

    /**
     * Initializes a graph with the information about poi's and path's
     * @return a map graph with weight being the distance between poi's
     */
    public Graph<PointOfInterest,Path> initializeDistanceGraph(){
        mapGraphDistance = new Graph<>(true);
        for(PointOfInterest poi : poiAPI.fetchAllPois()){
            mapGraphDistance.insertVertex(poi);
        }
        PointOfInterest startingPoint;
        PointOfInterest endingPoint;
        for(Path path : pathAPI.fetchAllPaths()){
            startingPoint = path.getStartingPoint();
            endingPoint = path.getEndingPoint();
            mapGraphDistance.insertEdge(startingPoint,endingPoint,path,startingPoint.getCoordinates().distance(endingPoint.getCoordinates()));
        }
        return mapGraphDistance;
    }

    /**
     * Initializes a graph with the information about poi's and path's
     * @return a map graph with the weight being the energy between poi's
     */
    public Graph<PointOfInterest,Path> initializeEnergyGraph(Client client, Vehicle vehicle){
        mapGraphEnergy = new Graph<>(true);
        for(PointOfInterest poi : poiAPI.fetchAllPois()){
            mapGraphEnergy.insertVertex(poi);
        }
        PointOfInterest startingPoint;
        PointOfInterest endingPoint;
        for(Path path : pathAPI.fetchAllPaths()){
            startingPoint = path.getStartingPoint();
            endingPoint = path.getEndingPoint();
            List<Path> trip = new ArrayList<>();
            trip.add(path);
            mapGraphEnergy.insertEdge(startingPoint,endingPoint,path, PhysicsMethods.predictEnergySpent(client,trip,vehicle));
        }
        return mapGraphEnergy;
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

    /**
     * @return Returns the map graph that contains the information about parks and poi's distance
     */
    public Graph<PointOfInterest,Path> getMapGraphDistance(){
        if(mapGraphDistance != null) {
            return mapGraphDistance;
        }else{
            return initializeDistanceGraph();
        }
    }

    /**
     * @return Returns the map graph that contains the information about parks and poi's energy
     */
    public Graph<PointOfInterest,Path> getMapGraphEnergy(Client client, Vehicle vehicle){
        if(mapGraphEnergy != null) {
            return mapGraphEnergy;
        }else{
            return initializeEnergyGraph(client, vehicle);
        }
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

    public InvoiceAPI getInvoiceAPI() {
        return invoiceAPI;
    }
}
