package com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email;

import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.EmailMessage;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.EmailTemplate;

@Service
public class EmailFactory {
    private final TemplateLoader templateLoader;

    public EmailFactory(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    public EmailMessage Create(EmailTemplate template, String to, Map<String, String> variables) {
        String raw = templateLoader.load(template.getPath());
        String body = new StringSubstitutor(variables).replace(raw);

        return new EmailMessage(
                to,
                template.getSubject(),
                body,
                template.isHTML());
    }
}
