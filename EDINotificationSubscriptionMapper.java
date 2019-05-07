package com.jbhunt.infrastructure.notification.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import com.jbhunt.infrastructure.edinotification.common.dto.NotificationSubscriptionCommonDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.PartyAssociationDetailDTO;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationSubscriptionDTO;
import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;
import com.jbhunt.infrastructure.notification.entity.LoadTenderEventSubscription;
import com.jbhunt.infrastructure.notification.entity.TradingPartnerAssociatedPartyLocation;
import com.jbhunt.infrastructure.notification.user.dto.UserDTO;

@Component
@Mapper(componentModel = "spring")
@DecoratedWith(EDINotificationSubscriptionMapperDecorator.class)
public interface EDINotificationSubscriptionMapper {

	@Mappings({
			@Mapping(source = "notificationSubscription.ediNotificationSubscriptionID", target = "shipmentNotificationSubscriptionID"),
			@Mapping(source = "notificationSubscription.tradingPartnerCode", target = "tradingPartnerID"),
			@Mapping(source = "notificationSubscription.lastUpdateTimestamp", target = "lastUpdatedTimeStamp"),
			@Mapping(source = "notificationSubscription.lastUpdateUserId", target = "lastUpdatedBy") })
	EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO(EDINotificationSubscription notificationSubscription,
			Iterable<TradingPartnerAssociatedPartyLocation> tradingPartnerAssociatedPartyLocations);

	public static String getActiveStatus(LocalDateTime effectiveTimestamp, LocalDateTime expireTimestamp) {
		LocalDateTime currentTimestamp = LocalDateTime.now();
		if (currentTimestamp.isAfter(effectiveTimestamp) && currentTimestamp.isBefore(expireTimestamp)) {
			return "ACTIVE";
		}
		return "INACTIVE";
	}

	public static <T> List<T> convert(Collection<?> list, Class<T> clz) {
		List<T> newList = new ArrayList<>();
		list.forEach(obj -> newList.add(clz.cast(obj)));
		return newList;
	}

	public static void setBusinessUnitAndServiceOffer(Map<String, PartyAssociationDetailDTO> businessUnitMap,
			TradingPartnerAssociatedPartyLocation partyLocationEntity,
			Supplier<PartyAssociationDetailDTO> beanSupplier) {
		PartyAssociationDetailDTO partyAssociationDetailDTO = businessUnitMap
				.get(partyLocationEntity.getFinanceBusinessUnitCode()+"-"+
						partyLocationEntity.getServiceOfferingCode()+"-"+partyLocationEntity.getPartyId());
		if (Objects.isNull(partyAssociationDetailDTO)) {
			partyAssociationDetailDTO = beanSupplier.get();
			partyAssociationDetailDTO.setServiceOffering(new ArrayList<>());
			Set<String> keySet = businessUnitMap.keySet();
			partyAssociationDetailDTO.setBusinessUnit(partyLocationEntity.getFinanceBusinessUnitCode());
			String partyId = null;
			if(Objects.nonNull(partyLocationEntity.getPartyId())){
			    partyId = partyLocationEntity.getPartyId().toString();
			}
			partyAssociationDetailDTO.setPartyId(partyId);
			partyAssociationDetailDTO.setPartyRoleCode(partyLocationEntity.getPartyRoleCode());
			Set<String> deletablekeys = new HashSet<>();
			for (String key : keySet) {
				String[] keyElements =  key.split("-");
				if(keyElements[0].equalsIgnoreCase(partyLocationEntity.getFinanceBusinessUnitCode()) && 
						keyElements[2].equalsIgnoreCase(partyId)){
					partyAssociationDetailDTO = businessUnitMap.get(key);
					deletablekeys.add(key);
				}
			}
			deletablekeys.forEach(key->businessUnitMap.remove(key));
		businessUnitMap.put(partyLocationEntity.getFinanceBusinessUnitCode()+"-"+
				partyLocationEntity.getServiceOfferingCode()+"-"+partyLocationEntity.getPartyId(), partyAssociationDetailDTO);
		}
		if(!Objects.isNull(partyLocationEntity.getServiceOfferingCode())){
		partyAssociationDetailDTO.getServiceOffering().add(partyLocationEntity.getServiceOfferingCode());
		}
	}

	public static void setCommonProps(NotificationSubscriptionCommonDTO commonDto,
			List<LoadTenderEventSubscription> loadTenderEventSubscriptions) {
		loadTenderEventSubscriptions.forEach(entity -> {
			setCommonProps(commonDto, entity.getCreateUserId(), entity.getLastUpdateUserId(),
					entity.getLastUpdateTimestamp());
			commonDto.setStatus(getActiveStatus(entity.getEffectiveTimestamp(), entity.getExpirationTimestamp()));
			return;
		});
	}

	public static void setCommonProps(NotificationSubscriptionCommonDTO commonDto,
			EDINotificationSubscription ediNotificationSubscription) {
		setCommonProps(commonDto, ediNotificationSubscription.getCreateUserId(),
				ediNotificationSubscription.getLastUpdateUserId(),
				ediNotificationSubscription.getLastUpdateTimestamp());
		commonDto.setStatus(getActiveStatus(ediNotificationSubscription.getEffectiveTimestamp(),
				ediNotificationSubscription.getExpirationTimestamp()));
	}

	public static void setCommonProps(NotificationSubscriptionCommonDTO commonDto, String createdUser,
			String updatedUser, LocalDateTime lastUpdatedTimestamp) {
		commonDto.setCreatedBy(getUser(createdUser));
		commonDto.setLastUpdatedBy(getUser(updatedUser));
		commonDto.setLastUpdatedTimestamp(lastUpdatedTimestamp);
	}

	public static UserDTO getUser(String firstName) {
		UserDTO user = new UserDTO();
		user.setFirstName(firstName);
		return user;
	}
}
