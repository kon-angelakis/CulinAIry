package com.kangel.thesis.aipowered_location_advisor.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiRequest;

@Service
public class OpenAiService implements IAiService {

    private final ChatClient chatClient;
    private final PromptService promptService;

    public OpenAiService(ChatClient.Builder builder, PromptService promptService) {
        this.chatClient = builder.build();
        this.promptService = promptService;
    }

    @Override
    public <T> T Call(AiRequest<T> request) {
        UserMessage userMessage = new UserMessage(request.userPrompt());
        SystemMessage systemMessage = new SystemMessage(promptService.GetPrompt(request.instructionsPromptLocation()));
        Prompt prompt = new Prompt(systemMessage, userMessage);
        T response = chatClient.prompt(prompt).call().entity(request.responseClass());

        return response;

    }

}
