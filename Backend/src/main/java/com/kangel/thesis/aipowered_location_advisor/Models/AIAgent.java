package com.kangel.thesis.aipowered_location_advisor.Models;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kangel.thesis.aipowered_location_advisor.Services.AIService;

import lombok.Data;

@Data
public class AIAgent {

    private final AIService aiService;
    private String agentID, agentQuery;
    private Map<String, Object> agentResponse;

    public AIAgent(String agentID, String agentQuery, AIService aiService){
        this.agentID = agentID;
        this.agentQuery = agentQuery;
        this.aiService = aiService;
    }

    public Map<String, Object> Run() throws JsonProcessingException, InterruptedException{
        String threadId = aiService.CreateThread();
        aiService.AddMessageToThread(threadId, agentQuery);
        String runId = aiService.CreateAssistantRun(threadId, agentID);
        while(!aiService.GetAssistantRunStatus(threadId, runId)){
            Thread.sleep(500);
        }
        agentResponse = aiService.RetrieveAssistantResponse(threadId);

        aiService.DeleteThread(threadId);
        return agentResponse;
    }

}
