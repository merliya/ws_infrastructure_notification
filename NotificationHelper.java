package com.jbhunt.infrastructure.notification.helper;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jbhunt.edi.tradingpartners.dto.TradingPartnerDTO;
import com.jbhunt.infrastructure.notification.properties.EnrichmentProperties;
import com.jbhunt.infrastructure.taskassignment.entity.TaskAssignment;
import com.jbhunt.mdm.dto.CustomerDetailsDTO;
import com.jbhunt.mdm.dto.LocationProfileDTO;
import com.jbhunt.mdm.dto.LocationProfileDTOs;
import com.jbhunt.mdm.dto.MarketingAreaDTO;
import com.jbhunt.mdm.dto.ProfileContactDTO;
import com.jbhunt.ordermanagement.order.entity.Order;
import com.jbhunt.referencedata.entity.FinanceBusinessUnitServiceOfferingAssociation;
import com.jbhunt.referencedata.entity.ServiceOfferingBusinessUnitTransitModeAssociation;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationHelper {
	private static final String FINANCE_BUSINESS_UNIT_CODE = "financeBusinessUnitCode";

	private final RestTemplate restDataTemplate;
	private final EnrichmentProperties enrichmentProperties;
	private static final String BILL_TO_CODE = "billtocode";
	private static final String ACTIVE = "active";
	private static final String ROLE_TYPE = "roletype";
	private static final String LOB_ID = "lobid";
	private static final String BILL_TO_CUSTOMER = "/billtocustomers";

	public NotificationHelper(@Qualifier("restDataTemplate") RestTemplate restDataTemplate,
			EnrichmentProperties enrichmentProperties) {
		this.restDataTemplate = restDataTemplate;
		this.enrichmentProperties = enrichmentProperties;
	}

	/**
	 * REST Client call to get the Solicitor details
	 * 
	 * @param id
	 * @return
	 */
	@HystrixCommand
	public List<ProfileContactDTO> getSolictorDetails(String id) {
		log.debug("Profile client method to get the solictor details");

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromHttpUrl(enrichmentProperties.getAccountBaseURL() + enrichmentProperties.getProfileSearchURL())
				.queryParam("id", id).queryParam(ROLE_TYPE, "Bill To").queryParam(ACTIVE, "yes")
				.queryParam("contacttype", "Solicitor");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<List<ProfileContactDTO>> response = restDataTemplate.exchange(
				uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<ProfileContactDTO>>() {
				});
		return getResponseBody(response);

	}

	/**
	 * Client service to get Bill To Details
	 * 
	 * @param billToID
	 *            Integer
	 * @return
	 */
	@HystrixCommand
	public List<CustomerDetailsDTO> findAllCustomerDetailsByBillToCode(String billToID) {
		log.debug("Profile Client method to get customer details associated to bill to customer");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(BILL_TO_CODE, billToID);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>("parameter", headers);
		StringJoiner url = new StringJoiner(StringUtils.EMPTY);
		url.add(enrichmentProperties.getAccountBaseURL());
		url.add(enrichmentProperties.getCustomerURL());
		url.add(billToID);
		url.add(BILL_TO_CUSTOMER);
		String urlString = url.toString();
		ResponseEntity<List<CustomerDetailsDTO>> response = restDataTemplate.exchange(urlString, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<CustomerDetailsDTO>>() {
				});
		log.info("" + response);
		return getResponseBody(response);

	}

	/**
	 * Client service to get Line Of Business Details
	 * 
	 * @param billToID
	 *            Integer
	 * @return
	 */
	@HystrixCommand
	public List<CustomerDetailsDTO> findAllCustomerDetailsByLineOfBussiness(String lobId) {
		log.debug("Profile Client method to get customer details associated to bill to customer");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(LOB_ID, lobId);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>("parameter", headers);
		StringJoiner url = new StringJoiner(StringUtils.EMPTY);
		url.add(enrichmentProperties.getAccountBaseURL());
		url.add(enrichmentProperties.getCustomerURL());
		url.add(lobId);
		String urlString = url.toString();
		ResponseEntity<List<CustomerDetailsDTO>> response = restDataTemplate.exchange(urlString, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<CustomerDetailsDTO>>() {
				});
		log.info("" + response);
		return getResponseBody(response);

	}

	/**
	 * Client service to get the Origin & Destination Marketing Area
	 * 
	 * @param Id
	 * @return
	 */
	@HystrixCommand
	public List<MarketingAreaDTO> enrichOriginDestination(final String marketingId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final Map<String, String> parameter = new HashMap<>();
		ResponseEntity<List<MarketingAreaDTO>> response = null;
		parameter.put("Id", marketingId);
		response = restDataTemplate.exchange(
				enrichmentProperties.getLocationBaseURL() + enrichmentProperties.getMarketingAreaURL() + "{Id}",
				HttpMethod.GET, entity, new ParameterizedTypeReference<List<MarketingAreaDTO>>() {
				}, parameter);

		log.debug("enrichOriginDestination :: response got from mdm virtual services :" + response);
		return getResponseBody(response);
	}
	
	/**
	 * Client service to get the Origin & Destination Marketing Area
	 * 
	 * @param Id
	 * @return
	 */
	@HystrixCommand
	public TradingPartnerDTO enrichTradingPartner(final String tradingPartnerCode) {
		TradingPartnerDTO tradingPartner = null;
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final Map<String, String> parameter = new HashMap<>();
		ResponseEntity<List<TradingPartnerDTO>> response = null;
		parameter.put("Id", tradingPartnerCode);
		response = restDataTemplate.exchange(
				enrichmentProperties.getTradingPartnerBaseURL() + "{Id}" + "&"+enrichmentProperties.getTradingPartnerDocType(),
				HttpMethod.GET, entity, new ParameterizedTypeReference<List<TradingPartnerDTO>>() {
				}, parameter);
		List<TradingPartnerDTO> tradingPartnerList =getResponseBody(response);
		if (CollectionUtils.isNotEmpty(tradingPartnerList)) {
			tradingPartner = tradingPartnerList.stream().findFirst().get();
        }
		log.debug("enrichTradingPartner :: response got from edi virtual services :" + response);
		return tradingPartner;
	}

	/**
	 * Client call to get the SHIPPER/RECEIVER details
	 * 
	 * @param locationID
	 * @return
	 * @throws JSONException
	 */
	@HystrixCommand
	public LocationProfileDTO findLocationProfilebyLocationCode(String locationID)  {
		log.debug("Profile client method to find location profile based on location code ");
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("locationid", locationID);
		ResponseEntity<LocationProfileDTOs> response = restDataTemplate.exchange(
				enrichmentProperties.getLocationBaseURL() + enrichmentProperties.getLocationURL() + locationID,
				HttpMethod.GET, entity, LocationProfileDTOs.class);

		return Optional.ofNullable(getResponseBody(response))
				.map(LocationProfileDTOs::getLocationProfileDTO).orElse(null);

	}

	@HystrixCommand
	public Resources<ServiceOfferingBusinessUnitTransitModeAssociation> findBusinessUnitServiceOffering(
			String financeBusinessUnitCode) throws URISyntaxException {
		log.info("Reference data client method to find the Business Unit by Service Code and Transit Code");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put(FINANCE_BUSINESS_UNIT_CODE, financeBusinessUnitCode);
		ResponseEntity<Resources<ServiceOfferingBusinessUnitTransitModeAssociation>> response = restDataTemplate
				.exchange(enrichmentProperties.getReferenceBaseURL() + enrichmentProperties.getServiceOffering(),
						HttpMethod.GET, null,
						new ParameterizedTypeReference<Resources<ServiceOfferingBusinessUnitTransitModeAssociation>>() {
						}, parameter);

		return getResponseBody(response);

	}

	@HystrixCommand 
	public Resources<FinanceBusinessUnitServiceOfferingAssociation> findAllBusinessUnit() {
		log.info("Enterprise Reference client method to find all Bussiness Unit Service Offering");
		ResponseEntity<Resources<FinanceBusinessUnitServiceOfferingAssociation>> response = restDataTemplate.exchange(
				enrichmentProperties.getReferenceBaseURL() + enrichmentProperties.getBusinessUnit(), HttpMethod.GET,
				null, new ParameterizedTypeReference<Resources<FinanceBusinessUnitServiceOfferingAssociation>>() {
				}, Collections.emptyMap());
		return getResponseBody(response);
	}

	@HystrixCommand 
	public Order findOrderByOrderId(String orderID) {
		log.info("order client : To find Order by OrderId");
		ResponseEntity<Order> response = restDataTemplate.getForEntity(enrichmentProperties.getOrderURL() + orderID,
				Order.class);

		return getResponseBody(response);

	}
	
	@HystrixCommand
	public TaskAssignment findTaskAssignmentById(String taskAssignmentId)  {
		log.debug("Profile client method to find location profile based on location code ");
		ResponseEntity<TaskAssignment> response = restDataTemplate.exchange(
				enrichmentProperties.getTaskAssignmentBaseURL() + enrichmentProperties.getTaskAssignmentURL() + taskAssignmentId,
				HttpMethod.GET,null,TaskAssignment.class);
		log.info("TaskAssignmentEnrich" + response);
		return getResponseBody(response);
	}
	
	/**
	 * @param object
	 * @return
	 */
	private static <T> T getResponseBody(ResponseEntity<T> object) {
		T responseBody = null;
		if (Optional.ofNullable(object).isPresent()) {
			responseBody = object.getBody();
		}
		return responseBody;
	}

}
