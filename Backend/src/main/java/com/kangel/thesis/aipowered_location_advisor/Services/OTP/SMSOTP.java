package com.kangel.thesis.aipowered_location_advisor.Services.OTP;

import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Users.User;

@Service
public class SMSOTP implements IOTPService {

    @Override
    public void SendOTP(User user) {

    }

    @Override
    public boolean VerifyOTP(User user, String otpCode) {

        return true;
    }

}
