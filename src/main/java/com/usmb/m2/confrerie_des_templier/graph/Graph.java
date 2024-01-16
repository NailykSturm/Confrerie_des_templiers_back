package com.usmb.m2.confrerie_des_templier.graph;

import com.usmb.m2.confrerie_des_templier.graph.node.Node;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes = new ArrayList<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                '}';
    }
}
