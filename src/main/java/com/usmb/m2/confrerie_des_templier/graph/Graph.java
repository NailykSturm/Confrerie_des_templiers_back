package com.usmb.m2.confrerie_des_templier.graph;

import com.usmb.m2.confrerie_des_templier.EdgeDTO;
import com.usmb.m2.confrerie_des_templier.GraphDTO;
import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;
import com.usmb.m2.confrerie_des_templier.graph.node.Node;

import java.util.*;

public class Graph {
    private Map<String, Node> nodes = new HashMap<String, Node>();

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
}
