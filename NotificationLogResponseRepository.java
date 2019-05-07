package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.NotificationLogResponse;

@RepositoryRestResource(path = "/notificationlogresponses")
public interface NotificationLogResponseRepository extends JpaRepository<NotificationLogResponse, Integer>,
		JpaSpecificationExecutor<NotificationLogResponse>, QueryDslPredicateExecutor<NotificationLogResponse> {

}