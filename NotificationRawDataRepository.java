package com.jbhunt.infrastructure.notification.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.NotificationRawData;

@RepositoryRestResource(path = "notificationrawdata")
public interface NotificationRawDataRepository extends JpaRepository<NotificationRawData, Integer>,
		JpaSpecificationExecutor<NotificationRawData>, QueryDslPredicateExecutor<NotificationRawData> {

	List<NotificationRawData> findByApplicationDomainCode(@Param("applicationDomainCode") String applicationDomainCode);

}