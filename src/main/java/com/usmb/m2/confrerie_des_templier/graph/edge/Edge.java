package com.usmb.m2.confrerie_des_templier.graph.edge;

import com.usmb.m2.confrerie_des_templier.EdgeDTO;
import com.usmb.m2.confrerie_des_templier.graph.node.Node;

public class Edge {
    private final String name;
    private final Node node1;
    private final Node node2;
    public double weight = 1;

    public Edge(Node node1, Node node2, String name) {
        this.node1 = node1;
        this.node2 = node2;
        this.name = name;
        node1.addEdge(this);
        node2.addEdge(this);
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

    public EdgeDTO toDTO() {
        return new EdgeDTO(node1.getId(), node2.getId(), name);
    }
}
