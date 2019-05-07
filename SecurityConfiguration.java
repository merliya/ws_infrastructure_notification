package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.jbhunt.security.boot.autoconfig.enterprisesecurity.AuthorizationConfiguration;
import com.jbhunt.security.boot.autoconfig.enterprisesecurity.filter.AuthorizationFilter;

/**
 * Security configuration for order service.
 *
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AuthorizationConfiguration authorizationConfiguration;

	public SecurityConfiguration(AuthorizationConfiguration authorizationConfiguration) {
		this.authorizationConfiguration = authorizationConfiguration;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic();
		http.authorizeRequests().anyRequest().authenticated();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterAfter(authorizationConfiguration.getAuthorizationFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(authorizationConfiguration.getSwitchUserFilter(), AuthorizationFilter.class);
	}
}
