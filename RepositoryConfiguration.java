package com.jbhunt.infrastructure.notification.configuration;

import com.jbhunt.infrastructure.notification.entity.*;
import com.jbhunt.infrastructure.notification.repository.InlineUserNotificationSubscriptionDetailProjection;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(NotificationLog.class);
		config.exposeIdsFor(NotificationCategory.class);
		config.exposeIdsFor(NotificationSubCategory.class);
		config.exposeIdsFor(UserNotificationSubscription.class);
		config.exposeIdsFor(EDINotificationSubscription.class);
		config.exposeIdsFor(ShipmentNotificationStopType.class);
		config.exposeIdsFor(ShipmentNotificationEventType.class);
		config.exposeIdsFor(UserNotificationSubscriptionDetail.class);
		config.getProjectionConfiguration().addProjection(InlineUserNotificationSubscriptionDetailProjection.class);
	}
}
