package com.jbhunt.infrastructure.notification.mapper;

import java.util.List;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscription;
import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscriptionCriteria;
import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscriptionDetail;
import com.jbhunt.infrastructure.notification.user.dto.UserDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionCriteriaDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionDetailDTO;

/**
 * User NotificationSubscription Mapper
 * 
 * @author rcon980
 *
 */

@Component
@Mapper(componentModel = "spring")
@DecoratedWith(UserNotificationSubscriptionMapperDecorator.class)
public interface UserNotificationSubscriptionMapper {

	@Mappings({ @Mapping(target = "subscriptionID", source = "userNotificationSubscriptionID"),
			@Mapping(target = "subscriptionDomain", ignore = true),
			@Mapping(target = "effectiveTimestamp", source = "effectiveTimestamp"),
			@Mapping(target = "expirationTimestamp", source = "expirationTimestamp"),
			@Mapping(target = "createdTimestamp", source = "createTimestamp"),
			@Mapping(target = "lastUpdatedTimestamp", source = "lastUpdateTimestamp") })
	UserNotificationSubscriptionDTO userNotificationSubscriptionEntityToDTO(
			UserNotificationSubscription userNotificationSubscription);

	List<UserNotificationSubscriptionCriteriaDTO> userNotificationSubscriptionCriteriaLstEntityToDTO(
			List<UserNotificationSubscriptionCriteria> userNotificationSubscriptionCriteriaList);

	UserNotificationSubscriptionCriteriaDTO userNotificationSubscriptionCriteriaEntityToDTO(
			UserNotificationSubscriptionCriteria userNotificationSubscriptionCriteria);

	List<UserNotificationSubscriptionDetailDTO> userNotificationSubscriptionDetailLstEntityToDTO(
			List<UserNotificationSubscriptionDetail> userNotificationSubscriptionDetailList);

	UserNotificationSubscriptionDetailDTO userNotificationSubscriptionDetailEntityToDTO(
			UserNotificationSubscriptionDetail userNotificationSubscriptionDetail);

	@Mappings({ @Mapping(target = "firstName", ignore = true), @Mapping(target = "lastName", ignore = true),
			@Mapping(target = "jobTitle", ignore = true), @Mapping(target = "contactType", ignore = true),
			@Mapping(target = "companyName", ignore = true), @Mapping(target = "type", ignore = true),
			@Mapping(target = "emailAddress", ignore = true), @Mapping(target = "phoneNumber", ignore = true)

	})
	UserDTO userDTO(UserDTO createdBy);

}
