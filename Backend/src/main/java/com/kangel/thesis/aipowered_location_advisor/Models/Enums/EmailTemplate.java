package com.kangel.thesis.aipowered_location_advisor.Models.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailTemplate {
    VERIFY("verification.html", "Culinairy - Verification Required", true),
    THANKYOU("thankyou.html", "Culinairy - Thanks", true);

    private final String path;
    private final String subject;
    private final boolean isHTML;
}
