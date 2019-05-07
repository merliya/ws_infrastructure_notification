package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.NotificationRawDataLogAssociation;

@RepositoryRestResource(path = "notificationrawdatalogassociations")
public interface NotificationRawDataLogAssociationRepository
		extends JpaRepository<NotificationRawDataLogAssociation, Integer>,
		JpaSpecificationExecutor<NotificationRawDataLogAssociation>,
		QueryDslPredicateExecutor<NotificationRawDataLogAssociation> {

}