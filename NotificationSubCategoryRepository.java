package com.jbhunt.infrastructure.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.NotificationSubCategory;

@RepositoryRestResource(path = "notificationsubcategories")
public interface NotificationSubCategoryRepository
		extends JpaRepository<NotificationSubCategory, String>, JpaSpecificationExecutor<NotificationSubCategory> {

	List<NotificationSubCategory> findByNotificationCategoryNotificationCategoryDescription(
			@Param("notificationCategoryDescription") String notificationCategoryDescription);

	List<NotificationSubCategory> findByNotificationCategoryNotificationCategoryDescriptionIn(
			@Param("notificationCategoryDescriptionList") List<String> notificationCategoryDescriptionList);

}
