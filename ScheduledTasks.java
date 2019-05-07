package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.jbhunt.infrastructure.notification.service.EmailNotificationDeliveryRecordService;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "exacttarget", name = "deliveryLogTaskEnabled", havingValue = "true", matchIfMissing = true)
public class ScheduledTasks {

	private final EmailNotificationDeliveryRecordService emailNotificationDeliveryRecordService;

	public ScheduledTasks(EmailNotificationDeliveryRecordService emailNotificationDeliveryRecordService) {
		this.emailNotificationDeliveryRecordService = emailNotificationDeliveryRecordService;
	}

	@Scheduled(fixedDelayString = "${schedule.exactTarget.deliveryData}",  initialDelayString = "${schedule.exactTarget.initialDelay}")
	public void employeeSubordinatesRefresh() {
		emailNotificationDeliveryRecordService.fetchQueuedMessageDeliveryRecord();
	}

}
