package com.jbhunt.infrastructure.notification.interceptor;

import com.jbhunt.infrastructure.notification.constants.ExactTargetConstants;
import com.jbhunt.infrastructure.notification.properties.SendGridProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Arrays;

public class SendGridHttpHeaderInterceptor implements ClientHttpRequestInterceptor {

    private final SendGridProperties sendGridProperties;

    public SendGridHttpHeaderInterceptor(SendGridProperties sendGridProperties) {
        this.sendGridProperties = sendGridProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(ExactTargetConstants.AUTHORIZATION,
                ExactTargetConstants.AUTHORIZATION_TYPE_BEARER + sendGridProperties.getAPIToken());
        return execution.execute(request, body);
    }
}
