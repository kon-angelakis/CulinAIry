package com.kangel.thesis.aipowered_location_advisor.Services;

import com.kangel.thesis.aipowered_location_advisor.Users.User;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import io.github.cdimascio.dotenv.Dotenv;

public abstract class OTPService {

    private static Dotenv env = Dotenv.load();

    public static void SendOTP(User user) {
        Twilio.init(env.get("TWILIO_SID"), env.get("TWILIO_AUTH_TOKEN"));
        Verification verification =
            Verification.creator(env.get("EMAIL_OTP_SID"), user.getEmail(), "email").create();

        System.out.println(verification.getSid());
    }

    //Verify the OTP code thats been sent to the user's email address
    public static boolean VerifyOTP(User user, String otpCode) {
        Twilio.init(env.get("TWILIO_SID"), env.get("TWILIO_AUTH_TOKEN"));
        
        VerificationCheck verificationCheck = VerificationCheck.creator(
            env.get("EMAIL_OTP_SID")) 
            .setTo(user.getEmail())   
            .setCode(otpCode)          
            .create();

        return "approved".equals(verificationCheck.getStatus());
    }

}
