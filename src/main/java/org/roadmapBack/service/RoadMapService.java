package org.roadmapBack.service;

import org.roadmapBack.data.RoadMap;
import org.roadmapBack.data.User;
import org.roadmapBack.exceptions.RoadmapGenerationException;
import org.roadmapBack.exceptions.RoadmapNotFoundException;
import org.roadmapBack.repository.RoadMapRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoadMapService {

    private final ChatClient chatClient;
    private final UserService userService;
    private final RoadMapRepository roadMapRepository;

    public RoadMapService(ChatClient.Builder builder, UserService userService, RoadMapRepository roadMapRepository) {
        this.chatClient = builder.build();
        this.userService = userService;
        this.roadMapRepository = roadMapRepository;
    }

    @Transactional
    public RoadMap generateRoadmap(User currentUser, String userGoal) {
        final var generatedRoadMap= chatClient.prompt()
                .system("""
                You are a world-class Career and Educational Architect.
                Your task is to build a detailed, logical learning path.
                RULES:
                1. Logic: Order nodes from 'Foundations' to 'Advanced'.
                2. Detail: Descriptions must be professional and highly actionable.
                3. Structure: Use childrenIds to link steps (e.g., Step A leads to Step B).
                4. Format: Output ONLY valid JSON. No conversational text or markdown blocks.
                """)
                .user(userGoal)
                .call()
                .entity(RoadMap.class);
        if (generatedRoadMap == null){
            throw new RoadmapGenerationException("Roadmap generation failed try again later ");
        }
        generatedRoadMap.setId(null);
        final var savedRoadmap=roadMapRepository.save(generatedRoadMap);
        currentUser.addRoadMapId(savedRoadmap.getId(), savedRoadmap.getTopic());
        userService.saveUser(currentUser);
        return savedRoadmap;
    }

    public RoadMap getRoadMap(User currentUser, String id) {
        if(!currentUser.hasRoadmap(id)){
            throw new RoadmapNotFoundException("RoadMap not found");
        }
        return roadMapRepository.findRoadMapById(id);
    }

    public void deleteRoadMap(User currentUser, String id) {
        if(!currentUser.hasRoadmap(id)){
            throw new RoadmapNotFoundException("RoadMap not found");
        }
        roadMapRepository.deleteRoadMapById(id);
        currentUser.deleteRoadMap(id);
        userService.saveUser(currentUser);
    }
}