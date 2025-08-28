package com.kangel.thesis.aipowered_location_advisor.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmailMessage {
    private String to;
    private String subject;
    private String body;
    private boolean html;
}
