package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;

@RepositoryRestResource(path = "edinotificationsubscriptions")
public interface EDINotificationSubscriptionRepository  extends JpaRepository<EDINotificationSubscription, Integer>,
    JpaSpecificationExecutor<EDINotificationSubscription>, QueryDslPredicateExecutor<EDINotificationSubscription> {
    
    @Query(value="select count(e) from SUB.SHIPMENTNOTIFICATIONSUBSCRIPTION e where e.TradingPartnerCode = :tradingPartnerCode", nativeQuery = true)
    boolean isSubscriptionExists(@Param("tradingPartnerCode") String tradingPartnerCode);
    
}
