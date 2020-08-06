package com.concrete.poletime.utils.properties;

public interface ApplicationProperties {
    String getJwtSecretKey();
    void setJwtSecretKey(String jwtSecretKey);
    int getJwtExpiration();
    void setJwtExpiration(int jwtExpiration);
    String getMailSenderHost();
    void setMailSenderHost(String mailSenderHost);
    int getMailSenderPort();
    void setMailSenderPort(int mailSenderPort);
    String getMailSenderUsername();
    void setMailSenderUsername(String mailSenderUsername);
    String getMailSenderPassword();
    void setMailSenderPassword(String mailSenderPassword);
    String getApplicationUrl();
    void setApplicationUrl(String applicationUrl);
}
