package com.jbhunt.infrastructure.notification.repository;

import com.jbhunt.infrastructure.notification.entity.JBHEmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailTemplateRepository extends JpaRepository<JBHEmailTemplate, Integer> {

    @Query(value = "SELECT * FROM SUB.JBHEmailTemplate WHERE EmailTemplateName = ?1", nativeQuery = true)
    JBHEmailTemplate findEmailTemplateByName(@Param("templateName") String templateName);
}
