package com.jbhunt.infrastructure.notification.configuration;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;

@Component
public class EUMSecurityHeaderConfiguration implements WebServiceMessageCallback {

	private final PIDCredentials pidCredentials;

	public EUMSecurityHeaderConfiguration(PIDCredentials pidCredentials) {
		this.pidCredentials = pidCredentials;
	}

	private static final QName AUDIT_USER_ID_HEADER = new QName("http://ws.jbhunt.com/audit", "auditUserId");
	private static final QName AUDIT_APPLICATION_NAME_HEADER = new QName("http://ws.jbhunt.com/audit",
			"auditApplicationName");

	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
		SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
		soapHeader.addHeaderElement(AUDIT_USER_ID_HEADER).addAttribute(AUDIT_USER_ID_HEADER,
				pidCredentials.getUsername());
		soapHeader.addHeaderElement(AUDIT_APPLICATION_NAME_HEADER).addAttribute(AUDIT_APPLICATION_NAME_HEADER,
				NotificationApplicationConstants.APPLICATION_NAME_PLATFORM);
	}
}
