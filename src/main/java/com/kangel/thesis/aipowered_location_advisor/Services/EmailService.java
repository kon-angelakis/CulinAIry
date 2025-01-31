package com.kangel.thesis.aipowered_location_advisor.Services;

import com.kangel.thesis.aipowered_location_advisor.Users.User;
import java.io.IOException;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.github.cdimascio.dotenv.Dotenv;
import com.sendgrid.*;

//Handles the email sending service
public abstract class EmailService {

    private static Dotenv env = Dotenv.load();

    public static void SendRegistrationEmail(User user) throws IOException {
        Email from = new Email(env.get("EMAIL_SENDER"));
        Email to = new Email(user.getEmail());

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(env.get("EMAIL_TEMPLATE_REGISTRATION"));
        // Set the emails context to match the template given
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("User", user.getFirstName());
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(env.get("EMAIL_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
        } catch (IOException ex) {
            throw ex;
        }
    }

}
