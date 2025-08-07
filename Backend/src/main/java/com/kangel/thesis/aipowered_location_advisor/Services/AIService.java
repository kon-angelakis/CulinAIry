package com.kangel.thesis.aipowered_location_advisor.Services;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.cdimascio.dotenv.Dotenv;


@Service
public class AIService {

  private Dotenv env;
  private final String API_KEY;
  @Autowired
    public AIService(Dotenv env) {
        this.env = env;
        this.API_KEY = env.get("OPENAI_API_KEY");
    }

    public String CreateThread() {
        
    Map<String, Object> response = WebClient.create()
        .post()
        .uri("https://api.openai.com/v1/threads")
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + API_KEY)
        .header("OpenAI-Beta", "assistants=v2")
        .retrieve()
        .bodyToMono(Map.class)
        .block(); 

        return response.get("id").toString();
    }

    public void AddMessageToThread(String threadId, String message) throws JsonProcessingException{
      

      ObjectMapper mapper = new ObjectMapper();
        ObjectNode body = mapper.createObjectNode();
        body.put("role", "user");
        body.put("content", message);

String requestBody = mapper.writeValueAsString(body);

        String response = WebClient.create()
        .post()
        .uri("https://api.openai.com/v1/threads/"+threadId+"/messages")
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + API_KEY)
        .header("OpenAI-Beta", "assistants=v2")
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block(); 

    }
    
    public String CreateAssistantRun(String threadId, String assistantId) { //thread_Gu2rB99N3fHL9Kuign1nFn5U

      String requestBody = String.format("""
        {
          "assistant_id": "%s"
        }
        """, assistantId);

        Map<String, Object> response = WebClient.create()
        .post()
        .uri("https://api.openai.com/v1/threads/"+threadId+"/runs")
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + API_KEY)
        .header("OpenAI-Beta", "assistants=v2")
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(Map.class)
        .block(); 

        return response.get("id").toString();
    }

    public Boolean GetAssistantRunStatus(String threadId, String runId) {
        Map<String, Object> response = WebClient.create()
        .get()
        .uri("https://api.openai.com/v1/threads/"+threadId+"/runs/"+runId)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + API_KEY)
        .header("OpenAI-Beta", "assistants=v2")
        .retrieve()
        .bodyToMono(Map.class)
        .block(); 

        return "completed".equals(response.get("status").toString());
    }

    public Map<String, Object> RetrieveAssistantResponse(String threadId) throws JsonMappingException, JsonProcessingException {
        String response = WebClient.create()
        .get()
        .uri("https://api.openai.com/v1/threads/"+threadId+"/messages")
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + API_KEY)
        .header("OpenAI-Beta", "assistants=v2")
        .retrieve()
        .bodyToMono(String.class)
        .block(); 
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        // Get the first message (assumed to be the assistant's)
        JsonNode assistantMessage = root.path("data").get(0);

        // Navigate to: data[0].content[0].text.value
        String messageText = assistantMessage
            .path("content").get(0)
            .path("text")
            .path("value")
            .asText();
        Map<String, Object> responseMap = mapper.readValue(messageText, Map.class);

        return responseMap;
    }

    public void DeleteThread(String threadId) {
        String response = WebClient.create()
        .delete()
        .uri("https://api.openai.com/v1/threads/"+threadId)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + API_KEY)
        .header("OpenAI-Beta", "assistants=v2")
        .retrieve()
        .bodyToMono(String.class)
        .block(); 

    }
    
}
