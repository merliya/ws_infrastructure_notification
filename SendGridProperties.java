package com.jbhunt.infrastructure.notification.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import java.util.List;

@RefreshScope
@Component
@ConfigurationProperties("sendgrid")
@Data
public class SendGridProperties {

    private String emailFromAddress;

    private String emailFromName;

    private String internalEmailToAddress;

    private String externalEmailToAddress;

    private String isTest;

    private String APIToken;

    private String emailSendUrl;

    private List<String> bccTemplates;

    private String orderManagementBccEmailAddress;
}
