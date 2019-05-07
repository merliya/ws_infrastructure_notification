package com.jbhunt.infrastructure.notification.handler;

import java.util.List;

import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;
import com.jbhunt.infrastructure.notification.entity.TradingPartnerAssociatedPartyLocation;

public class EDINotificationSubscriptionEvent
		implements CreationEDINotificationSubscription<EDINotificationSubscription> {

	private EDINotificationSubscription ediNotificationSubscription;
	private Iterable<TradingPartnerAssociatedPartyLocation> tradingPartnerAssociatedPartyLocationList;

	public EDINotificationSubscriptionEvent(EDINotificationSubscription ediNotificationSubscription,
			Iterable<TradingPartnerAssociatedPartyLocation> tradingPartnerAssociatedPartyLocationList) {
		this.ediNotificationSubscription = ediNotificationSubscription;
		this.tradingPartnerAssociatedPartyLocationList = tradingPartnerAssociatedPartyLocationList;
	}
	
	@Override
	public EDINotificationSubscription fetchEDINotificationSubscription() {
		return ediNotificationSubscription;
	}
	
	@Override
	public Iterable<TradingPartnerAssociatedPartyLocation> fetchTradingPartnerAssociatedPartyLocations(){
		return this.tradingPartnerAssociatedPartyLocationList;
	}


}
