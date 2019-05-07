package com.jbhunt.infrastructure.notification.interceptor;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.jbhunt.infrastructure.notification.constants.ExactTargetConstants;
import com.jbhunt.infrastructure.notification.service.ExactTargetOAuthAccessTokenService;

public class ExactTargetHttpHeaderInterceptor implements ClientHttpRequestInterceptor {

	private final ExactTargetOAuthAccessTokenService etOAuthAccessTokenService;

	public ExactTargetHttpHeaderInterceptor(ExactTargetOAuthAccessTokenService etOAuthAccessTokenService) {
		this.etOAuthAccessTokenService = etOAuthAccessTokenService;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		headers.add(ExactTargetConstants.AUTHORIZATION,
				ExactTargetConstants.AUTHORIZATION_TYPE_BEARER + etOAuthAccessTokenService.getETOAuthAccessToken());
		return execution.execute(request, body);
	}

}
