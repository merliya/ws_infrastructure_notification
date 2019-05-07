package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.ShipmentNotificationEvent;
@RepositoryRestResource(path = "edinotificationevents")
public interface ShipmentNotificationEventRepository extends JpaRepository<ShipmentNotificationEvent, Integer>,
        JpaSpecificationExecutor<ShipmentNotificationEvent>, QueryDslPredicateExecutor<ShipmentNotificationEvent> {

}
