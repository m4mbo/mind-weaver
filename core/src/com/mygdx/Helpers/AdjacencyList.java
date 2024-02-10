package com.mygdx.Helpers;
import java.util.*;

public class AdjacencyList<E> {
    private final Map<E, List<E>> adjacencyList;

    public AdjacencyList() {
        adjacencyList = new HashMap<>();
    }

    // Add a vertex to the graph
    public void addVertex(E vertex) {
        if (vertex == null) return;
        adjacencyList.put(vertex, new ArrayList<E>());
    }
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

    public LinkedList<E> getVerticesWithNeighbours() {
        LinkedList<E> vertices = new LinkedList<>();
        for (Map.Entry<E, List<E>> entry : adjacencyList.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                vertices.add(entry.getKey());
            }
        }
        return vertices;
    }


    // Get all vertices pointing to the given vertex
    public List<E> getVerticesPointingTo(E destination) {
        List<E> verticesPointingTo = new ArrayList<>();
        for (Map.Entry<E, List<E>> entry : adjacencyList.entrySet()) {
            E vertex = entry.getKey();
            List<E> neighbors = entry.getValue();
            if (neighbors.contains(destination)) {
                verticesPointingTo.add(vertex);
            }
        }
        return verticesPointingTo;
    }

    public LinkedList<E> getNeighbours(E source) {
        return new LinkedList<E>(adjacencyList.get(source));
    }

    public void printAdjacencyList() {
        for (Map.Entry<E, List<E>> entry : adjacencyList.entrySet()) {
            Object vertex = entry.getKey();
            List<E> neighbors = entry.getValue();

            System.out.print("Vertex " + vertex + " is connected to: ");
            for (Object neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }
}
