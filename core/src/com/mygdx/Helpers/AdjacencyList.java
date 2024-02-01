package com.mygdx.Helpers;

import com.mygdx.RoleCast.PlayableCharacter;

import java.util.*;

public class AdjacencyList<E> {
    private final Map<E, List<E>> adjacencyList;

    public AdjacencyList() {
        adjacencyList = new HashMap<>();
    }

    // Add a vertex to the graph
    public void addVertex(E vertex) {
        adjacencyList.put(vertex, new ArrayList<E>());
    }

    // Add an edge between two vertices
    public void addEdge(E source, E destination) {
        adjacencyList.get(source).add(destination);
    }

    public void removeEdge(E source, E destination) {
        adjacencyList.get(source).remove(destination);
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