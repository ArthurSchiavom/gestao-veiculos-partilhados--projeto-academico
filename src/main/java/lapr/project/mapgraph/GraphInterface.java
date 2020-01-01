
package lapr.project.mapgraph;

/**
 *
 * @author DEI-ESINF
 * @param <V>
 * @param <E>
 */
public interface GraphInterface<V,E> {
  
  // Returns the number of vertices of the graph 
  int numVertices();
  
  // Returns all the vertices of the graph as an iterable collection  
  Iterable<V> vertices();
  
  // Returns the number of edges of the graph  
  int numEdges();

  // Returns the information of all the edges of the graph as an iterable collection
  Iterable<Edge<V,E>> edges();
   
  /* Returns the edge from vorig to vdest, or null if vertices are not adjacent
   * @param vorig
   * @param vdest
   * @return the edge or null if vertices are not adjacent or don't exist 
   */
   Edge<V,E> getEdge(V vOrig, V vDest);

  /* Returns the vertices of edge e as an array of length two 
   * If the graph is directed, the first vertex is the origin, and
   * the second is the destination.  If the graph is undirected, the
   * order is arbitrary.
   * @param e
   * @return array of two vertices or null if edge doesn't exist 
   */
   V[] endVertices(Edge<V,E> edge); 
  
  /* Returns the vertex that is opposite vertex v on edge e. 
   * @param v
   * @param e
   * @return opposite vertex, or null if vertex or edge don't exist
   */
   V opposite(V vert, Edge<V,E> edge);
   
  /**
   * Returns the number of edges leaving vertex v
   * For an undirected graph, this is the same result returned by inDegree
   * @param v 
   * @return number of edges leaving vertex v, -1 if vertex doesn't exist  
   */
  int outDegree(V vert) ;
  
  /**
   * Returns the number of edges for which vertex v is the destination 
   * For an undirected graph, this is the same result returned by outDegree
   * @param v 
   * @return number of edges leaving vertex v, -1 if vertex doesn't exist  
   */
  int inDegree(V vert) ;
  
  /* Returns an iterable collection of edges for which vertex v is the origin 
  * for an undirected graph, this is the same result returned by incomingEdges
  * @param v
  * @return iterable collection of edges, null if vertex doesn't exist
  */
  Iterable<Edge<V,E>> outgoingEdges (V vert);
  
 /* Returns an iterable collection of edges for which vertex v is the destination
  * For an undirected graph this is the same result as returned by incomingEdges
  * @param v
  * @return iterable collection of edges reaching vertex, null if vertex doesn't exist
  */        
  Iterable<Edge<V,E>> incomingEdges(V vert);
  
  /* Inserts a new vertex with some specific comparable type
   * @param element the vertex contents
   * @return a true if insertion suceeds, false otherwise
   */
   boolean insertVertex(V newVert);

  /* Adds a new edge between vertices u and v, with some 
   * specific comparable type. If vertices u, v don't exist in the graph they  
   * are inserted  
   * @param vorigInf Information of vertex source
   * @param vdestInf Information of vertex destination
   * @param eInf edge information
   * @param eWeight edge weight
   * @return true if suceeds, or false if an edge already exists between the two verts.
   */
   boolean insertEdge(V vOrig, V vDest, E edge, double eWeight);
   
   
  /* Removes a vertex and all its incident edges from the graph 
  * @param vInf Information of vertex source
  */
  boolean removeVertex(V vert);

 /* Removes the edge between two vertices 
  *  
  * @param vA Information of vertex source
  * @param vB Information of vertex destination 
  */  
  boolean removeEdge(V vOrig, V vDest);
  
}
    
 
