package com.jbhunt.infrastructure.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@RefreshScope
@Component
@ConfigurationProperties("genesys")
public class GenesysProperties {

	private String sftpRemotehost;
	
	private Integer sftpPort;
	
	private String sftpUsername;
	
	private String sftpPassword;
	
	private String inDirectory;
	
	private String outDirectory;
	
	private String csvRecordTemplate;
	
	private String csvHeaders;
	
	private String contactHeaders;

}
