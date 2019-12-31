package lapr.project.mapgraph;

import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author DEI-ISEP
 */
public class GraphTest {

    Graph<String, String> instance = new Graph<>(true) ;

    public GraphTest() {
    }

    @BeforeEach
    void beforeEach() {

    }

    /**
     * Test of numVertices method, of class Graph.
     */
    @Test
    public void testNumVertices() {
        System.out.println("Test numVertices");

        assertEquals(true, (instance.numVertices()==0));

        instance.insertVertex("A");
        assertEquals(true, (instance.numVertices()==1));

        instance.insertVertex("B");
        assertEquals(true, (instance.numVertices()==2));

        instance.removeVertex("A");
        assertEquals(true, (instance.numVertices()==1));

        instance.removeVertex("B");
        assertEquals(true, (instance.numVertices()==0));
    }

    /**
     * Test of vertices method, of class Graph.
     */
    @Test
    public void testVertices() {
        System.out.println("Test vertices");

        Iterator<String> itVerts = instance.vertices().iterator();

        assertEquals(true, itVerts.hasNext()==false);

        instance.insertVertex("A");
        instance.insertVertex("B");

        itVerts = instance.vertices().iterator();

        assertEquals(true, (itVerts.next().compareTo("A")==0));
        assertEquals(true,(itVerts.next().compareTo("B")==0));

        instance.removeVertex("A");

        itVerts = instance.vertices().iterator();
        assertEquals(true,(itVerts.next().compareTo("B"))==0);

        instance.removeVertex("B");

        itVerts = instance.vertices().iterator();
        assertEquals(true,itVerts.hasNext()==false);
    }

    /**
     * Test of numEdges method, of class Graph.
     */
    @Test
    public void testNumEdges() {
        System.out.println("Test numEdges");

        assertEquals(true, (instance.numEdges()==0));

        instance.insertEdge("A","B","Edge1",6);
        assertEquals(true, (instance.numEdges()==1));

        instance.insertEdge("A","C","Edge2",1);
        assertEquals(true, (instance.numEdges()==2));

        instance.removeEdge("A","B");
        assertEquals(true, (instance.numEdges()==1));

        instance.removeEdge("A","C");
        assertEquals(true, (instance.numEdges()==0));
    }

    /**
     * Test of edges method, of class Graph.
     */
    @Test
    public void testEdges() {
        System.out.println("Test Edges");

        Iterator<Edge<String,String>> itEdge = instance.edges().iterator();

        assertEquals(true, (itEdge.hasNext()==false));

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        itEdge = instance.edges().iterator();

        itEdge.next(); itEdge.next();
        assertEquals(true, itEdge.next().getElement().equals("Edge3")==true);

        itEdge.next(); itEdge.next();
        assertEquals(true, itEdge.next().getElement().equals("Edge6")==true);

        instance.removeEdge("A","B");

        itEdge = instance.edges().iterator();
        assertEquals(true, itEdge.next().getElement().equals("Edge2")==true);

        instance.removeEdge("A","C"); instance.removeEdge("B","D");
        instance.removeEdge("C","D"); instance.removeEdge("C","E");
        instance.removeEdge("D","A"); instance.removeEdge("E","D");
        instance.removeEdge("E","E");
        itEdge = instance.edges().iterator();
        assertEquals(true, (itEdge.hasNext()==false));
    }

    /**
     * Test of getEdge method, of class Graph.
     */
    @Test
    public void testGetEdge() {
        System.out.println("Test getEdge");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        assertEquals(true, instance.getEdge("A","E")==null);

        assertEquals(true, instance.getEdge("B","D").getElement().equals("Edge3")==true);
        assertEquals(true, instance.getEdge("D","B")==null);

        instance.removeEdge("D","A");
        assertEquals(true, instance.getEdge("D","A")==null);

        assertEquals(true, instance.getEdge("E","E").getElement().equals("Edge8")==true);
    }

    /**
     * Test of endVertices method, of class Graph.
     */
    @Test
    public void testEndVertices() {
        System.out.println("Test endVertices");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        Edge<String,String> edge0 = new Edge<>();

        String[] vertices = new String[2];

        //assertEquals("endVertices should be null", instance.endVertices(edge0)==null);

        Edge<String,String> edge1 = instance.getEdge("A","B");
        //vertices = instance.endVertices(edge1);
        assertEquals(true, instance.endVertices(edge1)[0].equals("A"));
        assertEquals(true, instance.endVertices(edge1)[1].equals("B"));
    }

    /**
     * Test of opposite method, of class Graph.
     */
    @Test
    public void testOpposite() {
        System.out.println("Test opposite");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        Edge<String,String> edge5 = instance.getEdge("C","E");
        String vert = instance.opposite("A", edge5);
        assertEquals(true, vert==null);

        Edge<String,String> edge1 = instance.getEdge("A","B");
        vert = instance.opposite("A", edge1);
        assertEquals(true, vert.equals("B")==true);

        Edge<String,String> edge8 = instance.getEdge("E","E");
        vert = instance.opposite("E", edge8);
        assertEquals(true, vert.equals("E")==true);
    }

    /**
     * Test of outDegree method, of class Graph.
     */
    @Test
    public void testOutDegree() {
        System.out.println("Test outDegree");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        int outdeg = instance.outDegree("G");
        assertEquals(true, outdeg==-1);

        outdeg = instance.outDegree("A");
        assertEquals(true, outdeg==2);

        outdeg = instance.outDegree("B");
        assertEquals(true, outdeg==1);

        outdeg = instance.outDegree("E");
        assertEquals(true, outdeg==2);
    }

    /**
     * Test of inDegree method, of class Graph.
     */
    @Test
    public void testInDegree() {
        System.out.println("Test inDegree");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        int indeg = instance.inDegree("G");
        assertEquals(true, indeg==-1);

        indeg = instance.inDegree("A");
        assertEquals(true, indeg==1);

        indeg = instance.inDegree("D");
        assertEquals(true, indeg==3);

        indeg = instance.inDegree("E");
        assertEquals(true, indeg==2);
    }

    /**
     * Test of outgoingEdges method, of class Graph.
     */
    @Test
    public void testOutgoingEdges() {
        System.out.println(" Test outgoingEdges");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        Iterator<Edge<String,String>> itEdge = instance.outgoingEdges("C").iterator();
        Edge<String,String> first = itEdge.next();
        Edge<String,String> second = itEdge.next();
        assertEquals(true,
                ( (first.getElement().equals("Edge4")==true && second.getElement().equals("Edge5")==true) ||
                        (first.getElement().equals("Edge5")==true && second.getElement().equals("Edge4")==true) ));

        instance.removeEdge("E","E");

        itEdge = instance.outgoingEdges("E").iterator();
        assertEquals(true, (itEdge.next().getElement().equals("Edge7")==true));

        instance.removeEdge("E","D");

        itEdge = instance.outgoingEdges("E").iterator();
        assertEquals(true, (itEdge.hasNext()==false));
    }

    /**
     * Test of incomingEdges method, of class Graph.
     */
    @Test
    public void testIncomingEdges() {

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        Iterator<Edge<String,String>> itEdge = instance.incomingEdges("D").iterator();

        assertEquals(true, (itEdge.next().getElement().equals("Edge3")==true));
        assertEquals(true, (itEdge.next().getElement().equals("Edge4")==true));
        assertEquals(true, (itEdge.next().getElement().equals("Edge7")==true));

        itEdge = instance.incomingEdges("E").iterator();

        assertEquals(true, (itEdge.next().getElement().equals("Edge5")==true));
        assertEquals(true, (itEdge.next().getElement().equals("Edge8")==true));

        instance.removeEdge("E","E");

        itEdge = instance.incomingEdges("E").iterator();

        assertEquals(true, (itEdge.next().getElement().equals("Edge5")==true));

        instance.removeEdge("C","E");

        itEdge = instance.incomingEdges("E").iterator();
        assertEquals(true, (itEdge.hasNext()==false));
    }

    /**
     * Test of insertVertex method, of class Graph.
     */
    @Test
    public void testInsertVertex() {
        System.out.println("Test insertVertex");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        Iterator <String> itVert = instance.vertices().iterator();

        assertEquals(true, (itVert.next().equals("A")==true));
        assertEquals(true,(itVert.next().equals("B")==true));
        assertEquals(true, (itVert.next().equals("C")==true));
        assertEquals(true,(itVert.next().equals("D")==true));
        assertEquals(true, (itVert.next().equals("E")==true));
    }

    /**
     * Test of insertEdge method, of class Graph.
     */
    @Test
    public void testInsertEdge() {
        System.out.println("Test insertEdge");

        assertEquals(true, (instance.numEdges()==0));

        instance.insertEdge("A","B","Edge1",6);
        assertEquals(true, (instance.numEdges()==1));

        instance.insertEdge("A","C","Edge2",1);
        assertEquals(true, (instance.numEdges()==2));

        instance.insertEdge("B","D","Edge3",3);
        assertEquals(true, (instance.numEdges()==3));

        instance.insertEdge("C","D","Edge4",4);
        assertEquals(true, (instance.numEdges()==4));

        instance.insertEdge("C","E","Edge5",1);
        assertEquals(true, (instance.numEdges()==5));

        instance.insertEdge("D","A","Edge6",2);
        assertEquals(true, (instance.numEdges()==6));

        instance.insertEdge("E","D","Edge7",1);
        assertEquals(true, (instance.numEdges()==7));

        instance.insertEdge("E","E","Edge8",1);
        assertEquals(true, (instance.numEdges()==8));

        Iterator <Edge<String,String>> itEd = instance.edges().iterator();

        itEd.next(); itEd.next();
        assertEquals(true, (itEd.next().getElement().equals("Edge3")==true));
        itEd.next(); itEd.next();
        assertEquals(true,(itEd.next().getElement().equals("Edge6")==true));
    }

    /**
     * Test of removeVertex method, of class Graph.
     */
    @Test
    public void testRemoveVertex() {
        System.out.println("Test removeVertex");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.removeVertex("C");
        assertEquals(true, (instance.numVertices()==4));

        Iterator<String> itVert = instance.vertices().iterator();
        assertEquals(true, (itVert.next().equals("A")==true));
        assertEquals(true,(itVert.next().equals("B")==true));
        assertEquals(true, (itVert.next().equals("D")==true));
        assertEquals(true,(itVert.next().equals("E")==true));

        instance.removeVertex("A");
        assertEquals(true, (instance.numVertices()==3));

        itVert = instance.vertices().iterator();
        assertEquals(true, (itVert.next().equals("B")==true));
        assertEquals(true,(itVert.next().equals("D")==true));
        assertEquals(true, (itVert.next().equals("E")==true));

        instance.removeVertex("E");
        assertEquals(true, (instance.numVertices()==2));

        itVert = instance.vertices().iterator();

        assertEquals(true,itVert.next().equals("B")==true);
        assertEquals(true,itVert.next().equals("D")==true);

        instance.removeVertex("B"); instance.removeVertex("D");
        assertEquals(true, (instance.numVertices()==0));
    }

    /**
     * Test of removeEdge method, of class Graph.
     */
    @Test
    public void testRemoveEdge() {
        System.out.println("Test removeEdge");

        assertEquals(true, (instance.numEdges()==0));

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        assertEquals(true, (instance.numEdges()==8));

        instance.removeEdge("E","E");
        assertEquals(true, (instance.numEdges()==7));

        Iterator <Edge<String,String>> itEd = instance.edges().iterator();

        itEd.next(); itEd.next();
        assertEquals(true, (itEd.next().getElement().equals("Edge3")==true));
        itEd.next(); itEd.next();
        assertEquals(true, (itEd.next().getElement().equals("Edge6")==true));

        instance.removeEdge("C","D");
        assertEquals(true, (instance.numEdges()==6));

        itEd = instance.edges().iterator();
        itEd.next(); itEd.next();
        assertEquals(true, (itEd.next().getElement().equals("Edge3")==true));
        assertEquals(true, (itEd.next().getElement().equals("Edge5")==true));
        assertEquals(true, (itEd.next().getElement().equals("Edge6")==true));
        assertEquals(true, (itEd.next().getElement().equals("Edge7")==true));
    }

    /**
     * Test of toString method, of class Graph.
     */
    @Test
    public void testClone() {
        System.out.println("Test Clone");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        Graph<String,String> instClone = instance.clone();

        assertEquals(true, instance.numVertices()==instClone.numVertices());
        assertEquals(true, instance.numEdges()==instClone.numEdges());

        //vertices should be equal
        Iterator<String> itvertClone = instClone.vertices().iterator();
        Iterator<String> itvertSource = instance.vertices().iterator();
        while (itvertSource.hasNext())
            assertEquals(true,(itvertSource.next().equals(itvertClone.next())==true));
    }

    @Test
    public void testEquals() {
        System.out.println("Test Equals");

        instance.insertEdge("A","B","Edge1",6);
        instance.insertEdge("A","C","Edge2",1);
        instance.insertEdge("B","D","Edge3",3);
        instance.insertEdge("C","D","Edge4",4);
        instance.insertEdge("C","E","Edge5",1);
        instance.insertEdge("D","A","Edge6",2);
        instance.insertEdge("E","D","Edge7",1);
        instance.insertEdge("E","E","Edge8",1);

        assertNotEquals(true, instance.equals(null));

        assertEquals(true, instance.equals(instance));

        assertEquals(true, instance.equals(instance.clone()));

        Graph<String,String> other = instance.clone();

        other.removeEdge("E","E");
        assertNotEquals(true, instance.equals(other));

        other.insertEdge("E","E","Edge8",1);
        assertEquals(true, instance.equals(other));

    }


    /**
     * Test of toString method, of class Graph.
     */
    @Test
    public void testToString() {

        System.out.println(instance);
    }

}

