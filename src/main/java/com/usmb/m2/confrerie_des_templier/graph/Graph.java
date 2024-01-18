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

    public GraphDTO getGraph(int maxDepth, int maxNodes) {
        System.out.println(nodes.size());
        Set<Node> nodeSet = new HashSet<>();
        Set<Edge> edgeSet = new HashSet<>();
        Node start = nodes.get("Game");
        nodeSet.add(start);
        addNeighboursRec(start, nodeSet, edgeSet, maxDepth, maxNodes);
        List<HashMap<String, Object>> nodes = new ArrayList<>();
        List<EdgeDTO> edges = new ArrayList<>();
        for (Node node : nodeSet) {
            nodes.add(node.toJson());
        }
        for (Edge edge : edgeSet) {
            edges.add(edge.toDTO());
        }
        return new GraphDTO(nodes, edges);
    }

    private void addNeighboursRec(Node start, Set<Node> nodeSet, Set<Edge> edgeSet, int depth, int maxNodes) {
        if (depth == 0 || nodeSet.size() >= maxNodes) {
            return;
        }
        for (Edge edge : start.getEdges()) {
            if (nodeSet.size() >= maxNodes) {
                return;
            }
            Node neighbour = edge.getOtherNode(start);
            nodeSet.add(neighbour);
            for (Edge e : neighbour.getEdges()) {
                if (edgeSet.add(e)) {
                    addNeighboursRec(neighbour, nodeSet, edgeSet, depth - 1, maxNodes);
                }
            }
        }
    }

    public GraphDTO makeSubGraph(String name, int maxDepth) {
        Node start = nodes.get(name);
        HashMap<Node, Integer> nodeMap = new HashMap<>();
        addNodeRec(start, nodeMap, maxDepth);
        List<HashMap<String, Object>> nodes = new ArrayList<>();
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
