package com.kangel.thesis.aipowered_location_advisor.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AIService {

    private final String API_KEY;
    private WebClient webClient;

    public AIService(Dotenv env) {
        this.API_KEY = env.get("OPENAI_API_KEY");
        webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/threads")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + API_KEY)
                .defaultHeader("OpenAI-Beta", "assistants=v2")
                .build();
    }

    public String CreateThread() {
        Map<String, Object> response = webClient
                .post()
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response.get("id").toString();
    }

    public void AddMessageToThread(String threadId, String message)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode body = mapper.createObjectNode();
        body.put("role", "user");
        body.put("content", message);

        String requestBody = mapper.writeValueAsString(body);

        String response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(String.format("/%s/messages", threadId)).build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String CreateAssistantRun(String threadId, String assistantId) { // thread_Gu2rB99N3fHL9Kuign1nFn5U
        String requestBody = String.format(
                """
                        {
                          "assistant_id": "%s"
                        }
                        """,
                assistantId);

        Map<String, Object> response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(String.format("/%s/runs", threadId)).build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response.get("id").toString();
    }

    public Boolean GetAssistantRunStatus(String threadId, String runId) {
        Map<String, Object> response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(String.format("/%s/runs/%s", threadId, runId))
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return "completed".equals(response.get("status").toString());
    }

    public Map<String, Object> RetrieveAssistantResponse(String threadId)
            throws JsonMappingException, JsonProcessingException {
        String response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(String.format("/%s/messages", threadId)).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        // Get the first message (assumed to be the assistant's)
        JsonNode assistantMessage = root.path("data").get(0);

        // Navigate to: data[0].content[0].text.value
        String messageText = assistantMessage
                .path("content")
                .get(0)
                .path("text")
                .path("value")
                .asText();
        Map<String, Object> responseMap = mapper.readValue(messageText, Map.class);

        return responseMap;
    }

    public void DeleteThread(String threadId) {
        String response = webClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path(String.format("/%s", threadId)).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
