package com.jbhunt.infrastructure.notification.handler;

import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscription;

public interface CreationUserNotificationSubscription<T> {

	/**
	 * @return Event
	 */
	public UserNotificationSubscription fetchUserNotificationSubscription();
}