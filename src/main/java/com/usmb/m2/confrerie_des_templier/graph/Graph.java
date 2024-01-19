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
        if (start.isConcept()) {
            Set<Node> nodeSet = new HashSet<>();
            nodeSet.add(start);
            makeConceptSubGraphRec(start, nodeSet, edges);
            for (Node node : nodeSet) {
                nodesJson.add(node.toJson());
            }
        }
        else {
            HashMap<Node, Double> nodeMap = new HashMap<>();
            addNodeRec(start, nodeMap, maxDepth);
            double max = 0;
            for (Double value : nodeMap.values()) {
                if (value > max) {
                    max = value;
                }
            }
            nodesJson.add(start.toJson());
            for (Node node : nodeMap.keySet()) {
                double weight = nodeMap.get(node) / max;
                if (weight > 0.2) {
                    nodesJson.add(node.toJson());
                    EdgeDTO edgeDTO = new EdgeDTO(start.getId(), node.getId(), getShortestPath(start, node),weight);
                    edges.add(edgeDTO);
                }
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
            edges.add(new EdgeDTO(start.getId(), neighbour.getId(), "is", 1));
            if (neighbour.isConcept() && added) {
                makeConceptSubGraphRec(neighbour, nodeSet, edges);
            }
        }
    }

    private void addNodeRec(Node node, HashMap<Node, Double> nodeMap, int depth) {
        if (depth == 0) {
            return;
        }
        for (Edge edge : node.getEdges()) {
            Node neighbour = edge.getOtherNode(node);
            if (!neighbour.isConcept()) {;
                nodeMap.put(neighbour, nodeMap.getOrDefault(neighbour, 0.0) + Math.pow(depth, 2));
            }
            addNodeRec(neighbour, nodeMap, depth - 1);
        }
    }

    private String getShortestPath(Node start, Node end) {
        Queue<Node> queue = new LinkedList<>();
        Map<Node, Node> parent = new HashMap<>();
        queue.add(start);
        parent.put(start, null);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.equals(end)) {
                break;
            }
            for (Edge edge : node.getEdges()) {
                Node neighbour = edge.getOtherNode(node);
                if (!parent.containsKey(neighbour)) {
                    queue.add(neighbour);
                    parent.put(neighbour, node);
                }
            }
        }
        if (!parent.containsKey(end)) {
            return null;
        }
        StringBuilder path = new StringBuilder();
        Node node = end;
        while (node != null) {
            path.insert(0, node.getName());
            Node p = parent.get(node);
            if (p != null) {
                path.insert(0, "]- ");
                path.insert(0, node.findEdge(p).orElseThrow().name());
                path.insert(0, " -[");
            }
            node = p;
        }
        return path.toString();
    }
}
