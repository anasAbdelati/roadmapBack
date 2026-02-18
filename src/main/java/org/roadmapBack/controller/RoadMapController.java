package org.roadmapBack.controller;

import org.roadmapBack.data.RoadMap;
import org.roadmapBack.service.RoadmapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/roadMap")
public class RoadMapController {

    private final RoadmapService roadmapService;

    public RoadMapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @GetMapping("/generate")
    public RoadMap generate(@RequestParam String topic) {
        return roadmapService.generateRoadmap(topic);
    }
}
