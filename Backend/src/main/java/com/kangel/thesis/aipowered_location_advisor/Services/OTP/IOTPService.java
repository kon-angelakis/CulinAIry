package com.kangel.thesis.aipowered_location_advisor.Services.OTP;

import com.kangel.thesis.aipowered_location_advisor.Users.User;

public interface IOTPService {

    void SendOTP(User user);

    boolean VerifyOTP(User user, String otpCode);
}
