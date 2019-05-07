package com.jbhunt.infrastructure.notification.configuration;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "jbhunt.orderManagement.datasource.teamAndSubscription")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(org.apache.commons.dbcp2.BasicDataSource.class).build();
	}

}
