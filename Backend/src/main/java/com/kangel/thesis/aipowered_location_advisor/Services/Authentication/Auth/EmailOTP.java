package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Services.Email.VerificationEmailSender;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class EmailOTP implements IOTPService {

    private final Dotenv env;
    private final VerificationEmailSender vEmailSender;

    @Autowired
    public EmailOTP(Dotenv env, VerificationEmailSender vEmailSender) {
        this.env = env;
        this.vEmailSender = vEmailSender;
    }

    @Override
    public void SendOTP(User user) {
        vEmailSender.SendEmail(user);
    }

    @Override
    public boolean VerifyOTP(User user, String otpCode) {
        Twilio.init(env.get("TWILIO_SID"), env.get("TWILIO_AUTH_TOKEN"));
        VerificationCheck verificationCheck = VerificationCheck.creator(
                env.get("EMAIL_OTP_SID"))
                .setTo(user.getEmail())
                .setCode(otpCode)
                .create();

        return "approved".equals(verificationCheck.getStatus());
    }

}
