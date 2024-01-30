package com.mygdx.Helpers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DirectedGraph {

    private GraphNode head;

    public DirectedGraph(Object data) {
        head = new GraphNode(data);
    }

    public void addNode(Object sourceData, Object newData) {
        if (sourceData.equals(newData)) return;
        GraphNode sourceNode = findNode(sourceData);
        if (sourceNode == null) {
            sourceNode = findNode(newData);
            GraphNode newNode = findNode(sourceData);
            if (newNode == null) newNode = new GraphNode(sourceData);
            sourceNode.addNeighbour(newNode);
            return;
        }
        GraphNode newNode = findNode(newData);
        if (newNode == null) newNode = new GraphNode(newData);
        sourceNode.addNeighbour(newNode);
    }

    public void removeNode(Object data) {
        GraphNode nodeToRemove = findNode(data);
        if (nodeToRemove == null) {
            throw new IllegalArgumentException("Node not found in the graph.");
        }
        // Remove the node from its neighbors
        for (GraphNode node : head.neighbours) {
            node.neighbours.remove(nodeToRemove);
        }
        // Remove the node from the graph
        head.neighbours.remove(nodeToRemove);
    }

    private GraphNode findNode(Object data) {
        return findNodeRecursive(head, data);
    }

    private GraphNode findNodeRecursive(GraphNode current, Object data) {
        if (current == null) return null;
        if (current.data.equals(data)) return current;
        for (GraphNode neighbour : current.neighbours) {
            GraphNode result = findNodeRecursive(neighbour, data);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public LinkedList<Object> getNeighbours(Object data) {
        GraphNode node = findNode(data);
        if (node == null || node.neighbours.isEmpty()) return null;
        LinkedList<Object> neighbours = new LinkedList<>();
        for (GraphNode n : node.neighbours) {
            neighbours.add(n.data);
        }
        return neighbours;
    }

    public void printGraph() {
        Set<GraphNode> visited = new HashSet<>();
        printGraphRecursive(head, visited);
    }

    private void printGraphRecursive(GraphNode current, Set<GraphNode> visited) {
        if (current == null || visited.contains(current)) return;

        visited.add(current);

        System.out.print("Node " + current.data + " -> Neighbours: ");
        for (GraphNode neighbour : current.neighbours) {
            System.out.print(neighbour.data + " ");
        }
        System.out.println();

        for (GraphNode neighbour : current.neighbours) {
            printGraphRecursive(neighbour, visited);
        }
    }

    private static class GraphNode {
        private final Object data;
        private final LinkedList<GraphNode> neighbours;

        public GraphNode(Object data) {
            this.data = data;
            neighbours = new LinkedList<>();
        }

        public void addNeighbour(GraphNode node) {
            neighbours.add(node);
        }
    }
}
