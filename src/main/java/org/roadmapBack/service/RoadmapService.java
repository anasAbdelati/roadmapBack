package org.roadmapBack.service;

import org.roadmapBack.data.RoadMap;
import org.roadmapBack.data.User;
import org.roadmapBack.exceptions.RoadmapGenerationException;
import org.roadmapBack.repository.RoadmapRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoadmapService {

    private final ChatClient chatClient;
    private final UserService userService;
    private final RoadmapRepository roadmapRepository;

    public RoadmapService(ChatClient.Builder builder, UserService userService, RoadmapRepository roadmapRepository) {
        this.chatClient = builder.build();
        this.userService = userService;
        this.roadmapRepository = roadmapRepository;
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
        final var savedRoadmap=roadmapRepository.save(generatedRoadMap);
        currentUser.addRoadmapId(savedRoadmap.getId());
        userService.saveUser(currentUser);
        return savedRoadmap;
    }
}