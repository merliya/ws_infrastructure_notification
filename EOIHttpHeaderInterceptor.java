package com.jbhunt.infrastructure.notification.interceptor;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.properties.EOIProperties;

public class EOIHttpHeaderInterceptor implements ClientHttpRequestInterceptor {

	private static final String PERSON = "person";
	private static final String JBH_EOI_VER = "JBH-EOI-VER";
	private static final String JBH_EOI_PID = "JBH-EOI-PID";
	private static final String JBH_EOI_APPID = "JBH-EOI-APPID";

	private final EOIProperties eoiProperties;

	private String processid;

	@Autowired
	public EOIHttpHeaderInterceptor(PIDCredentials eoiPidCredential, EOIProperties eoiProperties) {
		this.processid = eoiPidCredential.getUsername();
		this.eoiProperties = eoiProperties;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();

		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		headers.add(JBH_EOI_APPID, processid);
		headers.add(JBH_EOI_PID, processid);

		if (StringUtils.contains(request.getURI().getPath(), PERSON)) {
			headers.add(JBH_EOI_VER, eoiProperties.getEmployeeDetailsVersion());
		} else {
			headers.add(JBH_EOI_VER, eoiProperties.getDefaultVersion());
		}
		return execution.execute(request, body);
	}

}
