package com.kangel.thesis.aipowered_location_advisor.Services.Email;

import java.io.IOException;

import com.kangel.thesis.aipowered_location_advisor.Models.User;

public interface IEmailService {

    default void SendEmail(String emailTo) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    };

    default void SendEmail(User user) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    };
}
