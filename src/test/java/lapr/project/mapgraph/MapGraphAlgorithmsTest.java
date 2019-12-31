package lapr.project.mapgraph;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import lapr.project.mapgraph.MapGraphAlgorithms;
import lapr.project.mapgraph.Graph;

/**
 *
 * @author DEI-ISEP
 */
public class MapGraphAlgorithmsTest {

    Graph<String,String> completeMap = new Graph<>(false);
    Graph<String,String> incompleteMap = new Graph<>(false);

    @BeforeEach
    void beforeEach() {
        completeMap.insertVertex("Porto");
        completeMap.insertVertex("Braga");
        completeMap.insertVertex("Vila Real");
        completeMap.insertVertex("Aveiro");
        completeMap.insertVertex("Coimbra");
        completeMap.insertVertex("Leiria");

        completeMap.insertVertex("Viseu");
        completeMap.insertVertex("Guarda");
        completeMap.insertVertex("Castelo Branco");
        completeMap.insertVertex("Lisboa");
        completeMap.insertVertex("Faro");

        completeMap.insertEdge("Porto","Aveiro","A1",75);
        completeMap.insertEdge("Porto","Braga","A3",60);
        completeMap.insertEdge("Porto","Vila Real","A4",100);
        completeMap.insertEdge("Viseu","Guarda","A25",75);
        completeMap.insertEdge("Guarda","Castelo Branco","A23",100);
        completeMap.insertEdge("Aveiro","Coimbra","A1",60);
        completeMap.insertEdge("Coimbra","Lisboa","A1",200);
        completeMap.insertEdge("Coimbra","Leiria","A34",80);
        completeMap.insertEdge("Aveiro","Leiria","A17",120);
        completeMap.insertEdge("Leiria","Lisboa","A8",150);

        completeMap.insertEdge("Aveiro","Viseu","A25",85);
        completeMap.insertEdge("Leiria","Castelo Branco","A23",170);
        completeMap.insertEdge("Lisboa","Faro","A2",280);

        incompleteMap = completeMap.clone();

        incompleteMap.removeEdge("Aveiro","Viseu");
        incompleteMap.removeEdge("Leiria","Castelo Branco");
        incompleteMap.removeEdge("Lisboa","Faro");
    }


    /**
     * Test of BreadthFirstSearch method, of class MapGraphAlgorithms.
     */
    @Test
    public void testBreadthFirstSearch() {
        System.out.println("Test BreadthFirstSearch");

        assertEquals(true, MapGraphAlgorithms.BreadthFirstSearch(completeMap, "LX")==null);

        LinkedList<String> path = MapGraphAlgorithms.BreadthFirstSearch(incompleteMap, "Faro");

        assertEquals(true, path.size()==1);

        Iterator<String> it = path.iterator();
        assertEquals(true, it.next().compareTo("Faro")==0);

        path = MapGraphAlgorithms.BreadthFirstSearch(incompleteMap, "Porto");
        assertEquals(true, path.size()==7);

        path = MapGraphAlgorithms.BreadthFirstSearch(incompleteMap, "Viseu");
        assertEquals(true, path.size()==3);
    }

    /**
     * Test of DepthFirstSearch method, of class MapGraphAlgorithms.
     */
    @Test
    public void testDepthFirstSearch() {
        System.out.println("Test of DepthFirstSearch");

        LinkedList<String> path;

        assertEquals(true, MapGraphAlgorithms.DepthFirstSearch(completeMap, "LX")==null);

        path = MapGraphAlgorithms.DepthFirstSearch(incompleteMap, "Faro");
        assertEquals(true, path.size()==1);

        Iterator<String> it = path.iterator();
        assertEquals(true, it.next().compareTo("Faro")==0);

        path = MapGraphAlgorithms.DepthFirstSearch(incompleteMap, "Porto");
        assertEquals(true, path.size()==7);

        path = MapGraphAlgorithms.DepthFirstSearch(incompleteMap, "Viseu");
        assertEquals(true, path.size()==3);

        it = path.iterator();
        assertEquals(true, it.next().compareTo("Viseu")==0);
        assertEquals(true, it.next().compareTo("Guarda")==0);
        assertEquals(true, it.next().compareTo("Castelo Branco")==0);
    }

    /**
     * Test of allPaths method, of class MapGraphAlgorithms.
     */
    @Test
    public void testAllPaths() {
        System.out.println("Test of all paths");

        ArrayList<LinkedList<String>> paths = new ArrayList<LinkedList<String>>();

        paths=MapGraphAlgorithms.allPaths(completeMap, "Porto", "LX");
        assertEquals(true,paths.size()==0);

        paths = MapGraphAlgorithms.allPaths(incompleteMap, "Porto", "Lisboa");
        assertEquals(true, paths.size()==4);

        paths=MapGraphAlgorithms.allPaths(incompleteMap, "Porto", "Faro");
        assertEquals(true, paths.size()==0);
    }

    /**
     * Test of shortestPath method, of class MapGraphAlgorithms.
     */
    @Test
    public void testShortestPath() {
        System.out.println("Test of shortest path");

        LinkedList<String> shortPath = new LinkedList<String>();
        double lenpath=0;
        lenpath=MapGraphAlgorithms.shortestPath(completeMap,"Porto","LX",shortPath);
        assertEquals(true, lenpath == 0);

        lenpath=MapGraphAlgorithms.shortestPath(incompleteMap,"Porto","Faro",shortPath);
        assertEquals(true, lenpath == 0);

        lenpath=MapGraphAlgorithms.shortestPath(completeMap,"Porto","Porto",shortPath);
        assertEquals(true, shortPath.size() == 1);

        lenpath=MapGraphAlgorithms.shortestPath(incompleteMap,"Porto","Lisboa",shortPath);
        assertEquals(true, lenpath == 335);

        Iterator<String> it = shortPath.iterator();

        assertEquals(true, it.next().compareTo("Porto")==0);
        assertEquals(true, it.next().compareTo("Aveiro")==0);
        assertEquals(true, it.next().compareTo("Coimbra")==0);
        assertEquals(true, it.next().compareTo("Lisboa")==0);

        lenpath=MapGraphAlgorithms.shortestPath(incompleteMap,"Braga","Leiria",shortPath);
        assertEquals(true, lenpath == 255);

        it = shortPath.iterator();

        assertEquals(true, it.next().compareTo("Braga")==0);
        assertEquals(true, it.next().compareTo("Porto")==0);
        assertEquals(true, it.next().compareTo("Aveiro")==0);
        assertEquals(true, it.next().compareTo("Leiria")==0);

        shortPath.clear();
        lenpath=MapGraphAlgorithms.shortestPath(completeMap,"Porto","Castelo Branco",shortPath);
        assertEquals(true, lenpath == 335);
        assertEquals(true, shortPath.size() == 5);

        it = shortPath.iterator();

        assertEquals(true, it.next().compareTo("Porto")==0);
        assertEquals(true, it.next().compareTo("Aveiro")==0);
        assertEquals(true, it.next().compareTo("Viseu")==0);
        assertEquals(true, it.next().compareTo("Guarda")==0);
        assertEquals(true, it.next().compareTo("Castelo Branco")==0);

        //Changing Edge: Aveiro-Viseu with Edge: Leiria-C.Branco 
        //should change shortest path between Porto and Castelo Branco

        completeMap.removeEdge("Aveiro", "Viseu");
        completeMap.insertEdge("Leiria","Castelo Branco","A23",170);
        shortPath.clear();
        lenpath=MapGraphAlgorithms.shortestPath(completeMap,"Porto","Castelo Branco",shortPath);
        assertEquals(true, lenpath == 365);
        assertEquals(true, shortPath.size() == 4);

        it = shortPath.iterator();

        assertEquals(true, it.next().compareTo("Porto")==0);
        assertEquals(true, it.next().compareTo("Aveiro")==0);
        assertEquals(true, it.next().compareTo("Leiria")==0);
        assertEquals(true, it.next().compareTo("Castelo Branco")==0);

    }

    /**
     * Test of shortestPaths method, of class MapGraphAlgorithms.
     */
    @Test
    public void testShortestPaths() {
        System.out.println("Test of shortest path");

        ArrayList <LinkedList<String>> paths = new ArrayList<>();
        ArrayList <Double> dists = new ArrayList<>();

        MapGraphAlgorithms.shortestPaths(completeMap,"Porto",paths,dists);

        assertEquals( paths.size(), dists.size(),"There should be as many paths as sizes");
        assertEquals(completeMap.numVertices(), paths.size());
        assertEquals(1, paths.get(completeMap.getKey("Porto")).size());
        assertEquals( Arrays.asList("Porto","Aveiro","Coimbra","Lisboa"), paths.get(completeMap.getKey("Lisboa")));
        assertEquals( Arrays.asList("Porto","Aveiro","Viseu","Guarda","Castelo Branco"), paths.get(completeMap.getKey("Castelo Branco")));
        assertEquals( 335, dists.get(completeMap.getKey("Castelo Branco")),0.01);

        //Changing Edge: Aveiro-Viseu with Edge: Leiria-C.Branco 
        //should change shortest path between Porto and Castelo Branco        
        completeMap.removeEdge("Aveiro", "Viseu");
        completeMap.insertEdge("Leiria","Castelo Branco","A23",170);
        MapGraphAlgorithms.shortestPaths(completeMap,"Porto",paths,dists);
        assertEquals( 365, dists.get(completeMap.getKey("Castelo Branco")),0.01);
        assertEquals(Arrays.asList("Porto","Aveiro","Leiria","Castelo Branco"), paths.get(completeMap.getKey("Castelo Branco")));



        MapGraphAlgorithms.shortestPaths(incompleteMap,"Porto",paths,dists);
        assertEquals(Double.MAX_VALUE,dists.get(completeMap.getKey("Faro")),0.01);
        assertEquals( 335, dists.get(completeMap.getKey("Lisboa")),0.01);
        assertEquals( Arrays.asList("Porto","Aveiro","Coimbra","Lisboa"), paths.get(completeMap.getKey("Lisboa")));
        assertEquals( 335, dists.get(completeMap.getKey("Lisboa")),0.01);

        MapGraphAlgorithms.shortestPaths(incompleteMap,"Braga",paths,dists);
        assertEquals( 255, dists.get(completeMap.getKey("Leiria")),0.01);
    }

    @Test
    public void shortestPathWithConstraintGreedy() {
        System.out.println("Test of shortest path with constraints (greedy method)");

        LinkedList<String> shortPath = new LinkedList<>();
        LinkedList<String> constraints = new LinkedList<>();
        constraints.add("Coimbra");
        constraints.add("Leiria");
        double dist = MapGraphAlgorithms.shortestPathWithConstraintsGreedy(completeMap, "Porto", "Lisboa", constraints, shortPath);
        assertEquals( 365, dist, 0);
    }

    @Test
    public void shortestPathWithConstraint() {
        System.out.println("Test of shortest path with constraints");

        LinkedList<String> shortPath = new LinkedList<>();
        Set<String> constraints = new HashSet<>();
        constraints.add("Coimbra");
        constraints.add("Leiria");
        double dist = MapGraphAlgorithms.shortestPathWithConstraints(completeMap, "Porto", "Lisboa", constraints, shortPath);
        assertEquals( 365, dist, 0);
    }
}

