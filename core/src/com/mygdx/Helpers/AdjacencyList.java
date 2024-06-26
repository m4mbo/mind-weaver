package com.mygdx.Helpers;
import java.util.*;

public class AdjacencyList<E> { //Adjacency list to maintain the wave connection between the mage and the goblins
    private final Map<E, List<E>> adjacencyList;

    // Declare adjacency list as a hash map
    public AdjacencyList() {
        adjacencyList = new HashMap<>();
    }

    // Add a vertex to the graph
    public void addVertex(E vertex) {
        if (vertex == null) return;
        adjacencyList.put(vertex, new ArrayList<E>());
    }

    //  Remove a vertex from the graph
    public void removeVertex(E vertex) {
        adjacencyList.remove(vertex);
        for (List<E> edges : adjacencyList.values()) {
            edges.remove(vertex);
        }
    }

    // Add an edge between two vertices
    public boolean addEdge(E source, E destination) {
        if (source == null || destination == null) return false;
        if (adjacencyList.get(source).contains(destination)) return false;
        adjacencyList.get(source).add(destination);
        return true;
    }

    //Remove an edge between two vertices
    public boolean removeEdge(E source, E destination) {
        if (source == null || destination == null) return false;
        if (!adjacencyList.get(source).contains(destination)) return false;
        adjacencyList.get(source).remove(destination);
        return true;
    }

    // Get a spanning tree from a given source vertex
    public Map<E, List<E>> getSpanningTree(E source) {
        Map<E, List<E>> spanningTree = new HashMap<>();
        Set<E> visited = new HashSet<>();
        dfs(source, null, visited, spanningTree);
        return spanningTree;
    }

    // Depth-first search to construct a spanning tree
    private void dfs(E current, E parent, Set<E> visited, Map<E, List<E>> spanningTree) {
        visited.add(current);
        spanningTree.put(current, (List<E>) new ArrayList<>());
        if (parent != null) {
            spanningTree.get(current).add(parent);
        }
        for (E neighbor : adjacencyList.get(current)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, current, visited, spanningTree);
            }
        }
    }

    // Depth-first search to keep track of the visited neighbours
    private boolean dfs(E current, E destination, Set<E> visited) {
        if (current.equals(destination)) return true;
        for (E neighbor : adjacencyList.get(current)) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                if (dfs(neighbor, destination, visited)) return true;
                visited.remove(neighbor); // Backtrack
            }
        }
        return false;
    }

    // Depth-first search to find all reachable vertices
    private void dfs(E current, Set<E> visited, List<E> reachableVertices) {
        visited.add(current);
        reachableVertices.add(current);
        for (E neighbor : adjacencyList.get(current)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, reachableVertices);
            }
        }
    }

    // Get all vertices reachable from a given source vertex
    public List<E> getReachableVertices(E source) {
        List<E> reachableVertices = new ArrayList<>();
        Set<E> visited = new HashSet<>();
        dfs(source, visited, reachableVertices);
        return reachableVertices;
    }

    // Check if a vertex is traceable from another vertex
    public boolean traceable(E source, E destination) {
        if (source == null || destination == null) return false;
        Set<E> visited = new HashSet<>();
        return dfs(source, destination, visited);
    }

    // Get vertices that have neighbours as a linked list
    public LinkedList<E> getVerticesWithNeighbours() {
        LinkedList<E> vertices = new LinkedList<>();
        for (Map.Entry<E, List<E>> entry : adjacencyList.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                vertices.add(entry.getKey());
            }
        }
        return vertices;
    }

    // Get neighbours of a given source as a linked list
    public LinkedList<E> getNeighbours(E source) {
        return new LinkedList<E>(adjacencyList.get(source));
    }
}
