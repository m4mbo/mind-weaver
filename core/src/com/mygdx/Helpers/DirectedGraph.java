package com.mygdx.Helpers;

import java.util.LinkedList;

public class DirectedGraph {

    private GraphNode head;

    public DirectedGraph(Object data) {
        head = new GraphNode(data);
    }

    public void addNode(Object sourceData, Object newData) {
        GraphNode sourceNode = findNode(sourceData);
        if (sourceNode == null) {
            throw new IllegalArgumentException("Source node not found in the graph.");
        }
        GraphNode newNode = new GraphNode(newData);
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

    public Object getNextNeighbour(Object data) {
        GraphNode node = findNode(data);
        if (node == null || node.neighbours.isEmpty()) return null;
        return node.neighbours.getFirst().data;
    }

    public Object getNextNeighbour(Object sourceData, Object maskData) {
        GraphNode node = findNode(sourceData);
        if (node == null || node.neighbours.isEmpty()) return null;
        for (GraphNode neighbour : node.neighbours) {
            if (!neighbour.data.equals(maskData)) return neighbour.data;
        }
        return null;
    }

    public void printGraph() {
        printGraphRecursive(head);
    }

    private void printGraphRecursive(GraphNode current) {
        if (current == null) return;

        System.out.print("Node " + current.data + " -> Neighbours: ");
        for (GraphNode neighbour : current.neighbours) {
            System.out.print(neighbour.data + " ");
        }
        System.out.println();

        for (GraphNode neighbour : current.neighbours) {
            printGraphRecursive(neighbour);
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
