package com.concrete.poletime.email;

import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailSenderService {

    private MailSender javaMailSender;
    protected ApplicationProperties properties;

    @Autowired
    public EmailSenderService(MailSender javaMailSender, ApplicationProperties properties) {
        this.javaMailSender = javaMailSender;
        this.properties = properties;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public void setConfirmationEmail(PoleUser user, ConfirmationToken confirmationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(properties.getMailSenderUsername());
        mailMessage.setText("To confirm your account, please click here : " +
                properties.getApplicationUrl() +
                "/api/user/confirm-account?token=" +
                confirmationToken.getConfirmationToken());
        sendEmail(mailMessage);
    }
}