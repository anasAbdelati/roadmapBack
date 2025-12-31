package org.roadmapBack.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/roadMap")
public class RoadMapController {

    private final ChatModel chat;

    public RoadMapController(ChatModel chat) {
        this.chat = chat;
    }

    @GetMapping("/call")
    public String testMethod(@RequestParam(defaultValue = "Is the API working?") String message){
        return chat.call(message);
    }
}
