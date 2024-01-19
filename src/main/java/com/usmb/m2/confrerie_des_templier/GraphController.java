package com.usmb.m2.confrerie_des_templier;

import com.usmb.m2.confrerie_des_templier.service.GraphService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
@CrossOrigin(origins = "*")
public class GraphController {
    private final int MAX_DEPTH = 3;
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping
    public GraphDTO getGraph() {
        return graphService.getGraph("Jeu vidéo", MAX_DEPTH);
    }

    @GetMapping("/{start}")
    public GraphDTO getGraphFromStart(@PathVariable(name = "start") String start) {
        return graphService.getGraph(start.replace('_', ' '), MAX_DEPTH);
    }


}
