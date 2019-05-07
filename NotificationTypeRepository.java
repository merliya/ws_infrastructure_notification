package com.jbhunt.infrastructure.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.NotificationType;

@RepositoryRestResource(path = "notificationtypes")
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Integer>,
		JpaSpecificationExecutor<NotificationType>, QueryDslPredicateExecutor<NotificationType> {

	List<NotificationType> findByNotificationCategoryNotificationCategoryCodeAndNotificationSubCategoryNotificationSubCategoryCodeAndNotificationTypeNameContaining(
			@Param("code") List<String> code, @Param("subCode") List<String> subCode, @Param("type") String type);

	List<NotificationType> findByNotificationTypeIDIn(List<Integer> notificationIds);
}
