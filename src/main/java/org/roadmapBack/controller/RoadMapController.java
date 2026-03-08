package org.roadmapBack.controller;

import org.roadmapBack.data.RoadMap;
import org.roadmapBack.data.User;
import org.roadmapBack.service.RoadMapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class RoadMapController {

    private final RoadMapService roadmapService;

    public RoadMapController(RoadMapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @PostMapping("/roadMap/generate")
    public ResponseEntity<RoadMap> generate(@AuthenticationPrincipal User currentUser, @RequestBody String topic) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roadmapService.generateRoadmap(currentUser, topic));
    }

    @GetMapping("/roadMap/{id}")
    public ResponseEntity<RoadMap> getRoadMap(@AuthenticationPrincipal User currentUser, @PathVariable String id) {
        return ResponseEntity.ok(roadmapService.getRoadMap(currentUser, id));
    }

    @DeleteMapping("/roadMap/{id}")
    public ResponseEntity<Void> deleteRoadMap(@AuthenticationPrincipal User currentUser, @PathVariable String id) {
        roadmapService.deleteRoadMap(currentUser, id);
        return ResponseEntity.noContent().build();
    }
}
