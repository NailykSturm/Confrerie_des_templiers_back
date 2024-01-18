package com.usmb.m2.confrerie_des_templier.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmb.m2.confrerie_des_templier.GraphDTO;
import com.usmb.m2.confrerie_des_templier.graph.Graph;
import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;
import com.usmb.m2.confrerie_des_templier.graph.node.*;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Service
public class GraphService {
    private final Graph graph = new Graph();

    @PostConstruct
    public void initialize() {
        try {
            String path = "src/main/resources/";
            ObjectMapper objectMapper = new ObjectMapper();
            initGames(path, objectMapper);
            initTimeline(path, objectMapper);
            initLocations(path, objectMapper);
            initSupports(path, objectMapper);
            initRelations(path, objectMapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initGames(String path, ObjectMapper objectMapper) throws IOException {
        Node gameConcept = new Concept("Jeu vidéo");
        graph.addNode(gameConcept);

        Node spinOffConcept = new Concept("SpinOff");
        graph.addNode(spinOffConcept);
        new Edge(spinOffConcept, gameConcept, "is");

        Node principale = new Concept("Jeu Principal");
        graph.addNode(principale);
        new Edge(principale, gameConcept, "is");

        File file = new File(path + "games.json");
        JsonNode jsonNode = objectMapper.readTree(file);
        JsonNode main = jsonNode.get("principale");
        JsonNode spinOff = jsonNode.get("spinOff");
        for (JsonNode node : main) {
            Game game = new Game();
            game.setName(node.get("name").asText());
            game.setDate(node.get("date").asText());
            game.setImg(node.get("img").asText());
            graph.addNode(game);
            new Edge(game, principale, "is");
        }
        for (JsonNode node : spinOff) {
            Game game = new Game();
            game.setName(node.get("name").asText());
            game.setDate(node.get("date").asText());
            game.setImg(node.get("img").asText());
            graph.addNode(game);
            new Edge(game, spinOffConcept, "is");
        }
    }

    private void initTimeline(String path, ObjectMapper objectMapper) throws IOException {
        Node timeline = new Concept("Chronologie");
        graph.addNode(timeline);

        Node present = new Concept("Present");
        graph.addNode(present);
        new Edge(present, timeline, "is");

        Node past = new Concept("Passé");
        graph.addNode(past);
        new Edge(past, timeline, "is");

        Node isu = new Concept("Isu");
        graph.addNode(isu);
        new Edge(isu, timeline, "is");

        File file = new File(path + "timelines.json");
        JsonNode jsonNode = objectMapper.readTree(file);
        for (Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> nodes = it.next();
            String type = nodes.getKey();
            for (JsonNode node : nodes.getValue()) {
                Timeline t = objectMapper.treeToValue(node, Timeline.class);
                graph.addNode(t);
                switch (type) {
                    case "Present":
                        new Edge(t, present, "is");
                        break;
                    case "Past":
                        new Edge(t, past, "is");
                        break;
                    case "Isu":
                        new Edge(t, isu, "is");
                        break;
                }
            }
        }
    }

    private void initLocations(String path, ObjectMapper objectMapper) throws IOException {
        Node locationConcept = new Concept("Localisation");
        graph.addNode(locationConcept);
        File file = new File(path + "locations.json");
        JsonNode locationNodes = objectMapper.readTree(file);
        for (JsonNode node : locationNodes) {
            Location location = new Location();
            location.setName(node.get("name").asText());
            JsonNode imagesNode = node.get("images");
            Iterator<String> iterator = imagesNode.fieldNames();
            iterator.forEachRemaining(e -> location.addImage(e, imagesNode.get(e).asText()));
            this.graph.addNode(location);
            new Edge(location, locationConcept, "is");
        }
    }

    private void initSupports(String path, ObjectMapper objectMapper) throws IOException {
        Node supportConcept = new Concept("Support");
        Node manufacturerConcept = new Concept("Constructeur");
        Node generationConcept = new Concept("Génération");
        graph.addNode(supportConcept);
        graph.addNode(manufacturerConcept);
        graph.addNode(generationConcept);
        File file = new File(path + "supports.json");
        JsonNode fileNodes = objectMapper.readTree(file);
        JsonNode manufacturerNode = fileNodes.get("manufacturers");
        for (JsonNode jsonNode : manufacturerNode) {
            Node node = new Concept(jsonNode.asText());
            this.graph.addNode(node);
            new Edge(node, manufacturerConcept, "is");
        }
        JsonNode generationNode = fileNodes.get("generations");
        for (JsonNode jsonNode : generationNode) {
            Node node = new Concept(jsonNode.asText());
            this.graph.addNode(node);
            new Edge(node, supportConcept, "belongs to");
            new Edge(node, generationConcept, "is");
        }
        JsonNode supportNodes = fileNodes.get("supports");
        for (JsonNode jsonNode : supportNodes) {
            Support node = new Support();
            node.setName(jsonNode.get("name").asText());
            node.setImg(jsonNode.get("img").asText());
            if (jsonNode.has("date")) {
                node.setDateSortie(jsonNode.get("date").asText());
            }
            if (jsonNode.has("manufacturer")) {
                new Edge(node, graph.searchNode(jsonNode.get("manufacturer").asText()).get(), "built by");
            }
            if (jsonNode.has("generation")) {
                new Edge(node, graph.searchNode(jsonNode.get("generation").asText()).get(), "is part of");
            } else {
                new Edge(node, supportConcept, "is");
            }
            this.graph.addNode(node);
        }
    }

    private void initRelations(String path, ObjectMapper objectMapper) throws IOException {
        File file = new File(path + "relations.json");
        JsonNode fileNodes = objectMapper.readTree(file);
        for (JsonNode jsonNode : fileNodes) {
            Optional<Node> opt = this.graph.searchNode(jsonNode.get("node1").asText());
            if (opt.isEmpty()) {
                throw new RuntimeException("Node not found");
            }
            Node node1 = opt.get();
            opt = this.graph.searchNode(jsonNode.get("node2").asText());
            if (opt.isEmpty()) {
                throw new RuntimeException("Node not found");
            }
            Node node2 = opt.get();
            new Edge(node1, node2, jsonNode.get("name").asText());
        }
    }

    public GraphDTO getGraph(String name, int maxDepth) {
        return this.graph.makeSubGraph(name, maxDepth);
    }
}
