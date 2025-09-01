package com.kangel.thesis.aipowered_location_advisor.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Models.Enums.AiProvider;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiSummarizationRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiSummarizationResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Services.AiManager;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiManager aiManager;

    public AiController(AiManager aiManager) {
        this.aiManager = aiManager;
    }

    @PostMapping("/summary")
    public ResponseEntity<?> ReviewSummary(@RequestBody AiSummarizationRequest request) {
        AiRequest<AiSummarizationResponse> aiRequest = new AiRequest<AiSummarizationResponse>(
                request.reviews().toString(), "summarizer", AiSummarizationResponse.class);

        ApiResponse<AiSummarizationResponse> response = aiManager.Call(AiProvider.OPENAI, aiRequest);
        return ResponseEntity.ok(response);
    }

}
