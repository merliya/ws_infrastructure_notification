package com.jbhunt.infrastructure.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@RefreshScope
@Component
@ConfigurationProperties("notification.sms")
public class NotificationSMSProperties {
    private String url;
    private String loginUser;
    private String password;
    private String sender;
    private String carrier;
    private String nonProdTragetNumber;
}
