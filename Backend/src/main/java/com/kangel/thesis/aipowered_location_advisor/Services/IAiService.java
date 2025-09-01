package com.kangel.thesis.aipowered_location_advisor.Services;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiRequest;

public interface IAiService {
    // Using spring ai call whatever endpoint(openai/anthropic etc) and return a
    // record structured response
    <T> T Call(AiRequest<T> request);
}
