package com.jbhunt.infrastructure.notification.helper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;
import com.jbhunt.security.boot.autoconfig.enterprisesecurity.user.AuditUserInfo;
import com.jbhunt.security.boot.autoconfig.enterprisesecurity.user.JbhUser;
import com.jbhunt.security.boot.autoconfig.enterprisesecurity.wrapper.ESAuthenticationWrapper;
import com.jbhunt.security.boot.autoconfig.enterprisesecurity.JBHAuthorizationAccessLevel;

@Component
public class SecurityContextPopulator {

	/*
	 * Private Constructor for SecurityContextPopulator
	 */
	private SecurityContextPopulator() {
		// Intentionally left blank
	}

	/**
	 * setSecurityContextAuthentication method populates the audit info in
	 * JbhUser object.
	 */
	public static void setSecurityContextAuthentication(PIDCredentials pidCredentials) {
		AuditUserInfo auditUserInfo = fetchAuditFields(pidCredentials);
		JbhUser jbhUser = new JbhUser(pidCredentials.getUsername(), auditUserInfo);
		Authentication auth = new UsernamePasswordAuthenticationToken(jbhUser, pidCredentials.getPassword());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(new ESAuthenticationWrapper(auth, JBHAuthorizationAccessLevel.READ_ONLY.getAccessLevel()));
	}

	/*
	 * fetchAuditFields method fetches the audit fields from pidCredentials
	 */
	private static AuditUserInfo fetchAuditFields(PIDCredentials pidCredentials) {
		AuditUserInfo auditUserInfo = new AuditUserInfo(pidCredentials.getUsername(), pidCredentials.getUsername());
		auditUserInfo.setAuthenticatedUserDisplayName(NotificationApplicationConstants.APPLICATION_NAME_PLATFORM);
		return auditUserInfo;
	}

}
