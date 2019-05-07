package com.jbhunt.infrastructure.notification.helper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.jbhunt.hrms.eoi.api.dto.person.PersonDTO;
import com.jbhunt.infrastructure.notification.properties.NotificationElasticProperties;
import com.jbhunt.infrastructure.usernotification.subscription.dto.ExternalUserDetailDTO;
import com.jbhunt.personnel.schedule.dto.EmployeeProfileElasticIndexDTO;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JestHelper {

	private final JestClient jestClient;
	private final NotificationElasticProperties notificationElasticProperties;

	public JestHelper(JestClient jestClient, NotificationElasticProperties notificationElasticProperties) {
		this.jestClient = jestClient;
		this.notificationElasticProperties = notificationElasticProperties;
	}

	public PersonDTO getExternalUser(String externalUserId) {
		log.info("Jest client method to get external user information");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.boolQuery()
				.must(QueryBuilders.matchQuery(notificationElasticProperties.getElasticProperty(), externalUserId)));
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex(notificationElasticProperties.getElasticSearchIndex())
				.addType(notificationElasticProperties.getElasticSearchType()).build();
		List<ExternalUserDetailDTO> personDTOList = fetchRecordsFromElastic(search, jestClient);
		PersonDTO personDTO = new PersonDTO();
		if (personDTOList != null && CollectionUtils.isNotEmpty(personDTOList)) {
			ExternalUserDetailDTO user = personDTOList.get(0);
			personDTO.setPrefName(user.getName());
			personDTO.setFirstName(user.getName());
		}
		return personDTO;
	}

	public static List<ExternalUserDetailDTO> fetchRecordsFromElastic(Search search, JestClient jestClient) {
		List<ExternalUserDetailDTO> personDTOList = null;
		try {
			JestResult result = jestClient.execute(search);
			personDTOList = result.getSourceAsObjectList(ExternalUserDetailDTO.class);
			log.info("NotificationHelper ::fetchRecordsFromElastic: personDTOList.size() :: " + personDTOList.size());

		} catch (IOException e) {
			log.error("NotificationHelper::fetchRecordsFromElastic : ", e);
		}

		return personDTOList;

	}
	
	public EmployeeProfileElasticIndexDTO getEmployeeDetails(String employeeId){
		EmployeeProfileElasticIndexDTO employeeDTO = null;
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
        		.must(QueryBuilders.matchQuery(notificationElasticProperties.getEmployeeProperty(), employeeId)));
		Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(notificationElasticProperties.getEmployeeIndex())
                .addType(notificationElasticProperties.getEmployeeType()).build();
		List<EmployeeProfileElasticIndexDTO> employeeDTOList = fetchEmployeeFromElastic(search, jestClient);
		if (employeeDTOList != null && CollectionUtils.isNotEmpty(employeeDTOList)) {
			employeeDTO = employeeDTOList.get(0);
		}
        return employeeDTO;
    }
	
	public static List<EmployeeProfileElasticIndexDTO> fetchEmployeeFromElastic(Search search, JestClient jestClient) {
		List<EmployeeProfileElasticIndexDTO> employeeDTOList = null;
		try {
			JestResult result = jestClient.execute(search);
			ObjectMapper objectMapper = new ObjectMapper();
			JavaTimeModule javaTimeModule = new JavaTimeModule();
			javaTimeModule.addDeserializer(LocalDateTime.class,
					new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
			objectMapper.registerModule(javaTimeModule);
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			employeeDTOList = objectMapper.readValue(
					objectMapper.writeValueAsString(result.getSourceAsObjectList(Object.class)),
					new TypeReference<List<EmployeeProfileElasticIndexDTO>>() {
					});

		} catch (IOException e) {
			log.error("NotificationHelper::fetchRecordsFromElastic : ", e);
		}

		return employeeDTOList;

	}

}
