package com.kangel.thesis.aipowered_location_advisor.Services.Email;

import java.io.IOException;

import com.kangel.thesis.aipowered_location_advisor.Users.User;

public interface IEmailService {

    void SendEmail(User user) throws IOException;
}
