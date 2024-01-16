package com.usmb.m2.confrerie_des_templier.graph.edge;

import com.usmb.m2.confrerie_des_templier.graph.node.Node;

public abstract class Edge {
    private final String name;
    private final Node node1;
    private final Node node2;
    private final double weight;

    public Edge(Node node1, Node node2, String name, double weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.name = name;
        this.weight = weight;
        node1.addEdge(this);
        node2.addEdge(this);
    }

    public Edge(Node node1, Node node2, String name) {
        this(node1, node2, name, 1);
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public Node getOtherNode(Node node) {
        if (node.equals(node1)) {
            return node2;
        } else if (node.equals(node2)) {
            return node1;
        } else {
            return null;
        }
    }
}
