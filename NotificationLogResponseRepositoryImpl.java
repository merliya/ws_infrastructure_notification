package com.jbhunt.infrastructure.notification.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.jbhunt.infrastructure.notification.entity.NotificationLogResponse;

@Repository
public  class NotificationLogResponseRepositoryImpl {

@Inject	
private EntityManager entityManager;


public List<NotificationLogResponse> findEDIResendCriterias(String queryValue,Map<String,String> criteriaFilter){
	
Query query;	
String sql = "select * from SUB.NotificationLogResponse WHERE " + queryValue;
query = entityManager.createNativeQuery(sql);
Set<String> criteriaFilterKeys = criteriaFilter.keySet();
criteriaFilterKeys.forEach((key)-> {
query.setParameter(key, criteriaFilter.get(key));;
});
List<NotificationLogResponse> notificationLogResponse = query.getResultList();
return notificationLogResponse;
}




}