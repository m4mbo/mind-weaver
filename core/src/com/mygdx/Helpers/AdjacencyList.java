package com.mygdx.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyList {
    private final Map<Object, List<Object>> adjacencyList;

    public AdjacencyList() {
        adjacencyList = new HashMap<>();
    }

    // Add a vertex to the graph
    public void addVertex(Object vertex) {
        adjacencyList.put(vertex, new ArrayList<>());
    }

    // Add an edge between two vertices
    public void addEdge(Object source, Object destination) {
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source);
    }

    // Print the adjacency list
    public void printAdjacencyList() {
        for (Map.Entry<Object, List<Object>> entry : adjacencyList.entrySet()) {
            Object vertex = entry.getKey();
            List<Object> neighbors = entry.getValue();

            System.out.print("Vertex " + vertex + " is connected to: ");
            for (Object neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }
}
