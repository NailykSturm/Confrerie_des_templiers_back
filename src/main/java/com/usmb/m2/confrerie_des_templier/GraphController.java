package com.usmb.m2.confrerie_des_templier;

import com.usmb.m2.confrerie_des_templier.service.GraphService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/graph")
public class GraphController {
    private final int MAX_DEPTH = 3;
    private final int MAX_NODES = 50;
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping
    public GraphDTO getGraph() {
        return graphService.getGraph("Assassin's Creed", MAX_DEPTH);
    }

    @GetMapping("/{start}")
    public GraphDTO getGraphFromStart(@PathVariable(name = "start") String start) {
        return graphService.getGraph(start.replace('_', ' '), MAX_DEPTH);
    }


}
