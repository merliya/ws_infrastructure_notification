package com.jbhunt.infrastructure.notification.handler;

import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;
import com.jbhunt.infrastructure.notification.entity.TradingPartnerAssociatedPartyLocation;

public interface CreationEDINotificationSubscription<T> {

	/**
	 * @return Event
	 */
	public EDINotificationSubscription fetchEDINotificationSubscription();

	public Iterable<TradingPartnerAssociatedPartyLocation> fetchTradingPartnerAssociatedPartyLocations();
}