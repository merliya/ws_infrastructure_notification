package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.NotificationCategory;

@RepositoryRestResource(path = "notificationcategories")
public interface NotificationCategoryRepository
		extends JpaRepository<NotificationCategory, String>, JpaSpecificationExecutor<NotificationCategory> {

}
