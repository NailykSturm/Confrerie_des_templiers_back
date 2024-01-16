package com.usmb.m2.confrerie_des_templier.graph.node;

import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    private String name;
    private String[] aliases;
    private List<Edge> edges;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }
}
