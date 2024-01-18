package com.usmb.m2.confrerie_des_templier.graph;

import com.usmb.m2.confrerie_des_templier.EdgeDTO;
import com.usmb.m2.confrerie_des_templier.GraphDTO;
import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;
import com.usmb.m2.confrerie_des_templier.graph.node.Node;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodes = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.getName(), node);
    }

    public Optional<Node> searchNode(String name) {
        return Optional.ofNullable(nodes.getOrDefault(name , null));
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                '}';
    }

    public GraphDTO makeSubGraph(String name, int maxDepth) {
        Node start = nodes.get(name);
        HashMap<Node, Integer> nodeMap = new HashMap<>();
        addNodeRec(start, nodeMap, maxDepth);
        List<HashMap<String, Object>> nodes = new ArrayList<>();
        nodes.add(start.toJson());
        List<EdgeDTO> edges = new ArrayList<>();
        int max = 0;
        for (Integer value : nodeMap.values()) {
            if (value > max) {
                max = value;
            }
        }
        for (Node node : nodeMap.keySet()) {
            nodes.add(node.toJson());
            EdgeDTO edgeDTO = new EdgeDTO(start.getId(), node.getId(), nodeMap.get(node) / (double) max);
            edges.add(edgeDTO);
        }
        return new GraphDTO(nodes, edges);
    }

    private void addNodeRec(Node start, HashMap<Node, Integer> nodeMap, int depth) {
        if (depth == 0) {
            return;
        }
        for (Edge edge : start.getEdges()) {
            Node neighbour = edge.getOtherNode(start);
            if (!neighbour.isConcept()) {
                nodeMap.put(neighbour, nodeMap.getOrDefault(neighbour, 0) + 1);
            }
            addNodeRec(neighbour, nodeMap, depth - 1);
        }
    }
}
