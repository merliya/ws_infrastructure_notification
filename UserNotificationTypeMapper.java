package com.jbhunt.infrastructure.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import com.jbhunt.infrastructure.notification.entity.NotificationType;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationTypeDTO;

@Component
@Mapper(componentModel = "spring")
public interface UserNotificationTypeMapper {

	@Mappings({ @Mapping(target = "notificationCategory", source = "notificationCategory.notificationCategoryCode"),
			@Mapping(target = "notificationSubCategory", source = "notificationSubCategory.notificationSubCategoryCode") })
	UserNotificationTypeDTO notificationTypeToUserNotificationTypeDTO(NotificationType notificationType);

}
