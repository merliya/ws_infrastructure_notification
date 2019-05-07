package com.jbhunt.infrastructure.notification.helper;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.jbhunt.edi.tradingpartners.dto.TradingPartnerDTO;
import com.jbhunt.hrms.eoi.api.dto.person.PersonDTO;
import com.jbhunt.infrastructure.edinotification.loadtenderresponse.dto.LoadTenderNotificationSubscriptionDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.CustomerLocationConfigurationDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.HoldStatusDetailDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.LocationDetailDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.PartyAssociationDetailDTO;
import com.jbhunt.infrastructure.edinotification.shipmentstatus.dto.ShipmentStatusNotificationSubscriptionDTO;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationSubscriptionDTO;
import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;
import com.jbhunt.infrastructure.notification.entity.ShipmentNotificationStopType;
import com.jbhunt.infrastructure.notification.entity.TradingPartnerAssociatedPartyLocation;
import com.jbhunt.infrastructure.notification.mapper.EDINotificationSubscriptionMapper;
import com.jbhunt.infrastructure.notification.repository.ShipmentNotificationStopTypeRepository;
import com.jbhunt.infrastructure.notification.repository.TradingPartnerAssociatedPartyLocationRepository;
import com.jbhunt.infrastructure.notification.service.EOIHelperService;
import com.jbhunt.infrastructure.notification.user.dto.UserDTO;
import com.jbhunt.infrastructure.notification.util.EDINotificationSubscriptionRepositoryUtil;
import com.jbhunt.mdm.dto.AddressDTO;
import com.jbhunt.mdm.dto.CustomerDetailsDTO;
import com.jbhunt.mdm.dto.LocationProfileDTO;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EDINotificationHelper {

	private final NotificationHelper notificationHelper;
	private final EDINotificationSubscriptionMapper ediNotificationSubscriptionMapper;
	private final TradingPartnerAssociatedPartyLocationRepository tradingPartnerAssociatedPartyLocationRepository;
	private final EOIHelperService eOIHelperService;
	private final ShipmentNotificationStopTypeRepository shipmentNotificationStopTypeRepository;

	public EDINotificationHelper(NotificationHelper notificationHelper,
			EDINotificationSubscriptionMapper ediNotificationSubscriptionMapper,
			TradingPartnerAssociatedPartyLocationRepository tradingPartnerAssociatedPartyLocationRepository,
			EOIHelperService eOIHelperService, ShipmentNotificationStopTypeRepository shipmentNotificationStopTypeRepository) {
		this.notificationHelper = notificationHelper;
		this.ediNotificationSubscriptionMapper = ediNotificationSubscriptionMapper;
		this.tradingPartnerAssociatedPartyLocationRepository = tradingPartnerAssociatedPartyLocationRepository;
		this.eOIHelperService = eOIHelperService;
		this.shipmentNotificationStopTypeRepository = shipmentNotificationStopTypeRepository;
	}

	public EDINotificationSubscriptionDTO ediNotificationSubscriptionEnrichment(
			EDINotificationSubscription ediNotificationSubscription) throws JsonParseException {
		log.info("EDI Notification Helper: getEDINotificationSubscription");

		Iterable<TradingPartnerAssociatedPartyLocation> partyLocation = tradingPartnerAssociatedPartyLocationRepository
				.findAll(EDINotificationSubscriptionRepositoryUtil.getTradingPartnerAssociatedPartyLocationPredicate(
						ediNotificationSubscription.getTradingPartnerCode()));

		EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO = ediNotificationSubscriptionMapper
				.ediNotificationSubscriptionDTO(ediNotificationSubscription, partyLocation);
		this.enrichTradingPartner(ediNotificationSubscriptionDTO);
		this.enrichBTODetails(ediNotificationSubscriptionDTO);
		this.enrichLOBDetails(ediNotificationSubscriptionDTO);
		this.enrichPickUpLocationDetails(ediNotificationSubscriptionDTO);
		this.enrichDeliveryLocationDetails(ediNotificationSubscriptionDTO);
		this.enrichUserDetails(ediNotificationSubscription,ediNotificationSubscriptionDTO);
		

		return ediNotificationSubscriptionDTO;
	}
	
	public void enrichTradingPartner(EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO) {
		if (Optional.ofNullable(ediNotificationSubscriptionDTO).map(EDINotificationSubscriptionDTO::getTradingPartnerID)
				.isPresent()) {
			TradingPartnerDTO tradingPartner = notificationHelper
					.enrichTradingPartner(ediNotificationSubscriptionDTO.getTradingPartnerID());
			if (Optional.ofNullable(tradingPartner).map(TradingPartnerDTO::getTradingPartnerDescription)
					.isPresent()) {
			ediNotificationSubscriptionDTO.setTradingPartnerDescription(tradingPartner.getTradingPartnerDescription());
			}
		}
	}

	public void enrichBTODetails(EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO) {
		if (Optional.ofNullable(ediNotificationSubscriptionDTO)
				.map(EDINotificationSubscriptionDTO::getShipmentNotificationSubscription)
				.map(ShipmentStatusNotificationSubscriptionDTO::getCustomerLocationConfig)
				.map(CustomerLocationConfigurationDTO::getBillTo).isPresent()) {
			List<PartyAssociationDetailDTO> partyAssociationDetailDTOs = ediNotificationSubscriptionDTO
					.getShipmentNotificationSubscription().getCustomerLocationConfig().getBillTo();

			partyAssociationDetailDTOs.forEach(billTo -> {
				List<CustomerDetailsDTO> customerDetailsDTOs = notificationHelper
						.findAllCustomerDetailsByBillToCode(billTo.getPartyId());
				CustomerDetailsDTO customerDetailsDTO = customerDetailsDTOs.get(0);
				// need to cleanup boilerplate codes start

				enrichLocationDetailDTo(customerDetailsDTO, billTo);
			});
		}
	}

	public void enrichLOBDetails(EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO) {
		if (Optional.ofNullable(ediNotificationSubscriptionDTO)
				.map(EDINotificationSubscriptionDTO::getShipmentNotificationSubscription)
				.map(ShipmentStatusNotificationSubscriptionDTO::getCustomerLocationConfig)
				.map(CustomerLocationConfigurationDTO::getLineOfBusiness).isPresent()) {
			List<PartyAssociationDetailDTO> partyAssociationDetailDTOs = ediNotificationSubscriptionDTO
					.getShipmentNotificationSubscription().getCustomerLocationConfig().getLineOfBusiness();
			partyAssociationDetailDTOs.forEach(lob -> {
				List<CustomerDetailsDTO> customerDetailsDTOs = notificationHelper
						.findAllCustomerDetailsByLineOfBussiness(lob.getPartyId());
				CustomerDetailsDTO customerDetailsDTO = customerDetailsDTOs.get(0);
				// need to cleanup boilerplate codes start

				enrichLocationDetailDTo(customerDetailsDTO, lob);
				log.info("EDINotificationHelper::enrichLOBDetails " + customerDetailsDTO.getName()
						+ customerDetailsDTO.getAddressLineOne() + customerDetailsDTO.getAddressLineTwo()
						+ customerDetailsDTO.getCity() + customerDetailsDTO.getState() + customerDetailsDTO.getZipCode()
						+ customerDetailsDTO.getCountry());
			});

		}
	}

	public void enrichPickUpLocationDetails(EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO) {
		if (Optional.ofNullable(ediNotificationSubscriptionDTO)
				.map(EDINotificationSubscriptionDTO::getShipmentNotificationSubscription)
				.map(ShipmentStatusNotificationSubscriptionDTO::getCustomerLocationConfig)
				.map(CustomerLocationConfigurationDTO::getPickUpLocations).isPresent()) {
			List<LocationDetailDTO> locationDetailDTOs = ediNotificationSubscriptionDTO
					.getShipmentNotificationSubscription().getCustomerLocationConfig().getPickUpLocations();
			locationDetailDTOs.forEach(pickupLocation -> {
				LocationProfileDTO locationProfileDTO = notificationHelper
						.findLocationProfilebyLocationCode(pickupLocation.getId().toString());

				if (Optional.ofNullable(locationProfileDTO).map(LocationProfileDTO::getAddressDTO).isPresent()) {
					AddressDTO addressDTO = locationProfileDTO.getAddressDTO();
					pickupLocation.setId(locationProfileDTO.getId());
					pickupLocation.setName(
							locationProfileDTO.getLocationName() + "(" + locationProfileDTO.getLocationCode() + ")");
					pickupLocation.setAddressLineOne(addressDTO.getAddressLine1());
					pickupLocation.setAddressLineTwo(addressDTO.getAddressLine2());
					pickupLocation.setCity(addressDTO.getCity());
					pickupLocation.setState(addressDTO.getState());
					pickupLocation.setZipCode(addressDTO.getZipcode());
					pickupLocation.setCountry(addressDTO.getCountry());
					pickupLocation.setDetail(locationProfileDTO.getId() + " " + locationProfileDTO.getLocationCode()
							+ " " + addressDTO.getAddressLine1() + ", " + addressDTO.getAddressLine2() + ", "
							+ addressDTO.getCity() + ", " + addressDTO.getState() + ", " + addressDTO.getZipcode()
							+ ", " + addressDTO.getCountry());

				}
			});
		}
	}

	public void enrichDeliveryLocationDetails(EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO) {
		if (Optional.ofNullable(ediNotificationSubscriptionDTO)
				.map(EDINotificationSubscriptionDTO::getShipmentNotificationSubscription)
				.map(ShipmentStatusNotificationSubscriptionDTO::getCustomerLocationConfig)
				.map(CustomerLocationConfigurationDTO::getDeliveryLocations).isPresent()) {
			List<LocationDetailDTO> locationDetailDTOs = ediNotificationSubscriptionDTO
					.getShipmentNotificationSubscription().getCustomerLocationConfig().getDeliveryLocations();
			locationDetailDTOs.forEach(deliveryLocation -> {
				LocationProfileDTO locationProfileDTO = notificationHelper
						.findLocationProfilebyLocationCode(deliveryLocation.getId().toString());

				if (Optional.ofNullable(locationProfileDTO).map(LocationProfileDTO::getAddressDTO).isPresent()) {
					AddressDTO addressDTO = locationProfileDTO.getAddressDTO();
					deliveryLocation.setId(locationProfileDTO.getId());
					deliveryLocation.setName(
							locationProfileDTO.getLocationName() + "(" + locationProfileDTO.getLocationCode() + ")");
					deliveryLocation.setAddressLineOne(addressDTO.getAddressLine1());
					deliveryLocation.setAddressLineTwo(addressDTO.getAddressLine2());
					deliveryLocation.setCity(addressDTO.getCity());
					deliveryLocation.setState(addressDTO.getState());
					deliveryLocation.setZipCode(addressDTO.getZipcode());
					deliveryLocation.setCountry(addressDTO.getCountry());
					deliveryLocation.setDetail(locationProfileDTO.getId() + " " + locationProfileDTO.getLocationCode()
							+ " " + addressDTO.getAddressLine1() + ", " + addressDTO.getAddressLine2() + ", "
							+ addressDTO.getCity() + ", " + addressDTO.getState() + ", " + addressDTO.getZipcode()
							+ ", " + addressDTO.getCountry());

				}

			});
		}
	}

	void enrichLocationDetailDTo(CustomerDetailsDTO customerDetailsDTO,
			PartyAssociationDetailDTO partyAssociationDetailDTO) {
		partyAssociationDetailDTO.setId(customerDetailsDTO.getId());
		partyAssociationDetailDTO.setName(customerDetailsDTO.getName());
		partyAssociationDetailDTO.setCode(customerDetailsDTO.getCode());
		partyAssociationDetailDTO.setAddressLineOne(customerDetailsDTO.getAddressLineOne());
		partyAssociationDetailDTO.setAddressLineTwo(customerDetailsDTO.getAddressLineTwo());
		partyAssociationDetailDTO.setCity(customerDetailsDTO.getCity());
		partyAssociationDetailDTO.setState(customerDetailsDTO.getState());
		partyAssociationDetailDTO.setZipCode(customerDetailsDTO.getZipCode());
		partyAssociationDetailDTO.setCountry(customerDetailsDTO.getCountry());
		partyAssociationDetailDTO.setDetail(customerDetailsDTO.getName() + ", " +customerDetailsDTO.getCode()
				+ ", " + customerDetailsDTO.getAddressLineOne()
				+ ", " + customerDetailsDTO.getCity() + ", " + customerDetailsDTO.getState() + ", "
				+ customerDetailsDTO.getZipCode() + ", " + customerDetailsDTO.getCountry());
	}
	
	public void enrichUserDetails(EDINotificationSubscription ediNotificationSubscription,EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO){
		PersonDTO createdPersonDTO = eOIHelperService.fetchEmployeeDetailForUserId(ediNotificationSubscription.getCreateUserId());
		PersonDTO updatedPersonDTO = eOIHelperService.fetchEmployeeDetailForUserId(ediNotificationSubscription.getLastUpdateUserId());
		UserDTO createdUserDTO = new UserDTO();
		UserDTO updatedUserDTO = new UserDTO();
		createdUserDTO.setFirstName(createdPersonDTO.getFirstName());
		createdUserDTO.setPreferredName(createdPersonDTO.getPrefName());
		createdUserDTO.setLastName(createdPersonDTO.getLastName());
		updatedUserDTO.setFirstName(updatedPersonDTO.getFirstName());
		updatedUserDTO.setPreferredName(updatedPersonDTO.getPrefName());
		updatedUserDTO.setLastName(updatedPersonDTO.getLastName());
		ediNotificationSubscriptionDTO.setLastUpdatedBy(updatedPersonDTO.getPrefName()+" "+updatedPersonDTO.getLastName());
		ediNotificationSubscriptionDTO.setCreatedBy(createdPersonDTO.getPrefName()+" "+createdPersonDTO.getLastName());
		if (Optional.ofNullable(ediNotificationSubscriptionDTO)
				.map(EDINotificationSubscriptionDTO::getShipmentNotificationSubscription).isPresent()){
			ShipmentStatusNotificationSubscriptionDTO shipmentStatusNotificationSubscriptionDTO = ediNotificationSubscriptionDTO.getShipmentNotificationSubscription();
			enrichStopTypeDescription(shipmentStatusNotificationSubscriptionDTO);
			shipmentStatusNotificationSubscriptionDTO.setCreatedBy(createdUserDTO);
			shipmentStatusNotificationSubscriptionDTO.setLastUpdatedBy(updatedUserDTO);
		}
		if (Optional.ofNullable(ediNotificationSubscriptionDTO)
				.map(EDINotificationSubscriptionDTO::getLoadTenderNotificationSubscription).isPresent()){
			LoadTenderNotificationSubscriptionDTO loadTenderNotificationSubscriptionDTO = ediNotificationSubscriptionDTO.getLoadTenderNotificationSubscription();
			loadTenderNotificationSubscriptionDTO.setCreatedBy(createdUserDTO);
			loadTenderNotificationSubscriptionDTO.setLastUpdatedBy(updatedUserDTO);
		}
	}
	public void enrichStopTypeDescription(ShipmentStatusNotificationSubscriptionDTO shipmentStatusNotificationSubscriptionDTO) {
		if(Optional.ofNullable(shipmentStatusNotificationSubscriptionDTO)
				.map(ShipmentStatusNotificationSubscriptionDTO::getHoldStatusDetail).isPresent() && Optional.ofNullable(shipmentStatusNotificationSubscriptionDTO)
				.map(ShipmentStatusNotificationSubscriptionDTO::getHoldStatusDetail).
				map(HoldStatusDetailDTO::getStopTypeCode).isPresent()){
			ShipmentNotificationStopType shipmentNotificationStopType = shipmentNotificationStopTypeRepository.findOne(shipmentStatusNotificationSubscriptionDTO.getHoldStatusDetail().getStopTypeCode());
			shipmentStatusNotificationSubscriptionDTO.getHoldStatusDetail().setStopTypeDescription(shipmentNotificationStopType.getShipmentNotificationStopTypeDescription());
		}
	}
}