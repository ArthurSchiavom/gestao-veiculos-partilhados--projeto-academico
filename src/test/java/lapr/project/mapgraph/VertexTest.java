package lapr.project.mapgraph;

import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author frodrigues
 */
public class VertexTest {

    Vertex<String, Integer> instance = new Vertex<>() ;

    public VertexTest() {
    }

    /**
     * Test of getKey method, of class Vertex.
     */
    @Test
    public void GetKeyTest() {
        System.out.println("getKey");

        int expResult = -1;
        assertEquals(expResult, instance.getKey());

        Vertex<String, Integer> instance1 = new Vertex<>(1,"Vertex1");
        expResult = 1;
        assertEquals(expResult, instance1.getKey());
    }

    /**
     * Test of setKey method, of class Vertex.
     */
    @Test
    public void SetKeyTest() {
        System.out.println("setKey");
        int k = 2;
        instance.setKey(k);
        int expResult = 2;
        assertEquals(expResult, instance.getKey());
    }

    /**
     * Test of getElement method, of class Vertex.
     */
    @Test
    public void GetElementTest() {
        System.out.println("getElement");

        String expResult = null;
        assertEquals(expResult, instance.getElement());

        Vertex<String, Integer> instance1 = new Vertex<>(1,"Vertex1");
        expResult = "Vertex1";
        assertEquals(expResult, instance1.getElement());

    }

    /**
     * Test of setElement method, of class Vertex.
     */
    @Test
    public void SetElementTest() {
        System.out.println("setElement");
        String vInf = "Vertex1";
        instance.setElement(vInf);
        assertEquals(vInf, instance.getElement());
    }

    /**
     * Test of addAdjVert method, of class Vertex.
     */
    @Test
    public void AddAdjVertTest() {
        System.out.println("addAdjVert");

        assertEquals(true, (instance.numAdjVerts()==0));

        String vAdj1 = "VAdj1";
        Edge<String,Integer> edge = new Edge<>();

        instance.addAdjVert(vAdj1,edge);
        assertEquals(true, (instance.numAdjVerts()==1));

        String vAdj2 = "VAdj2";
        instance.addAdjVert(vAdj2,edge);
        assertEquals(true, (instance.numAdjVerts()==2));
    }

    /**
     * Test of getAdjVert method, of class Vertex.
     */
    @Test
    public void GetAdjVertTest() {
        System.out.println("getAdjVert");

        Edge<String,Integer> edge = new Edge<>();
        Object expResult = null;
        assertEquals(expResult, instance.getAdjVert(edge));

        String vAdj = "VertexAdj";
        instance.addAdjVert(vAdj,edge);
        assertEquals(vAdj, instance.getAdjVert(edge));
    }

    /**
     * Test of remAdjVert method, of class Vertex.
     */
    @Test
    public void RemAdjVertTest() {
        System.out.println("remAdjVert");

        Edge<String,Integer> edge1 = new Edge<>();
        String vAdj = "VAdj1";
        instance.addAdjVert(vAdj,edge1);

        Edge<String,Integer> edge2 = new Edge<>();
        vAdj = "VAdj2";
        instance.addAdjVert(vAdj,edge2);

        instance.remAdjVert(vAdj);
        assertEquals(true, (instance.numAdjVerts()==1));

        vAdj = "VAdj1";
        instance.remAdjVert(vAdj);
        assertEquals(true, (instance.numAdjVerts()==0));
    }

    /**
     * Test of getEdge method, of class Vertex.
     */
    @Test
    public void GetEdgeTest() {
        System.out.println("getEdge");

        Edge<String,Integer> edge1 = new Edge<>();
        String vAdj1 = "VAdj1";
        instance.addAdjVert(vAdj1,edge1);

        assertEquals(edge1, instance.getEdge(vAdj1));

        Edge<String,Integer> edge2 = new Edge<>();
        String vAdj2 = "VAdj2";
        instance.addAdjVert(vAdj2,edge2);

        assertEquals(edge2, instance.getEdge(vAdj2));
    }

    /**
     * Test of numAdjVerts method, of class Vertex.
     */
    @Test
    public void NumAdjVertsTest() {
        System.out.println("numAdjVerts");

        Edge<String,Integer> edge1 = new Edge<>();
        String vAdj1 = "VAdj1";
        instance.addAdjVert(vAdj1,edge1);

        assertEquals(true, (instance.numAdjVerts()==1));

        Edge<String,Integer> edge2 = new Edge<>();
        String vAdj2 = "VAdj2";
        instance.addAdjVert(vAdj2,edge2);

        assertEquals(true, (instance.numAdjVerts()==2));

        instance.remAdjVert(vAdj1);

        assertEquals(true, (instance.numAdjVerts()==1));

        instance.remAdjVert(vAdj2);
        assertEquals(true, (instance.numAdjVerts()==0));

    }

    /**
     * Test of getAllAdjVerts method, of class Vertex.
     */
    @Test
    public void GetAllAdjVertsTest() {
        System.out.println("getAllAdjVerts");

        Iterator<String> itVerts = instance.getAllAdjVerts().iterator();

        assertEquals(true, itVerts.hasNext()==false);

        Edge<String,Integer> edge1 = new Edge<>();
        String vAdj1 = "VAdj1";
        instance.addAdjVert(vAdj1,edge1);

        Edge<String,Integer> edge2 = new Edge<>();
        String vAdj2 = "VAdj2";
        instance.addAdjVert(vAdj2,edge2);

        itVerts = instance.getAllAdjVerts().iterator();

        assertEquals(true, (itVerts.next().compareTo("VAdj1")==0));
        assertEquals(true,(itVerts.next().compareTo("VAdj2")==0));

        instance.remAdjVert(vAdj1);

        itVerts = instance.getAllAdjVerts().iterator();
        assertEquals(true,(itVerts.next().compareTo("VAdj2"))==0);

        instance.remAdjVert(vAdj2);

        itVerts = instance.getAllAdjVerts().iterator();
        assertEquals(true,itVerts.hasNext()==false);
    }

    /**
     * Test of getAllOutEdges method, of class Vertex.
     */
    @Test
    public void GetAllOutEdgesTest() {
        System.out.println("getAllOutEdges");

        Iterator<Edge<String,Integer>> itEdges = instance.getAllOutEdges().iterator();

        assertEquals(true, itEdges.hasNext()==false);

        Edge<String,Integer> edge1 = new Edge<>();
        String vAdj1 = "VAdj1";
        instance.addAdjVert(vAdj1,edge1);

        Edge<String,Integer> edge2 = new Edge<>();
        String vAdj2 = "VAdj2";
        instance.addAdjVert(vAdj2,edge2);

        itEdges = instance.getAllOutEdges().iterator();

        assertEquals(true, (itEdges.next().compareTo(edge1)==0));
        assertEquals(true,(itEdges.next().compareTo(edge2)==0));

        instance.remAdjVert(vAdj1);

        itEdges = instance.getAllOutEdges().iterator();
        assertEquals(true,(itEdges.next().compareTo(edge2))==0);

        instance.remAdjVert(vAdj2);

        itEdges = instance.getAllOutEdges().iterator();
        assertEquals(true,itEdges.hasNext()==false);
    }

    /**
     * Test of equals method, of class Vertex.
     */
    @Test
    public void EqualsTest() {
        System.out.println("equals");

        instance.setKey(1);
        instance.setElement("Vertex1");

        Vertex<String, Integer> instance2 = new Vertex<>(2,"Vertex2");

        Edge<String,Integer> edge1 = new Edge<>(null, 2, instance, instance2);
        instance.addAdjVert("vAdj2",edge1);

        assertNotEquals(true, instance.equals(null));

        assertEquals(true, instance.equals(instance));

        assertEquals(true, instance.equals(instance.clone()));

        Vertex<String,Integer> other = instance.clone();
        other.remAdjVert("vAdj2");
        assertNotEquals(true, instance.equals(other));

        other.addAdjVert("vAdj2",edge1);
        assertEquals(true, instance.equals(other));

        Vertex<String, Integer> instance3 = new Vertex<>(3,"Vertex3");
        Edge<String,Integer> edge2 = new Edge<>(null, 3, instance, instance3);
        instance.addAdjVert("vAdj3",edge2);
        assertNotEquals(true, instance.equals(other));
    }

    /**
     * Test of clone method, of class Vertex.
     */
    @Test
    public void CloneTest() {
        System.out.println("clone");

        Edge<String,Integer> edge1 = new Edge<>();
        String vAdj1 = "VAdj1";
        instance.addAdjVert(vAdj1,edge1);

        Edge<String,Integer> edge2 = new Edge<>();
        String vAdj2 = "VAdj2";
        instance.addAdjVert(vAdj2,edge2);

        Vertex<String,Integer> instClone = instance.clone();

        assertEquals(true, instance.numAdjVerts()==instClone.numAdjVerts());

        //adjacency vertices should be equal
        Iterator<String> itvertClone = instClone.getAllAdjVerts().iterator();
        Iterator<String> itvertSource = instance.getAllAdjVerts().iterator();
        while (itvertSource.hasNext())
            assertEquals(true,(itvertSource.next().equals(itvertClone.next())==true));

        //and edges also
        Iterator<Edge<String,Integer>> itedgeSource = instance.getAllOutEdges().iterator();
        while (itedgeSource.hasNext()){
            Iterator<Edge<String,Integer>> itedgeClone = instClone.getAllOutEdges().iterator();
            boolean exists=false;
            while (itedgeClone.hasNext()){
                if (itedgeSource.next().equals(itedgeClone.next()))
                    exists=true;
            }
            assertEquals(true,(exists==true));
        }
    }

    /**
     * Test of toString method, of class Vertex.
     */
    @Test
    public void ToStringTest() {
        System.out.println("toString");

        instance.setKey(1);
        instance.setElement("Vertex1");

        Vertex<String, Integer> instance2 = new Vertex<>(2,"Vertex2");

        Edge<String,Integer> edge1 = new Edge<>(null, 2, instance, instance2);
        String vAdj2 = "VAdj2";
        instance.addAdjVert(vAdj2,edge1);

        Vertex<String, Integer> instance3 = new Vertex<>(3,"Vertex3");
        Edge<String,Integer> edge2 = new Edge<>(null, 3, instance, instance3);
        String vAdj3 = "VAdj3";
        instance.addAdjVert(vAdj3,edge2);

        System.out.println(instance);
    }

}
