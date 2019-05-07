package com.jbhunt.infrastructure.notification.handler;

import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscription;

public class UserNotificationSubscriptionEvent
		implements CreationUserNotificationSubscription<UserNotificationSubscription> {

	private UserNotificationSubscription userNotificationSubscription;

	public UserNotificationSubscriptionEvent(UserNotificationSubscription userNotificationSubscription) {
		this.userNotificationSubscription = userNotificationSubscription;
	}

	@Override
	public UserNotificationSubscription fetchUserNotificationSubscription() {
		return userNotificationSubscription;
	}

}
