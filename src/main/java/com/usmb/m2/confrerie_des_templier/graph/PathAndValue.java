package com.usmb.m2.confrerie_des_templier.graph;

import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;
import com.usmb.m2.confrerie_des_templier.graph.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathAndValue {
    public final List<Node> path = new ArrayList<>();
    public double value = 0d;

    public String shortestPath() {
        if (path.size() == 2) {
            return path.getFirst().findEdge(path.getLast()).orElseThrow().name();
        }
        List<Node> shortestPath = new ArrayList<>();
        shortestPath.add(path.getFirst());
        findShortestPathRec(shortestPath, path.getFirst(), path.getLast());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shortestPath.size(); i++) {
            Node node = shortestPath.get(i);
            sb.append(node.getName());
            if (node != shortestPath.getLast()) {
                sb.append(" -[");
                sb.append(node.findEdge(shortestPath.get(i + 1)).orElseThrow().name());
                sb.append("]- ");
            }
        }
        return sb.toString();
    }

    private void findShortestPathRec(List<Node> shortestPath, Node first, Node last) {
        Optional<Edge> next = first.findEdge(last);
        if (next.isPresent()) {
            shortestPath.add(last);
            return;
        }
        List<Node> bestPath = new ArrayList<>();
        for (Edge edge : first.getEdges()) {
            Node neighbour = edge.getOtherNode(first);
            if (!path.contains(neighbour)) {
                path.add(neighbour);
                findShortestPathRec(path, neighbour, last);
                if (bestPath.isEmpty() || path.size() < bestPath.size()) {
                    bestPath = path;
                }
            }
        }
    }
}
