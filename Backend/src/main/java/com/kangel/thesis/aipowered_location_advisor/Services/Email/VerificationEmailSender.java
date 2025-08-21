package com.kangel.thesis.aipowered_location_advisor.Services.Email;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class VerificationEmailSender implements IEmailService {

    private final Dotenv env;

    public VerificationEmailSender(Dotenv env) {
        this.env = env;
    }

    @Override
    public void SendEmail(String emailTo) {
        Twilio.init(env.get("TWILIO_SID"), env.get("TWILIO_AUTH_TOKEN"));
        Verification verification = Verification.creator(env.get("EMAIL_OTP_SID"), emailTo, "email").create();

        System.out.println(verification.getSid() + String.format(" Verification Email sent to %s", emailTo));
    }

}
