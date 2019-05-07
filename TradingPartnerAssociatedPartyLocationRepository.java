package com.jbhunt.infrastructure.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.jbhunt.infrastructure.notification.entity.TradingPartnerAssociatedPartyLocation;

public interface TradingPartnerAssociatedPartyLocationRepository
        extends JpaRepository<TradingPartnerAssociatedPartyLocation, Integer>,
        JpaSpecificationExecutor<TradingPartnerAssociatedPartyLocation>,
        QueryDslPredicateExecutor<TradingPartnerAssociatedPartyLocation> {

}
