package com.usmb.m2.confrerie_des_templier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmb.m2.confrerie_des_templier.graph.Graph;
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
            File file = new File(path + "game.json");
            Game[] games = objectMapper.readValue(file, Game[].class);
            //System.out.println(Arrays.toString(games));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
