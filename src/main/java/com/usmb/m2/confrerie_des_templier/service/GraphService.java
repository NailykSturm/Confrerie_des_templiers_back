package com.usmb.m2.confrerie_des_templier.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmb.m2.confrerie_des_templier.DTO.GraphDTO;
import com.usmb.m2.confrerie_des_templier.graph.Graph;
import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;
import com.usmb.m2.confrerie_des_templier.graph.node.*;
import com.usmb.m2.confrerie_des_templier.graph.node.Character;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
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
            initCharactersAndFactions(path, objectMapper);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        JsonNode jsonNode = objectMapper.readTree(reader);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        JsonNode jsonNode = objectMapper.readTree(reader);
        for (JsonNode node : jsonNode) {
            Timeline t = objectMapper.treeToValue(node, Timeline.class);
            graph.addNode(t);
        }
    }

    private void initLocations(String path, ObjectMapper objectMapper) throws IOException {
        Node locationConcept = new Concept("Localisation");
        graph.addNode(locationConcept);
        File file = new File(path + "locations.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        JsonNode locationNodes = objectMapper.readTree(reader);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        JsonNode fileNodes = objectMapper.readTree(reader);
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

    private void initCharactersAndFactions(String path, ObjectMapper objectMapper) throws IOException {
        Node factionConcept = new Concept("Faction");
        Node characterConcept = new Concept("Personnage");
        graph.addNode(factionConcept);
        graph.addNode(characterConcept);
        File file = new File(path + "characters.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        JsonNode fileNodes = objectMapper.readTree(reader);
        JsonNode typeCharacterNode = fileNodes.get("type");
        for (JsonNode jsonNode : typeCharacterNode) {
            Node node = new Concept(jsonNode.asText());
            this.graph.addNode(node);
            new Edge(node, characterConcept, "is");
        }
        JsonNode factionNode = fileNodes.get("factions");
        for (JsonNode jsonNode : factionNode) {
            Faction faction = new Faction();
            faction.setName(jsonNode.get("name").asText());
            faction.setImg(jsonNode.get("img").asText());
            faction.setDescription(jsonNode.get("description").asText());
            if (jsonNode.has("parent")) {
                new Edge(faction, graph.searchNode(jsonNode.get("parent").asText()).get(), "extends");
            } else {
                new Edge(faction, factionConcept, "is");
            }
            this.graph.addNode(faction);
        }
        JsonNode characterNode = fileNodes.get("characters");
        for (JsonNode jsonNode : characterNode) {
            Character character = new Character();
            character.setName(jsonNode.get("name").asText());
            character.setImg(jsonNode.get("img").asText());
            if (jsonNode.has("belongs to")) {
                new Edge(character, graph.searchNode(jsonNode.get("belongs to").asText()).get(), "belongs to");
            }
            if (jsonNode.has("is")) {
                new Edge(character, graph.searchNode(jsonNode.get("is").asText()).get(), "is");
            }
            this.graph.addNode(character);
        }
    }

    private void initRelations(String path, ObjectMapper objectMapper) throws IOException {
        File file = new File(path + "relations.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        JsonNode fileNodes = objectMapper.readTree(reader);
        for (JsonNode jsonNode : fileNodes) {
            Optional<Node> opt = this.graph.searchNode(jsonNode.get("node1").asText());
            if (opt.isEmpty()) {
                throw new RuntimeException("Node not found " + jsonNode.get("node1").asText());
            }
            Node node1 = opt.get();
            opt = this.graph.searchNode(jsonNode.get("node2").asText());
            if (opt.isEmpty()) {
                throw new RuntimeException("Node not found " + jsonNode.get("node2").asText());
            }
            Node node2 = opt.get();
            new Edge(node1, node2, jsonNode.get("name").asText());
        }
    }

    public GraphDTO getGraph(String name, int maxDepth) {
        return this.graph.makeSubGraph(name, maxDepth);
    }

    public List<String> getAllConcept() {
        return this.graph.getAllConcept();
    }
}
