package com.kangel.thesis.aipowered_location_advisor.Services.AI;

import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Services.AI.Records.PlaceRecommendation;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AIController {

    private final ChatClient chatClient;
    
    @Autowired
    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/api/ai/query")
    public PlaceRecommendation postMethodName(@RequestBody Map<String, String> payload) {
        return chatClient.prompt()
        .system("Your role is to summarize into 5-10 words or less the user query so it can be later used as a google maps search query.")
        .user(payload.get("search_query"))
        //Returns an ai response in the form of a record
        .call().entity(PlaceRecommendation.class);
    }
    

}
