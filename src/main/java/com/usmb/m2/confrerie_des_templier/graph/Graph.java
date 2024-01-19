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
        List<HashMap<String, Object>> nodesJson = new ArrayList<>();
        List<EdgeDTO> edges = new ArrayList<>();
        Set<Node> nodeSet = new HashSet<>();
        if (start.isConcept()) {
            nodeSet.add(start);
            makeConceptSubGraphRec(start, nodeSet, edges);
            for (Node node : nodeSet) {
                nodesJson.add(node.toJson());
            }
        }
        else {
            HashMap<Node, Integer> nodeMap = new HashMap<>();
            addNodeRec(start, nodeMap, maxDepth);
            int max = 0;
            for (Integer value : nodeMap.values()) {
                if (value > max) {
                    max = value;
                }
            }
            for (Node node : nodeMap.keySet()) {
                nodesJson.add(node.toJson());
                EdgeDTO edgeDTO = new EdgeDTO(start.getId(), node.getId(), nodeMap.get(node) / (double) max);
                edges.add(edgeDTO);
            }
        }
        return new GraphDTO(nodesJson, edges);
    }

    private void makeConceptSubGraphRec(Node start, Set<Node> nodeSet, List<EdgeDTO> edges) {
        for (Edge edge : start.getEdges()) {
            Node neighbour = edge.getOtherNode(start);
            if (edge.node2() == neighbour)
                continue;
            boolean added = nodeSet.add(neighbour);
            edges.add(new EdgeDTO(start.getId(), neighbour.getId(), 1));
            if (neighbour.isConcept() && added) {
                makeConceptSubGraphRec(neighbour, nodeSet, edges);
            }
        }
    }

    private void addNodeRec(Node start, HashMap<Node, Integer> nodeMap, int depth) {
        if (depth == 0) {
            return;
        }
        for (Edge edge : start.getEdges()) {
            Node neighbour = edge.getOtherNode(start);
            if (!neighbour.isConcept()) {
                nodeMap.put(neighbour, nodeMap.getOrDefault(neighbour, 0) + depth);
            }
            addNodeRec(neighbour, nodeMap, depth - 1);
        }
    }
}
