package org.roadmapBack.service;

import org.roadmapBack.dto.RoadMap;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RoadmapService {

    private final ChatClient chatClient;

    public RoadmapService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public RoadMap generateRoadmap(String userGoal) {
        return chatClient.prompt()
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
    }
}
