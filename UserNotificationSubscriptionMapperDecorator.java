package com.jbhunt.infrastructure.notification.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jbhunt.infrastructure.usernotification.subscription.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jbhunt.infrastructure.notification.entity.NotificationParameter;
import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscription;
import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscriptionAssociation;
import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscriptionCriteria;
import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscriptionDetail;
import com.jbhunt.infrastructure.notification.user.dto.UserDTO;

/**
 * UserNotificationSubscriptionMapper Decorator
 * 
 * @author rcon980
 *
 */
public abstract class UserNotificationSubscriptionMapperDecorator implements UserNotificationSubscriptionMapper {

	@Autowired
	@Qualifier("delegate")
	private UserNotificationSubscriptionMapper delegate;

	public UserNotificationSubscriptionDTO userNotificationSubscriptionEntityToDTO(
			UserNotificationSubscription userNotificationSubscription) {
		UserNotificationSubscriptionDTO userNotificationSubscriptionDTO = delegate
				.userNotificationSubscriptionEntityToDTO(userNotificationSubscription);
		if (Objects.nonNull(userNotificationSubscription)) {
			List<UserNotificationSubscriptionAssociation> userNotificationSubAssociationLst = userNotificationSubscription
					.getUserNotificationSubscriptionAssociations();
			if (CollectionUtils.isNotEmpty(userNotificationSubAssociationLst)) {
				List<String> notificationTypeLst = new ArrayList<>();
				userNotificationSubAssociationLst.forEach(userNotificationSubAssociation -> {
					userNotificationSubscriptionDTO.setNotificationSubscriptionCategory(userNotificationSubAssociation
							.getNotificationType().getNotificationCategory().getNotificationCategoryDescription());
					userNotificationSubscriptionDTO
							.setNotificationSubscriptionSubCategory(userNotificationSubAssociation.getNotificationType()
									.getNotificationSubCategory().getNotificationSubCategoryDescription());
					userNotificationSubscriptionDTO.setSubscriptionDomain(userNotificationSubAssociation
							.getNotificationType().getNotificationCategory().getApplicationDomainCode());

					if (userNotificationSubAssociation.getNotificationType() != null
							&& userNotificationSubAssociation.getNotificationType().getNotificationTypeName() != null) {
						notificationTypeLst
								.add(userNotificationSubAssociation.getNotificationType().getNotificationTypeName());
						userNotificationSubscriptionDTO.setNotificationSubscriptionTypes(notificationTypeLst);
					}
				});
			}
			userNotificationSubscriptionCriteriaLstEntityToDTO(userNotificationSubscription,
					userNotificationSubscriptionDTO);
			userNotificationSubscriptionDetailLstEntityToDTO(userNotificationSubscription,
					userNotificationSubscriptionDTO);
			userNotificationSubscriptionDTO.setDomainCode(userNotificationSubscription.getApplication().getDomainCode());
			userNotificationSubscriptionDTO.setCreatedBy(createdByDTODetails(userNotificationSubscription));
			userNotificationSubscriptionDTO.setLastUpdatedBy(updatedByDTODetails(userNotificationSubscription));
		}
		return userNotificationSubscriptionDTO;
	}

	private static UserDTO createdByDTODetails(UserNotificationSubscription userSubscription) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userSubscription.getCreateUserId());
		userDTO.setPreferredName(userSubscription.getCreateProgramName());
		return userDTO;
	}

	private static UserDTO updatedByDTODetails(UserNotificationSubscription userSubscription) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userSubscription.getLastUpdateUserId());
		userDTO.setPreferredName(userSubscription.getLastUpdateProgramName());
		return userDTO;
	}

	private static UserNotificationSubscriptionDTO userNotificationSubscriptionCriteriaLstEntityToDTO(
			UserNotificationSubscription userNotificationSubscription,
			UserNotificationSubscriptionDTO userNotificationSubscriptionDTO) {
		List<UserNotificationSubscriptionCriteria> userNotificationSubscriptionCriteriaEntityLst = userNotificationSubscription
				.getUserNotificationSubscriptionCriterias();
		if (CollectionUtils.isNotEmpty(userNotificationSubscriptionCriteriaEntityLst)) {
			List<UserNotificationSubscriptionCriteriaDTO> userNotificationSubscriptionCriteriaDTOLst = new ArrayList<>();
			Map<String, List<UserNotificationSubscriptionCriteria>> groupByPrameterValueMap = userNotificationSubscriptionCriteriaEntityLst
					.stream()
					.collect(Collectors.groupingBy(
							userNotificationSubscriptionCriteriaEntity -> userNotificationSubscriptionCriteriaEntity
									.getNotificationParameter().getNotificationParameterDescription()));
			groupByPrameterValueMap.forEach((key, userNotificationSubCriteriaLst) -> {
				UserNotificationSubscriptionCriteriaDTO userNotificationSubscriptionCriteriaDTO = new UserNotificationSubscriptionCriteriaDTO();
				NotificationParameter notificationParameter = userNotificationSubCriteriaLst.get(0)
						.getNotificationParameter();
				userNotificationSubscriptionCriteriaDTO.setUserNotificationSubscriptionParameterCode(
						notificationParameter.getNotificationParameterDescription());
				List<UserNotificationSubscriptionParameterDTO> notificationParamLstDTO = new ArrayList<>();
				userNotificationSubCriteriaLst
						.forEach(userSubscriptionCriteria -> setNotificationParameter(notificationParamLstDTO,
								userSubscriptionCriteria)

				);
				userNotificationSubscriptionCriteriaDTO
						.setUserNotificationSubscriptionParameterValues(notificationParamLstDTO);
				userNotificationSubscriptionCriteriaDTOLst.add(userNotificationSubscriptionCriteriaDTO);

			});

			userNotificationSubscriptionDTO
					.setUserNotificationSubscriptionCriterias(userNotificationSubscriptionCriteriaDTOLst);
		}
		return userNotificationSubscriptionDTO;

	}

	private static List<UserNotificationSubscriptionParameterDTO> setNotificationParameter(
			List<UserNotificationSubscriptionParameterDTO> notificationParamLstDTO,
			UserNotificationSubscriptionCriteria userSubscriptionCriteria) {
		Map<String, String> parameterMap = new HashMap<>();
		UserNotificationSubscriptionParameterDTO subNotificationParameterDTO = new UserNotificationSubscriptionParameterDTO();
		subNotificationParameterDTO.setId(userSubscriptionCriteria.getUserNotificationSubscriptionParameterValue());
		parameterMap.put(userSubscriptionCriteria.getUserNotificationSubscriptionParameterValue(), "");
		notificationParamLstDTO.add(subNotificationParameterDTO);
		return notificationParamLstDTO;

	}

	private static UserNotificationSubscriptionDTO userNotificationSubscriptionDetailLstEntityToDTO(
			UserNotificationSubscription userNotificationSubscription,
			UserNotificationSubscriptionDTO userNotificationSubscriptionDTO) {
		List<UserNotificationSubscriptionDetail> userNotificationSubscriptionDetailEntityLst = userNotificationSubscription
				.getUserNotificationSubscriptionDetails();
		if (CollectionUtils.isNotEmpty(userNotificationSubscriptionDetailEntityLst)) {
			List<UserNotificationSubscriptionDetailDTO> userNotificationSubscriptionDetailDTOLst = new ArrayList<>();
			Map<String, List<UserNotificationSubscriptionDetail>> groupByDelivaryMethodCodeMap = userNotificationSubscriptionDetailEntityLst
					.stream().filter(u -> u.getSubscribedUserID() != null)
					.collect(Collectors.groupingBy(UserNotificationSubscriptionDetail::getSubscribedUserID));
			groupByDelivaryMethodCodeMap.forEach((key, userSubDetailLst) -> {
				UserNotificationSubscriptionDetailDTO userNotificationSubscriptionDetailDTO = new UserNotificationSubscriptionDetailDTO();
				userNotificationSubscriptionDetailDTO.setSubscribedPerson(setSubscribedPersondetail(userSubDetailLst));
				List<String> userNotificationSubDeliveryMethodLst = new ArrayList<>();
				userSubDetailLst.forEach(userSubDetail -> {
					if (userSubDetail.getNotificationSubscriptionDeliveryMethod() != null
							&& userSubDetail.getNotificationSubscriptionDeliveryMethod()
									.getNotificationSubscriptionDeliveryMethodCode() != null) {
						userNotificationSubDeliveryMethodLst
								.add(userSubDetail.getNotificationSubscriptionDeliveryMethod()
										.getNotificationSubscriptionDeliveryMethodCode());
					}
				});
				userNotificationSubscriptionDetailDTO
						.setNotificationSubscriptionDeliveryMethodCodes(userNotificationSubDeliveryMethodLst);
				userNotificationSubscriptionDetailDTOLst.add(userNotificationSubscriptionDetailDTO);

			});
			userNotificationSubscriptionDTO
					.setUserNotificationSubscriptionDetails(userNotificationSubscriptionDetailDTOLst);
		}
		return userNotificationSubscriptionDTO;

	}

	private static UserDTO setSubscribedPersondetail(List<UserNotificationSubscriptionDetail> userSubDetailLst) {
		UserDTO subscribedPerson = new UserDTO();

		subscribedPerson.setId(userSubDetailLst.get(0).getSubscribedUserID());
		subscribedPerson.setEmailAddress(userSubDetailLst.get(0).getSubscribedEmailAddress());
		subscribedPerson.setPhoneNumber(userSubDetailLst.get(0).getSubscribedPhoneNumber());
		subscribedPerson.setSubscribedEmailType(SubscribedEmailType.getEnumByValue(userSubDetailLst.get(0).getSubscribedEmailType()));
		return subscribedPerson;
	}
}
