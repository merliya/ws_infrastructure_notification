package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import com.jbhunt.infrastructure.notification.entity.NotificationSubscriptionDeliveryMethod;

@RepositoryRestResource(path = "notificationsubscriptiondeliverymethods")
public interface NotificationSubscriptionDeliveryMethodRepository
		extends JpaRepository<NotificationSubscriptionDeliveryMethod, String>,
		JpaSpecificationExecutor<NotificationSubscriptionDeliveryMethod>,
		QueryDslPredicateExecutor<NotificationSubscriptionDeliveryMethod> {

}
