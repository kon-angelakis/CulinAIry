package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth;

import com.kangel.thesis.aipowered_location_advisor.Models.User;

public interface IOTPService {

    void SendOTP(User user);

    boolean VerifyOTP(User user, String otpCode);
}
