package com.usmb.m2.confrerie_des_templier.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmb.m2.confrerie_des_templier.graph.Graph;
import com.usmb.m2.confrerie_des_templier.graph.edge.Edge;
import com.usmb.m2.confrerie_des_templier.graph.node.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Service
public class GraphService {
    private final Graph graph = new Graph();

    @PostConstruct
    public void initialize() {
        try {
            String path = "src/main/resources/";
            ObjectMapper objectMapper = new ObjectMapper();
            this.initGames(path, objectMapper);
            initTimeline(path, objectMapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initGames(String path, ObjectMapper objectMapper) throws IOException {
        File file = new File(path + "game.json");
        JsonNode jsonNode = objectMapper.readTree(file);
        JsonNode main = jsonNode.get("principale");
        JsonNode spinOff = jsonNode.get("spinOff");
        Game[] games = new Game[main.size() + spinOff.size()];
        int i = 0;
        for (JsonNode node : main) {
            Game game = new Game();
            game.setName(node.get("name").asText());
            game.setDate(node.get("date").asText());
            game.setImg(node.get("img").asText());
            game.setType(EGameType.Principale);
            this.graph.addNode(game);
            games[i] = game;
            i++;
        }
        for (JsonNode node : spinOff) {
            Game game = new Game();
            game.setName(node.get("name").asText());
            game.setDate(node.get("date").asText());
            game.setImg(node.get("img").asText());
            game.setType(EGameType.SpinOff);
            this.graph.addNode(game);
            games[i] = game;
            i++;
        }

        System.out.println(Arrays.toString(games));
    }

    private void initTimeline(String path, ObjectMapper objectMapper) throws IOException {
        Node timeline = new Node("Timeline");
        graph.addNode(timeline);

        Node present = new Node("Present");
        graph.addNode(present);
        new Edge(present, timeline, "is");

        Node past = new Node("Past");
        graph.addNode(past);
        new Edge(past, timeline, "is");

        Node isu = new Node("Isu");
        graph.addNode(isu);
        new Edge(isu, timeline, "is");

        File file = new File(path + "timelines.json");
        JsonNode jsonNode = objectMapper.readTree(file);
        for (Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> nodes = it.next();
            ETimelineType type = ETimelineType.valueOf(nodes.getKey());
            for (JsonNode node : nodes.getValue()) {
                Timeline t = objectMapper.treeToValue(node, Timeline.class);
                t.setType(type);
                graph.addNode(t);
                switch (type) {
                    case Present:
                        new Edge(t, present, "is");
                        break;
                    case Past:
                        new Edge(t, past, "is");
                        break;
                    case Isu:
                        new Edge(t, isu, "is");
                        break;
                }
            }
        }
    }
}
