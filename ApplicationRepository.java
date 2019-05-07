package com.jbhunt.infrastructure.notification.repository;

import com.jbhunt.infrastructure.notification.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer>,
        JpaSpecificationExecutor<Application> {

    List<Application> findByDomainCodeOrderById(String domainCode);
}
