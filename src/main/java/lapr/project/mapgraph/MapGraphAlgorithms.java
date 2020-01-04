/*
* A collection of graph algorithms.
 */
package lapr.project.mapgraph;

import java.util.*;

/**
 *
 * @author DEI-ESINF
 */
public class MapGraphAlgorithms {

    private MapGraphAlgorithms() {
    }

    /**
     * Performs breadth-first search of a Graph starting in a Vertex
     *
     * @param g Graph instance
     * @param vert information of the Vertex that will be the source of the
     * search
     * @return qbfs a queue with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> breadthFirstSearch(Graph<V, E> g, V vert) {
        LinkedList<V> result = new LinkedList<>();
        LinkedList<V> aux = new LinkedList<>();
        result.add(vert);
        aux.add(vert);
        while (!aux.isEmpty()) {
            V prevRem = aux.getFirst();
            aux.removeFirst();
            try {
                for (V adj : g.adjVertices(prevRem)) {
                    if (!result.contains(adj)) {
                        result.add(adj);
                        aux.add(adj);
                    }
                }
            } catch (NullPointerException exception) {
                return null;
            }
        }
        return result;
    }

    /**
     * Performs depth-first search starting in a Vertex
     *
     * @param g Graph instance
     * @param vOrig Vertex of graph g that will be the source of the search
     * @param qdfs queue with vertices of depth-first search
     */
    private static <V, E> void depthFirstSearch(Graph<V, E> g, V vOrig, LinkedList<V> qdfs) {
        qdfs.add(vOrig);
        for (V adj : g.adjVertices(vOrig)) {
            if (!qdfs.contains(adj)) {
                depthFirstSearch(g, adj, qdfs);
            }
        }
    }

    /**
     * @param g Graph instance
     * @param vert information of the Vertex that will be the source of the
     * search
     * @return qdfs a queue with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> depthFirstSearch(Graph<V, E> g, V vert) {
        LinkedList<V> result = new LinkedList<>();
        try {
            depthFirstSearch(g, vert, result);
        } catch (NullPointerException exception) {
            return null;
        }
        return result;
    }

    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vDest Vertex that will be the end of the path
     * @param path stack with vertices of the current path (the path is in
     * reverse order)
     * @param paths ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        path.add(vOrig);
        for (V adj : g.adjVertices(vOrig)) {
            if (adj.equals(vDest)) {
                path.add(vDest);
                paths.add(new LinkedList<>(path));
                path.removeLast();
            } else if (!path.contains(adj)) {            //Path doesn't contain adjacent vertice
                allPaths(g, adj, vDest, path, paths);
            }
        }
        path.removeLast();
    }

    /**
     * @param g Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @return paths ArrayList with all paths from voInf to vdInf
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        ArrayList<LinkedList<V>> arrResult = new ArrayList<>();
        LinkedList<V> path = new LinkedList<>();
        try {
            allPaths(g, vOrig, vDest, path, arrResult);
        } catch (NullPointerException exception) {
            return null;
        }
        return arrResult;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with nonnegative edge weights This implementation
     * uses Dijkstra's algorithm
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vertices auxiliar vertices
     * @param visited set of discovered vertices
     * @param pathKeys minimum path vertices keys
     * @param dist minimum distances
     */
    protected static <V, E> void shortestPathLength(Graph<V, E> g, V vOrig, V[] vertices, boolean[] visited, int[] pathKeys, double[] dist) {
        int vkey = g.getKey(vOrig);
        dist[vkey] = 0;
        while (vkey != -1) {
            vOrig = vertices[vkey];
            visited[vkey] = true;
            for (V adj : g.adjVertices(vOrig)) {
                int vkeyAdj = g.getKey(adj);
                Edge<V, E> edge = g.getEdge(vOrig, adj);
                if (!visited[vkeyAdj] && dist[vkeyAdj] > dist[vkey] + edge.getWeight()) {
                    dist[vkeyAdj] = dist[vkey] + edge.getWeight();
                    pathKeys[vkeyAdj] = vkey;
                }
            }
            double minDist = Double.MAX_VALUE;
            vkey = -1;
            for (int i = 0; i < g.numVertices(); i++) {
                if (!visited[i] && dist[i] < minDist) {
                    minDist = dist[i];
                    vkey = i;
                }
            }
        }
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf The path
     * is constructed from the end to the beginning
     *
     * @param g Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path stack with the minimum path (correct order)
     */
    protected static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest, V[] verts, int[] pathKeys, LinkedList<V> path) {
        if (!vOrig.equals(vDest)) {     //vOrig doesn't equal vDest
            path.push(vDest);
            int vKey = g.getKey(vDest);
            int prevVKey = pathKeys[vKey];
            vDest = verts[prevVKey];
            getPath(g, vOrig, vDest, verts, pathKeys, path);
        } else {
            path.push(vOrig);
        }
    }

    //shortest-path between vOrig and vDest
    @SuppressWarnings("Duplicates")
    public static <V, E> double shortestPath(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return 0;
        }
        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts];
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];
        V[] vertices = g.allkeyVerts();

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);
        double lengthPath = dist[g.getKey(vDest)];
        if (lengthPath != Double.MAX_VALUE) {
            getPath(g, vOrig, vDest, vertices, pathKeys, shortPath);
            return lengthPath;
        }
        return 0;
    }

    //shortest-path between voInf and all other
    @SuppressWarnings("Duplicates")
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig, ArrayList<LinkedList<V>> paths, ArrayList<Double> dists) {

        if (!g.validVertex(vOrig)) {
            return false;
        }

        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts]; //default value: false
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];
        V[] vertices = g.allkeyVerts();

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);

        dists.clear();
        paths.clear();
        for (int i = 0; i < nverts; i++) {
            paths.add(null);
            dists.add(null);
        }
        for (int i = 0; i < nverts; i++) {
            LinkedList<V> shortPath = new LinkedList<>();
            if (dist[i] != Double.MAX_VALUE) {
                getPath(g, vOrig, vertices[i], vertices, pathKeys, shortPath);
            }
            paths.set(i, shortPath);
            dists.set(i, dist[i]);
        }
        return true;
    }

    /**
     * Reverses the path
     *
     * @param path stack with path
     */
    private static <V, E> LinkedList<V> revPath(LinkedList<V> path) {
        LinkedList<V> pathcopy = new LinkedList<>(path);
        LinkedList<V> pathrev = new LinkedList<>();

        while (!pathcopy.isEmpty()) {
            pathrev.push(pathcopy.pop());
        }

        return pathrev;
    }

    /**
     * Calculates the shortest path problem with constraints searching for the nearest path everytime with the use of
     * Dijktra's Algorithms
     * It's a greedy algorithm, looking for the optimal solution in each step which might not lead to the optimal solution in the
     * end, but it does so in a speedy manner instead of the brute force approach which is a NP Hard problem to solve
     * @param g the graph
     * @param vOrig the original vertex
     * @param vDest the destination vertex
     * @param constraints a list with all the vertexes we must pass by ORDERED
     * @return the shortest path with all the constraints
     */
    @SuppressWarnings("Duplicates")
    public static <V, E> double shortestPathWithConstraintsGreedy(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> constraints, LinkedList<V> shortPath) {
        LinkedList<V> temp = new LinkedList<>();
        double totalLength = 0;
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return 0;
        }
        if(constraints.isEmpty()) {
            totalLength = shortestPath(g, vOrig, vDest, shortPath);
            return totalLength;
        }
        if(vOrig.equals(vDest)) {
            shortPath.add(vDest);
            return 0;
        }

        int nverts = g.numVertices();
        V previousVertex = vOrig;
        while(!constraints.isEmpty()) {     //While constraints is not empty
            boolean[] visited = new boolean[nverts];
            int[] pathKeys = new int[nverts];
            double[] dist = new double[nverts];
            V[] vertices = g.allkeyVerts();

            for (int i = 0; i < nverts; i++) {
                dist[i] = Double.MAX_VALUE;
                pathKeys[i] = -1;
            }

            shortestPathLength(g, previousVertex, vertices, visited, pathKeys, dist);
            V nextVertex = constraints.getFirst();
            double min = dist[g.getKey(nextVertex)];
            for(V vert : constraints) {
                if(dist[g.getKey(vert)] < min) {
                    nextVertex = vert;
                    min = dist[g.getKey(vert)];
                }
            }
            totalLength += min;
            getPath(g, previousVertex, nextVertex, vertices, pathKeys, temp);
            constraints.remove(nextVertex);
            previousVertex = nextVertex;
            temp.removeLast();
            shortPath.addAll(temp);
            temp.clear();
        }
        temp.clear();
        totalLength += shortestPath(g, previousVertex, vDest, temp);
        shortPath.addAll(temp);
        return totalLength;
    }

    /**
     * Calculates all constraints
     * @param g the graphic
     * @param vOrig the origin vertex
     * @param vDest the destination vertex
     * @param constraints all the vertexes the path must pass through
     * @param shortPath the result path
     * @param <V> a vertex
     * @param <E> a edge
     * @return the shortest path going through all constraints
     */
    @SuppressWarnings("Duplicates")
    public static <V, E> double shortestPathWithConstraints(Graph<V, E> g, V vOrig, V vDest, Set<V> constraints, List<LinkedList<V>> shortPaths) {
        double totalWeight = Double.MAX_VALUE;
        //Performs all the standard checks
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return 0;
        }
        if(constraints.isEmpty()) {
            shortPaths.clear();
            shortPaths.add(new LinkedList<>());
            totalWeight = shortestPath(g, vOrig, vDest, shortPaths.get(0));
            return totalWeight;
        }
        if(vOrig.equals(vDest)) {
            shortPaths.get(0).add(vDest);
            return 0;
        }
        ArrayList<LinkedList<V>> allSolutions = allPaths(g, vOrig, vDest);
        ArrayList<LinkedList<V>> filteredSolutions = new ArrayList<>();
        //Checks if no solutions were found
        if(allSolutions==null) {
            return 0;
        }
        //Filters all the solutions to only the paths that contain all the constraints
        for(LinkedList<V> list : allSolutions) {
            if(list.containsAll(constraints)) {
               filteredSolutions.add(list);
            }
        }
        //Checks if no solutions containing all constraints were found
        if(filteredSolutions.isEmpty()) {
            return 0;
        }
        int i = 0;
        for(LinkedList<V> path: filteredSolutions) {
            double temp = 0;
            Iterator<V> it = path.iterator();
            V prevVert = it.next();
            while(it.hasNext()) {
                V nextVert = it.next();
                Edge<V, E> edge = g.getEdge(prevVert, nextVert);
                prevVert = nextVert;
                temp += edge.getWeight();                       //Goes through all edges in solution and calculates total cost
            }
            if(temp<totalWeight) {                              //If total cost is less than already found solution, then record this
                shortPaths.clear();                              //as the optimal solution
                shortPaths.add(new LinkedList<V>(path));
                totalWeight = temp;
            }else if(temp == totalWeight){
                shortPaths.add(new LinkedList<V>(path));
            }
            i++;
        }
        return totalWeight;
    }
}
