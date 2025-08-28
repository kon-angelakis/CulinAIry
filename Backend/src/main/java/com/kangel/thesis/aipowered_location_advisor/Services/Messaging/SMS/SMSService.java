package com.kangel.thesis.aipowered_location_advisor.Services.Messaging.SMS;

import com.kangel.thesis.aipowered_location_advisor.Models.SMSMessage;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.IMessagingService;

public interface SMSService extends IMessagingService {
    void SendSMS(SMSMessage request);
}
