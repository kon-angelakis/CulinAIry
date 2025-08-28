package com.kangel.thesis.aipowered_location_advisor.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SMSMessage {
    private String to;
    private String subject;
    private String body;
}
