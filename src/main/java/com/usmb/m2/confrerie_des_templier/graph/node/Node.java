package com.usmb.m2.confrerie_des_templier.graph.node;

import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;
    private List<Edge> edges = new ArrayList<>();

    public Node() {}

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }
}
