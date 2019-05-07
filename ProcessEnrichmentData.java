package com.jbhunt.infrastructure.notification.helper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jbhunt.hrms.eoi.api.dto.person.PersonDTO;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;
import com.jbhunt.infrastructure.notification.service.EOIHelperService;
import com.jbhunt.infrastructure.notification.user.dto.UserDTO;
import com.jbhunt.infrastructure.taskassignment.entity.TaskAssignment;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionCriteriaDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionDetailDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionParameterDTO;
import com.jbhunt.mdm.dto.CustomerDetailsDTO;
import com.jbhunt.mdm.dto.LocationProfileDTO;
import com.jbhunt.mdm.dto.ProfileContactDTO;
import com.jbhunt.edi.tradingpartners.dto.TradingPartnerDTO;
import com.jbhunt.ordermanagement.order.entity.Order;
import com.jbhunt.personnel.schedule.dto.EmployeeProfileElasticIndexDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessEnrichmentData {

	private final NotificationHelper notificationHelper;
	private final EOIHelperService eoiHelperService;
	private final JestHelper jestHelper;

	public ProcessEnrichmentData(NotificationHelper notificationHelper, EOIHelperService eoiHelperService,
			JestHelper jestHelper) {
		this.notificationHelper = notificationHelper;
		this.eoiHelperService = eoiHelperService;
		this.jestHelper = jestHelper;
	}

	/**
	 * 
	 * @param id
	 *            int
	 * @param criteriaCode
	 *            String
	 * @param parameterDTO
	 *            UserNotificationSubscriptionParameterDTO
	 * @param parameterId
	 *            String
	 * @return
	 * @throws URISyntaxException
	 * @throws JSONException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws Exception
	 */
	private UserNotificationSubscriptionParameterDTO enrichMMDDetails(String criteriaCode,
			UserNotificationSubscriptionParameterDTO parameterDTO, String parameterId)
			throws URISyntaxException, JSONException, JsonParseException, JsonMappingException, IOException {
		Map<String, String> parameterMap = new HashMap<String, String>();
		locationCriteria(criteriaCode, parameterDTO, parameterId, parameterMap);
		accountCriteria(criteriaCode, parameterDTO, parameterId, parameterMap);
		if (NotificationApplicationConstants.ORDER_NUMBER.equalsIgnoreCase(criteriaCode)) {
			Order response = notificationHelper.findOrderByOrderId(parameterId);
			setOrderDetails(parameterMap, response);
			parameterDTO.setDetails(parameterMap);
		}
		if (NotificationApplicationConstants.BUSINESS_UNIT.equalsIgnoreCase(criteriaCode)
				|| NotificationApplicationConstants.SERVICE_OFFERING.equalsIgnoreCase(criteriaCode)) {
			parameterMap.put(criteriaCode, parameterId);
			parameterDTO.setDetails(parameterMap);
		}
		if(NotificationApplicationConstants.TRADING_PARTNER.equalsIgnoreCase(criteriaCode)){
			parameterMap = tradingPartnerCriteria(criteriaCode, parameterId, parameterMap);
			parameterDTO.setDetails(parameterMap);
		}
		if(NotificationApplicationConstants.TASK_ASS.equalsIgnoreCase(criteriaCode)){
			parameterMap = taskAssignmentCriteria(parameterId, parameterMap);
			parameterDTO.setDetails(parameterMap);
		}
		if (NotificationApplicationConstants.DAYS_EXPIRY.equalsIgnoreCase(criteriaCode)
				|| NotificationApplicationConstants.WEEKS_LST_EFF_DATE.equalsIgnoreCase(criteriaCode)
				|| NotificationApplicationConstants.SCAC.equalsIgnoreCase(criteriaCode)){
			parameterMap.put(criteriaCode, parameterId);
			parameterDTO.setDetails(parameterMap);
		}
		if(NotificationApplicationConstants.ORDER_OWNER.equalsIgnoreCase(criteriaCode)){
			parameterMap = orderOwnerCriteria(criteriaCode, parameterId, parameterMap);
			parameterDTO.setDetails(parameterMap);
		}
		
		if(NotificationApplicationConstants.ASSOCIATED_USER.equalsIgnoreCase(criteriaCode)){
			parameterMap = associatedUserCriteria(criteriaCode, parameterId, parameterMap);
			parameterDTO.setDetails(parameterMap);
		}
		return parameterDTO;
	}

	public Map<String, String> taskAssignmentCriteria(String parameterId,
			Map<String, String> parameterMap) {
		TaskAssignment taskAssignment = notificationHelper.findTaskAssignmentById(parameterId);
		parameterMap.put("taskAssignmentName", taskAssignment.getTaskAssignmentName());
		return parameterMap;
	}

	/**
	 * @param criteriaCode
	 * @param parameterDTO
	 * @param parameterId
	 * @param parameterMap
	 * @return
	 */
	private Map<String, String> accountCriteria(String criteriaCode,
			UserNotificationSubscriptionParameterDTO parameterDTO, String parameterId,
			Map<String, String> parameterMap) {
		if (criteriaCode.matches(NotificationApplicationConstants.NATIONAL_ACCOUNT + "|"
				+ NotificationApplicationConstants.CORPORATE_ACCOUNT)) {
			List<CustomerDetailsDTO> response = notificationHelper.findAllCustomerDetailsByBillToCode(parameterId);
			setBillToDetails(response, parameterMap);
			parameterDTO.setDetails(parameterMap);
		}
		if (NotificationApplicationConstants.SOLICITOR.equalsIgnoreCase(criteriaCode)) {
			List<ProfileContactDTO> response = notificationHelper.getSolictorDetails(parameterId);
			setSolictorDetails(response, parameterMap);
			parameterDTO.setDetails(parameterMap);

		}
		if (NotificationApplicationConstants.BILL_TO.equalsIgnoreCase(criteriaCode)) {
			List<CustomerDetailsDTO> response = notificationHelper.findAllCustomerDetailsByBillToCode(parameterId);
			setBillToDetails(response, parameterMap);
			parameterDTO.setDetails(parameterMap);
		}
		if (NotificationApplicationConstants.LINE_OF_BUSINESS.equalsIgnoreCase(criteriaCode)) {
			List<CustomerDetailsDTO> response = notificationHelper.findAllCustomerDetailsByLineOfBussiness(parameterId);
			setBillToDetails(response, parameterMap);
			parameterDTO.setDetails(parameterMap);
		}
		return parameterMap;
	}

	/**
	 * @param criteriaCode
	 * @param parameterDTO
	 * @param parameterId
	 * @param parameterMap
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private Map<String, String> locationCriteria(String criteriaCode,
			UserNotificationSubscriptionParameterDTO parameterDTO, String parameterId, Map<String, String> parameterMap)
			throws JSONException, IOException, JsonParseException, JsonMappingException {
		if (criteriaCode.matches(NotificationApplicationConstants.ORIGIN_MARKETING_AREA + "|"
				+ NotificationApplicationConstants.DESTINATION_MARKETING_AREA + "|"
				+ NotificationApplicationConstants.ORIGIN_CAPACITY_AREA + "|"
				+ NotificationApplicationConstants.DESTINATION_CAPACITY_AREA)) {
			parameterMap.put(criteriaCode, parameterId);
			parameterDTO.setDetails(parameterMap);
		}

		if (criteriaCode
				.matches(NotificationApplicationConstants.SHIPPER + "|" + NotificationApplicationConstants.RECEIVER + "|" + NotificationApplicationConstants.INTERMEDIATE)) {
			LocationProfileDTO locationProfileDTO = notificationHelper.findLocationProfilebyLocationCode(parameterId);
			if (locationProfileDTO != null) {
				setLocationDetails(parameterMap, locationProfileDTO);
			}
			parameterDTO.setDetails(parameterMap);
		}
		return parameterMap;
	}

	private Map<String, String> setLocationDetails(Map<String, String> parameterMap, LocationProfileDTO location) {
		if (Objects.nonNull(location) && Optional.ofNullable(location.getId()).isPresent()) {
			parameterMap.put("locationID", String.valueOf(location.getId()));
			parameterMap.put("locationCode", location.getLocationCode());
			parameterMap.put("locationname", location.getLocationName());
			parameterMap.put("addressLine1", location.getAddressDTO().getAddressLine1());
			parameterMap.put("addressLine2", location.getAddressDTO().getAddressLine2());
			parameterMap.put("city", location.getAddressDTO().getCity());
			parameterMap.put("state", location.getAddressDTO().getState());

		}
		return parameterMap;

	}

	/**
	 * Set the Order details
	 * 
	 * @param parameterMap
	 *            Map<String, String>
	 * @param response
	 *            Order
	 */
	private void setOrderDetails(Map<String, String> parameterMap, Order response) {
		parameterMap.put("orderID", String.valueOf(response.getOrderID()));
		parameterMap.put("orderTypeCode", response.getOrderTypeCode());
		parameterMap.put("orderStatusCode", response.getOrderStatusCode());
		parameterMap.put("orderSubTypeCode", response.getOrderSubTypeCode());
		parameterMap.put("orderGroupingID", String.valueOf(response.getOrderGroupingID()));
	}

	/**
	 * 
	 * @param response
	 *            List<CustomerDetailsDTO>>
	 * @param parameterMap
	 *            Map<String, String>
	 *
	 * @return
	 */
	private Map<String, String> setBillToDetails(List<CustomerDetailsDTO> response, Map<String, String> parameterMap) {
		if (response != null && CollectionUtils.isNotEmpty(response)) {
			CustomerDetailsDTO customerDetailsDTO = response.get(0);
			parameterMap.put("id", Integer.toString(customerDetailsDTO.getId()));
			parameterMap.put("code", customerDetailsDTO.getCode());
			parameterMap.put("name", customerDetailsDTO.getName());
			parameterMap.put("roleType", customerDetailsDTO.getRoleType());
			parameterMap.put("addressLineOne", customerDetailsDTO.getAddressLineOne());
			parameterMap.put("addressLineTwo", customerDetailsDTO.getAddressLineTwo());
			parameterMap.put("city", customerDetailsDTO.getCity());
			parameterMap.put("state", customerDetailsDTO.getState());
			parameterMap.put("zipCode", customerDetailsDTO.getZipCode());
			parameterMap.put("country", customerDetailsDTO.getCountry());

		}
		return parameterMap;
	}

	private Map<String, String> setSolictorDetails(List<ProfileContactDTO> response, Map<String, String> parameterMap) {
		if (response != null && CollectionUtils.isNotEmpty(response)) {
			ProfileContactDTO profileContactDTO = response.get(0);
			parameterMap.put("contactId", String.valueOf(profileContactDTO.getContactId()));
			parameterMap.put("contactType", profileContactDTO.getContactType());
			parameterMap.put("contactValue", profileContactDTO.getContactValue());
			parameterMap.put("contactMethod", profileContactDTO.getContactMethod());
			parameterMap.put("firstName", profileContactDTO.getFirstName());
			parameterMap.put("lastName", profileContactDTO.getLastName());
		}
		return parameterMap;
	}

	/**
	 * @param userNotificationSubCriteriaLst
	 */
	public void processUserNotificationSubscriptionCriteria(
			List<UserNotificationSubscriptionCriteriaDTO> userNotificationSubCriteriaLst) {
		if (CollectionUtils.isNotEmpty(userNotificationSubCriteriaLst)) {
			userNotificationSubCriteriaLst.forEach(criteriaDTO -> {
				String parameterCode = criteriaDTO.getUserNotificationSubscriptionParameterCode();
				List<UserNotificationSubscriptionParameterDTO> parameterValueLst = criteriaDTO
						.getUserNotificationSubscriptionParameterValues();

				parameterValueLst.forEach(parameterDto -> {
					String parameterId = parameterDto.getId();
					if (Optional.ofNullable(parameterId).isPresent()
							&& Optional.ofNullable(parameterCode).isPresent()) {
						try {
							enrichMMDDetails(parameterCode, parameterDto, parameterId);
						} catch (Exception e) {
							log.error("Error Message :Get the Enrichment Details", e);
						}
					}
				});

			});
		}
	}

	/**
	 * @param userNotificationSubscriptionDetailDTO
	 */
	public void setUserDTO(List<UserNotificationSubscriptionDetailDTO> userNotificationSubscriptionDetailDTO) {
		try {
			if (CollectionUtils.isNotEmpty(userNotificationSubscriptionDetailDTO)) {
				userNotificationSubscriptionDetailDTO.forEach(userNotificationSubDetail -> {
					UserDTO subscribedPerson = userNotificationSubDetail.getSubscribedPerson();
					if (Optional.ofNullable(subscribedPerson.getId()).isPresent()) {
						if (StringUtils.isNumeric(subscribedPerson.getId())) {
							PersonDTO employeeDetail = jestHelper.getExternalUser(subscribedPerson.getId());
							subscribedPerson.setType(NotificationApplicationConstants.SUBSCRIBED_USER_TYPE_EXTERNAL);
							setSubscribedPersonDetails(employeeDetail, subscribedPerson);

						} else {
							PersonDTO userDetail = eoiHelperService
									.fetchEmployeeDetailForUserId(subscribedPerson.getId());
							subscribedPerson.setType(NotificationApplicationConstants.SUBSCRIBED_USER_TYPE_INTERNAL);
							setSubscribedPersonDetails(userDetail, subscribedPerson);

						}

					}

				});
			}
		}catch (HttpClientErrorException exception) {
			log.error("Exception Occur during reterive the User details from EOI Service ProcessEnrichmentData:setUserDTO",exception);
		}
		

	}

	private void setSubscribedPersonDetails(PersonDTO personDto, UserDTO subscribedPerson) {
		if (Objects.nonNull(personDto)) {
			subscribedPerson.setFirstName(personDto.getFirstName());
			subscribedPerson.setJobTitle(personDto.getJobTitle());
			subscribedPerson.setContactType(personDto.getPersonType());
			subscribedPerson.setPreferredName(personDto.getPrefName());
			subscribedPerson.setLastName(personDto.getLastName());
		}
	}

	public PersonDTO getUserName(String userId) {
		PersonDTO userDetail = null;
		try {
			userDetail = eoiHelperService.fetchEmployeeDetailForUserId(userId);

		} catch (HttpClientErrorException exception) {
			log.error("Exception Occur during reterive the User details from EOI Service ProcessEnrichmentData:getUserName", exception);
		}
		return userDetail;

	}
	
	/**
	 * @param criteriaCode
	 * @param parameterDTO
	 * @param parameterId
	 * @param parameterMap
	 * @return
	 */
	private Map<String, String> tradingPartnerCriteria(String criteriaCode,
			String parameterId, Map<String, String> parameterMap) {
			TradingPartnerDTO response = notificationHelper.enrichTradingPartner(parameterId);
			parameterMap.put(criteriaCode, response.getTradingPartnerCode()+ "(" +response.getTradingPartnerDescription()+")");
		return parameterMap;
	}
	
	/**
	 * @param criteriaCode
	 * @param parameterDTO
	 * @param parameterId
	 * @param parameterMap
	 * @return
	 */
	public Map<String, String> orderOwnerCriteria(String criteriaCode,
			String parameterId, Map<String, String> parameterMap) {
		EmployeeProfileElasticIndexDTO employeeDTO = jestHelper.getEmployeeDetails(parameterId);
		parameterMap.put(criteriaCode, String.valueOf(employeeDTO.getFirstName())+ " "+String.valueOf(employeeDTO.getLastName()));
		return parameterMap;
	}
	
	/**
	 * @param criteriaCode
	 * @param parameterDTO
	 * @param parameterId
	 * @param parameterMap
	 * @return
	 */
	public Map<String, String> associatedUserCriteria(String criteriaCode, String parameterId,
			Map<String, String> parameterMap) {
		EmployeeProfileElasticIndexDTO employeeDTO = jestHelper.getEmployeeDetails(parameterId);
		parameterMap.put(criteriaCode, String.valueOf(employeeDTO.getFirstName())+ " "+String.valueOf(employeeDTO.getLastName()));
		return parameterMap;
	}

}