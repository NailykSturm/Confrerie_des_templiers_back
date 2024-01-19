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
            HashMap<Node, PathAndValue> nodeMap = new HashMap<>();
            addNodeRec(start, start, nodeMap, maxDepth);
            System.out.println(nodeMap);
            double max = 0;
            for (PathAndValue value : nodeMap.values()) {
                if (value.value > max) {
                    max = value.value;
                }
            }
            nodesJson.add(start.toJson());
            for (Node node : nodeMap.keySet()) {
                PathAndValue pathAndValue = nodeMap.get(node);
                double weight = pathAndValue.value / max;
                if (weight > 0.2) {
                    nodesJson.add(node.toJson());
                    EdgeDTO edgeDTO = new EdgeDTO(start.getId(), node.getId(), pathAndValue.shortestPath(),weight);
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

    private void addNodeRec(Node start, Node node, HashMap<Node, PathAndValue> nodeMap, int depth) {
        if (depth == 0) {
            return;
        }
        for (Edge edge : node.getEdges()) {
            Node neighbour = edge.getOtherNode(node);
            if (!neighbour.isConcept()) {
                PathAndValue pathAndValue;
                if (nodeMap.containsKey(neighbour)) {
                    pathAndValue = nodeMap.get(neighbour);
                }
                else {
                    pathAndValue = new PathAndValue();
                    nodeMap.put(neighbour, pathAndValue);
                    pathAndValue.path.add(start);
                }
                pathAndValue.path.add(neighbour);
                pathAndValue.value += Math.pow(depth, 2);
            }
            addNodeRec(start, neighbour, nodeMap, depth - 1);
        }
    }
}
