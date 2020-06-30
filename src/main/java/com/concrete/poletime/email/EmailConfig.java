package com.concrete.poletime.email;

import com.concrete.poletime.utils.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.concrete.poletime")
public class EmailConfig {

    private ApplicationProperties properties;

    @Autowired
    public EmailConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "mailSender")
    public MailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(properties.getMailSenderHost());
        javaMailSender.setPort(properties.getMailSenderPort());
        javaMailSender.setProtocol("smtp");
        javaMailSender.setUsername(properties.getMailSenderUsername());
        javaMailSender.setPassword(properties.getMailSenderPassword());
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.ssl.trust", properties.getMailSenderHost());
        mailProperties.put("mail.smtp.debug", "true");
        javaMailSender.setJavaMailProperties(mailProperties);
        return javaMailSender;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }
}
