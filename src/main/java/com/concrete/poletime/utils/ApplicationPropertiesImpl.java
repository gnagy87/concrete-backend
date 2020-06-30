package com.concrete.poletime.utils;

import com.sun.istack.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "concrete.backend")
public class ApplicationPropertiesImpl implements ApplicationProperties {
    @NotNull private String jwtSecretKey;
    @NotNull private int jwtExpiration;
    @NotNull private String mailSenderHost;
    @NotNull private int mailSenderPort;
    @NotNull private String mailSenderUsername;
    @NotNull private String mailSenderPassword;
    @NotNull private String applicationUrl;

    @Override
    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    @Override
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    public int getJwtExpiration() {
        return jwtExpiration;
    }

    @Override
    public void setJwtExpiration(int jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    @Override
    public String getMailSenderHost() {
        return mailSenderHost;
    }

    @Override
    public void setMailSenderHost(String mailSenderHost) {
        this.mailSenderHost = mailSenderHost;
    }

    @Override
    public int getMailSenderPort() {
        return mailSenderPort;
    }

    @Override
    public void setMailSenderPort(int mailSenderPort) {
        this.mailSenderPort = mailSenderPort;
    }

    @Override
    public String getMailSenderUsername() {
        return mailSenderUsername;
    }

    @Override
    public void setMailSenderUsername(String mailSenderUsername) {
        this.mailSenderUsername = mailSenderUsername;
    }

    @Override
    public String getMailSenderPassword() {
        return mailSenderPassword;
    }

    @Override
    public void setMailSenderPassword(String mailSenderPassword) {
        this.mailSenderPassword = mailSenderPassword;
    }

    @Override
    public String getApplicationUrl() {
        return applicationUrl;
    }

    @Override
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
}
