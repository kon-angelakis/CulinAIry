package com.kangel.thesis.aipowered_location_advisor.Services.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Users.User;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class VerificationEmailSender implements IEmailService {

    private final Dotenv env;

    @Autowired
    public VerificationEmailSender(Dotenv env) {
        this.env = env;
    }

    @Override
    public void SendEmail(User user) {
        Twilio.init(env.get("TWILIO_SID"), env.get("TWILIO_AUTH_TOKEN"));
        Verification verification = Verification.creator(env.get("EMAIL_OTP_SID"), user.getEmail(), "email").create();

        System.out.println(verification.getSid());
    }

}
