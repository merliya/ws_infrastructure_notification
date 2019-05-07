package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.ShipmentNotificationSubscriptionHold;

@RepositoryRestResource(path = "shipmentnotificationsubscriptionholds")
public interface ShipmentNotificationSubscriptionHoldRepository extends JpaRepository<ShipmentNotificationSubscriptionHold, Integer>,
		JpaSpecificationExecutor<ShipmentNotificationSubscriptionHold> {

}
