package com.jbhunt.infrastructure.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.jbhunt.infrastructure.notification.entity.ShipmentNotificationStopReasonEventTypeAssociation;

@RepositoryRestResource(path = "shipmentnotificationstopreasoneventtypeassociations")
public interface ShipmentNotificationStopReasonEventTypeAssociationRepository extends JpaRepository<ShipmentNotificationStopReasonEventTypeAssociation, Integer>,
JpaSpecificationExecutor<ShipmentNotificationStopReasonEventTypeAssociation>, QueryDslPredicateExecutor<ShipmentNotificationStopReasonEventTypeAssociation>{
	
	List<ShipmentNotificationStopReasonEventTypeAssociation> findByShipmentNotificationStopTypeShipmentNotificationStopTypeCode(@Param("stopType") String stopType);

	List<ShipmentNotificationStopReasonEventTypeAssociation> findByShipmentNotificationStopTypeShipmentNotificationStopTypeCodeAndStopReasonCode(
			@Param("stopType") String stopType, @Param("stopReason") String stopReason); 
}
