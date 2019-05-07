package com.jbhunt.infrastructure.notification.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.jbhunt.infrastructure.notification.entity.NotificationParameter;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationParameterDTO;

@Component
@Mapper(componentModel = "spring")
public interface UserNotificationParameterMapper {

	List<UserNotificationParameterDTO> notificationParametersToUserNotificationParametersDTO(
			List<NotificationParameter> notificationParameters);

	UserNotificationParameterDTO notificationParameterToUserNotificationParameterDTO(
			NotificationParameter notificationParameter);
}
