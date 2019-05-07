package com.jbhunt.infrastructure.notification.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jbhunt.infrastructure.edinotification.loadtenderresponse.dto.LoadTenderNotificationSubscriptionDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.CustomerLocationConfigurationDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.HoldStatusDetailDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.LocationDetailDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.PartyAssociationDetailDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.ShipmentStatusNotificationSubscriptionDTO;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationSubscriptionDTO;
import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;
import com.jbhunt.infrastructure.notification.entity.TradingPartnerAssociatedPartyLocation;

public abstract class EDINotificationSubscriptionMapperDecorator
        implements EDINotificationSubscriptionMapper, ApplicationContextAware {

    private EDINotificationSubscriptionMapper delegate;
    public static final String PARTY_ROLE_CODE_BILL_TO = "Bill To";
    public static final String PARTY_ROLE_CODE_LINE_OF_BUSINESS = "LOB";

    @Override
    public EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO(
            EDINotificationSubscription ediNotificationSubscription,
            Iterable<TradingPartnerAssociatedPartyLocation> tradingPartnerAssociatedPartyLocations) {
        EDINotificationSubscriptionDTO ediSubscriptionDto = delegate
                .ediNotificationSubscriptionDTO(ediNotificationSubscription, tradingPartnerAssociatedPartyLocations);

        Optional.ofNullable(ediNotificationSubscription.getLoadTenderEventSubscriptions())
                .ifPresent(loadTenderSubscriptions -> {
                    List<String> loadTenderEvents = new ArrayList<>();
                    loadTenderSubscriptions.forEach(ltes -> loadTenderEvents
                            .add(ltes.getShipmentNotificationEvent().getShipmentNotificationEventName()));
                    if (!loadTenderEvents.isEmpty()) {
                        LoadTenderNotificationSubscriptionDTO loadTenderNotificationSubscriptionDTO = new LoadTenderNotificationSubscriptionDTO();
                        loadTenderNotificationSubscriptionDTO.setSubscriptionEvents(loadTenderEvents);
                        ediSubscriptionDto.setLoadTenderNotificationSubscription(loadTenderNotificationSubscriptionDTO);
                        EDINotificationSubscriptionMapper.setCommonProps(loadTenderNotificationSubscriptionDTO,
                                loadTenderSubscriptions);
                    }
                });
        Optional.ofNullable(ediNotificationSubscription.getShipmentNotificationSubscriptionEventAssociations())
                .ifPresent(shipmentSubEvntAssosiations -> {
                    List<String> shipmentEvents = new ArrayList<>();
                    shipmentSubEvntAssosiations
                            .forEach(shipmentSubEvntAssosiation -> shipmentEvents.add(shipmentSubEvntAssosiation
                                    .getShipmentNotificationEvent().getShipmentNotificationEventName()));
                    if (!shipmentEvents.isEmpty()) {
                        ShipmentStatusNotificationSubscriptionDTO statusNotificationSubscriptionDTO = new ShipmentStatusNotificationSubscriptionDTO();
                        statusNotificationSubscriptionDTO.setSubscriptionEvents(shipmentEvents);
                        ediSubscriptionDto.setShipmentNotificationSubscription(statusNotificationSubscriptionDTO);
                        EDINotificationSubscriptionMapper.setCommonProps(statusNotificationSubscriptionDTO,
                                ediNotificationSubscription);
                    }
                });
        List<LocationDetailDTO> pickLocations = new ArrayList<>(), deliveryLocations = new ArrayList<>();
        Map<String, PartyAssociationDetailDTO> billToMap = new HashMap<>(), lineOfBuinessMap = new HashMap<>();
        tradingPartnerAssociatedPartyLocations.forEach(partyLocationEntity -> {
            if (Objects.nonNull(partyLocationEntity.getPickupLocationID())) {
            	LocationDetailDTO locationDetailDTO = new LocationDetailDTO();
            	locationDetailDTO.setId(partyLocationEntity.getPickupLocationID());
                pickLocations.add(locationDetailDTO);
            } else if (Objects.nonNull(partyLocationEntity.getDeliveryLocationID())) {
            	LocationDetailDTO locationDetailDTO = new LocationDetailDTO();
            	locationDetailDTO.setId(partyLocationEntity.getDeliveryLocationID());
                deliveryLocations.add(locationDetailDTO);
            } else if (Objects.nonNull(partyLocationEntity.getPartyRoleCode())
                    && PARTY_ROLE_CODE_BILL_TO.equals(partyLocationEntity.getPartyRoleCode())) {
                EDINotificationSubscriptionMapper.setBusinessUnitAndServiceOffer(billToMap, partyLocationEntity,
                        () -> new PartyAssociationDetailDTO());
            } else if (Objects.nonNull(partyLocationEntity.getPartyRoleCode())
                    && PARTY_ROLE_CODE_LINE_OF_BUSINESS.equals(partyLocationEntity.getPartyRoleCode())) {
                EDINotificationSubscriptionMapper.setBusinessUnitAndServiceOffer(lineOfBuinessMap, partyLocationEntity,
                        () -> new PartyAssociationDetailDTO());
            }
        });
        CustomerLocationConfigurationDTO customerLocationConfig = new CustomerLocationConfigurationDTO();
        customerLocationConfig.setPickUpLocations(pickLocations);
        customerLocationConfig.setDeliveryLocations(deliveryLocations);
		customerLocationConfig.setBillTo(
				EDINotificationSubscriptionMapper.convert(billToMap.values(), PartyAssociationDetailDTO.class));
        customerLocationConfig.setLineOfBusiness(
                EDINotificationSubscriptionMapper.convert(lineOfBuinessMap.values(), PartyAssociationDetailDTO.class));
        if (Objects.isNull(ediSubscriptionDto.getShipmentNotificationSubscription())) {
            ediSubscriptionDto.setShipmentNotificationSubscription(new ShipmentStatusNotificationSubscriptionDTO());
        }
        ediSubscriptionDto.getShipmentNotificationSubscription().setCustomerLocationConfig(customerLocationConfig);
		Optional.ofNullable(ediNotificationSubscription.getInTransitNotificationSubscription())
				.ifPresent(inTransitSubscription -> ediSubscriptionDto
						.setInTransitTimeFrequency(inTransitSubscription.getInTransitNotificationFrequency()));
		enrichments(ediNotificationSubscription, ediSubscriptionDto);
        return ediSubscriptionDto;
    }
	private void enrichments(EDINotificationSubscription ediNotificationSubscription,
			EDINotificationSubscriptionDTO ediSubscriptionDto) {
		if (ediSubscriptionDto.getShipmentNotificationSubscription() != null) {
			ediSubscriptionDto.getShipmentNotificationSubscription()
			.setBatchFrequency(ediNotificationSubscription.getEdiNotificationBatchFrequency());
			ediSubscriptionDto.getShipmentNotificationSubscription()
			.setTimeDelayFrequency(ediNotificationSubscription.getEdiNotificationTimeDelay());
			ediSubscriptionDto.getShipmentNotificationSubscription().setSendStatusOnlyForEDIOrders(ediNotificationSubscription.getElectronicDataInterchangeSubscriptionIndicator());
			Optional.ofNullable(ediNotificationSubscription.getShipmentNotificationSubscriptionReferenceNumber())
			.ifPresent(shipmentnotificationSubscriptionReferenceNumber -> ediSubscriptionDto
					.getShipmentNotificationSubscription().setReferenceTypeCode(shipmentnotificationSubscriptionReferenceNumber.getReferenceNumberTypeCode()));
		}
		if (ediSubscriptionDto.getLoadTenderNotificationSubscription() != null) {
			ediSubscriptionDto.getLoadTenderNotificationSubscription()
			.setTimeDelayFrequency(ediNotificationSubscription.getEdiNotificationLoadTenderTimeDelay());
		}
		HoldStatusDetailDTO holdStatusDetailDTO = new HoldStatusDetailDTO();
		if (Optional.ofNullable(ediNotificationSubscription.getShipmentNotificationSubscriptionHold()).isPresent()
				&& Optional.ofNullable(ediNotificationSubscription.getShipmentNotificationSubscriptionHold()
						.getShipmentNotificationStopReasonEventTypeAssociation()).isPresent()) {
			holdStatusDetailDTO.setEventType(ediNotificationSubscription.getShipmentNotificationSubscriptionHold()
					.getShipmentNotificationStopReasonEventTypeAssociation().getShipmentNotificationStopEventType()
					.getShipmentNotificationStopEventTypeCode());
			holdStatusDetailDTO.setStopClass(ediNotificationSubscription.getShipmentNotificationSubscriptionHold()
					.getShipmentNotificationStopReasonEventTypeAssociation().getStopReasonCode());
			holdStatusDetailDTO.setStopTypeCode(ediNotificationSubscription.getShipmentNotificationSubscriptionHold()
					.getShipmentNotificationStopReasonEventTypeAssociation().getShipmentNotificationStopType()
					.getShipmentNotificationStopTypeCode());
			ediSubscriptionDto.getShipmentNotificationSubscription().setHoldStatusDetail(holdStatusDetailDTO);
		}else{
			ediSubscriptionDto.getShipmentNotificationSubscription().setHoldStatusDetail(holdStatusDetailDTO);
		}
	}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, EDINotificationSubscriptionMapper> beansMap = applicationContext
                .getBeansOfType(EDINotificationSubscriptionMapper.class);
        beansMap.values().forEach(mapper -> {
            if (!(mapper instanceof EDINotificationSubscriptionMapperDecorator)) {
                this.delegate = mapper;
                return;
            }
        });
    }

}
