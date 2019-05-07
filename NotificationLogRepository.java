package com.jbhunt.infrastructure.notification.repository;

import com.jbhunt.infrastructure.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "notificationlogs")
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Integer>,
		JpaSpecificationExecutor<NotificationLog>, QueryDslPredicateExecutor<NotificationLog> {

	@Query(value = "SELECT COUNT(NotificationLogID) FROM SUB.NotificationLog WHERE ISJSON(NotificationLogContent)>0"
			+ " AND NotificationSubscriptionDeliveryMethodCode='in-app'"
			+ " AND SubscribedUserID= ?1 AND ReadReceiptTimeStamp IS NULL"
			+ " AND DATEDIFF(DAY, CreateTimestamp, CURRENT_TIMESTAMP) <15", nativeQuery = true)
	public int findUnreadNotificationLogCountByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM SUB.NotificationLog WHERE ISJSON(NotificationLogContent)>0"
			+ " AND NotificationSubscriptionDeliveryMethodCode='in-app'"
			+ " AND SubscribedUserID= ?1 "
			+ " AND DATEDIFF(DAY, CreateTimestamp, CURRENT_TIMESTAMP) <15"
			+ " ORDER BY CreateTimestamp DESC", nativeQuery = true)
	public List<NotificationLog> findInAppNotificationLogByUserId(@Param("userId") String userId);

	@Query(value = "SELECT nl.* from SUB.NotificationLogResponse nlr, SUB.NotificationLog nl WHERE nl.NotificationLogID = nlr.NotificationLogID "
			+ "and nlr.LastUpdateTimestamp = (SELECT MAX(nlresponse.LastUpdateTimestamp) FROM SUB.NotificationLogResponse nlresponse "
			+ "WHERE nlresponse.NotificationLogID = nl.NotificationLogID) and nlr.NotificationLogResponseTypeReason = 'Queued' "
			+ "and nlr.CreateTimestamp < ?1", nativeQuery = true)
	public List<NotificationLog> findQueuedEmailResponse(String createdTimestamp);

}