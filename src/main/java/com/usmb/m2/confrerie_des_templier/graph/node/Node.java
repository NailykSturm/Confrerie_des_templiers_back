package com.usmb.m2.confrerie_des_templier.graph.node;

import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Node {
    private static int idCounter = 0;
    private final int id = idCounter++;
    private String name;
    private final List<Edge> edges = new ArrayList<>();

    public Node() {}

    public Node(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
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

    public boolean isConcept() {
        return false;
    }
    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("name", this.name);
        json.put("type", "Node");
        return json;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
