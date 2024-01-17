package com.usmb.m2.confrerie_des_templier.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmb.m2.confrerie_des_templier.graph.Graph;
import com.usmb.m2.confrerie_des_templier.graph.node.EGameType;
import com.usmb.m2.confrerie_des_templier.graph.node.Game;
import com.usmb.m2.confrerie_des_templier.graph.node.Location;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

@Service
public class GraphService {
    private final Graph graph = new Graph();

    @PostConstruct
    public void initialize() {
        try {
            String path = "src/main/resources/";
            ObjectMapper objectMapper = new ObjectMapper();
            this.initGames(path, objectMapper);
            this.initLocations(path, objectMapper);
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

    private void initLocations(String path, ObjectMapper objectMapper) throws IOException {
        File file = new File(path + "locations.json");
        JsonNode locationNodes = objectMapper.readTree(file);
        Location[] locations = new Location[locationNodes.size()];
        int i = 0;
        for (JsonNode node : locationNodes) {
            Location location = new Location();
            location.setName(node.get("name").asText());
            JsonNode imagesNode = node.get("images");
            Iterator<String> iterator = imagesNode.fieldNames();
            iterator.forEachRemaining(e -> location.addImage(e, imagesNode.get(e).asText()));
            this.graph.addNode(location);
            locations[i] = location;
            i++;
        }
        System.out.println(Arrays.toString(locations));
    }

    private void initSupports(String path, ObjectMapper objectMapper) throws IOException {

    }
}
