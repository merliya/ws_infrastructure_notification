package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.ShipmentNotificationStopType;

@RepositoryRestResource(path = "shipmentnotificationstoptypes")
public interface ShipmentNotificationStopTypeRepository extends JpaRepository<ShipmentNotificationStopType, String>,
		JpaSpecificationExecutor<ShipmentNotificationStopType>{

}
