package com.usmb.m2.confrerie_des_templier.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmb.m2.confrerie_des_templier.graph.Graph;
import com.usmb.m2.confrerie_des_templier.graph.node.EGameType;
import com.usmb.m2.confrerie_des_templier.graph.node.Game;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Service
public class GraphService {
    private final Graph graph = new Graph();

    @PostConstruct
    public void initialize() {
        try {
            String path = "src/main/resources/";
            ObjectMapper objectMapper = new ObjectMapper();
            this.initGames(path, objectMapper);
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
}
