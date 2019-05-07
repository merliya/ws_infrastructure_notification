package com.jbhunt.infrastructure.notification.interceptor;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicAuthInterceptor implements ClientHttpRequestInterceptor {

	private final String username;
	private final String password;

	/**
	 * The username & password are injected from the securePid.
	 * 
	 * @param username
	 * @param password
	 */
	public BasicAuthInterceptor(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		log.info("BasicAuthInterceptor.intercept() is called");
		// Build the auth-header
		final String auth = username + ":" + password;
		final byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
		final String authHeader = "Basic " + new String(encodedAuth);
		// Add the auth-header
		request.getHeaders().add("Authorization", authHeader);
		request.getHeaders().add("usernametoken", getLoggedInUser());
		return execution.execute(request, body);
	}

	/**
	 * To get the logged in userID.
	 * 
	 * @return userId
	 */
	private String getLoggedInUser() {
		Optional<Authentication> authentication = Optional
				.ofNullable(SecurityContextHolder.getContext().getAuthentication());
		return authentication.map(Authentication::getPrincipal).map(obj -> (UserDetails) obj)
				.map(UserDetails::getUsername).orElse(null);
	}
}