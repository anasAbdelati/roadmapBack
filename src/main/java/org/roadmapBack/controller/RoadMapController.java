package org.roadmapBack.controller;

import org.roadmapBack.config.JwtUtils;
import org.roadmapBack.data.RoadMap;
import org.roadmapBack.data.User;
import org.roadmapBack.service.RoadmapService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class RoadMapController {

    private final RoadmapService roadmapService;

    public RoadMapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @PostMapping("/roadMap/generate")
    public RoadMap generate(@AuthenticationPrincipal User currentUser, @RequestBody String topic) {
        return roadmapService.generateRoadmap(currentUser, topic);
    }

    //TODO - change User class list of roadmaps
    //     - complete roadmap Controller ( add get roadmap by ID && delete roadmaps)
    //     - verify exception handling in new methods
    //     - add unitary tests

    /*@GetMapping("/roadMaps")
    public RoadMap roadmaps(@AuthenticationPrincipal User currentUser, @RequestParam String topic) {
        return roadmapService.generateRoadmap(currentUser, topic);
    }

    @GetMapping("/roadMap/{id}")
    public RoadMap roadmap(@AuthenticationPrincipal User currentUser, @RequestParam String topic) {
        return roadmapService.generateRoadmap(currentUser, topic);
    }*/
}
