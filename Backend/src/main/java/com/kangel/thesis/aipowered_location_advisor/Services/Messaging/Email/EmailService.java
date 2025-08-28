package com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email;

import com.kangel.thesis.aipowered_location_advisor.Models.EmailMessage;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.IMessagingService;

public interface EmailService extends IMessagingService {
    void SendMail(EmailMessage request);
}
