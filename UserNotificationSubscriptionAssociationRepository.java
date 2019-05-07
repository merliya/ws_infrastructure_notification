package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscriptionAssociation;

@RepositoryRestResource(path = "usernotificationsubscriptionassociations")
public interface UserNotificationSubscriptionAssociationRepository
		extends JpaRepository<UserNotificationSubscriptionAssociation, Integer> {

}
