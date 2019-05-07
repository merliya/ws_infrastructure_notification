package com.jbhunt.infrastructure.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jbhunt.infrastructure.notification.entity.NotificationParameter;

@RepositoryRestResource(path = "notificationparameters")
public interface NotificationParameterRepository extends JpaRepository<NotificationParameter, Integer>,
		JpaSpecificationExecutor<NotificationParameter>, QueryDslPredicateExecutor<NotificationParameter> {

	NotificationParameter findByNotificationParameterDescriptionEquals(String notificationParameterDescription);

	List<NotificationParameter> findByNotificationParameterDescriptionAndNotificationTypeNotificationTypeID(
			String userNotificationSubscriptionParameterCode, int notificationTypeID);
	
	public static String DISTINCT_NOTIFICATION_PARAMETERS_QUERY = "SELECT * FROM"
            +"(SELECT np.NotificationTypeID, "
            + "np.NotificationParameterID,"
            + "np.NotificationParameterDescription, "
            + "np.EffectiveTimestamp, "
            + "np.ExpirationTimestamp, "
            + "np.CreateTimestamp, "
            + "np.CreateUserID, "
            + "np.CreateProgramName, "
            + "np.LastUpdateTimestamp, "
            + "np.LastUpdateUserID, "
            + "np.LastUpdateProgramName, "
            + "ROW_NUMBER() OVER(PARTITION BY np.NotificationParameterDescription ORDER BY np.NotificationParameterDescription) AS rowNumber "
            + "FROM SUB.NotificationParameter AS np ";
            
	@Query(value=DISTINCT_NOTIFICATION_PARAMETERS_QUERY
			+ "JOIN SUB.NotificationType AS nt ON np.NotificationTypeID = nt.NotificationTypeID "
			+ "JOIN SUB.NotificationCategory AS nc ON nt.NotificationCategoryCode = nc.NotificationCategoryCode AND nc.NotificationCategoryDescription =:notificationCategory "
			+ "JOIN SUB.NotificationSubCategory AS nsc ON nt.NotificationSubCategoryCode = nsc.NotificationSubCategoryCode AND nsc.NotificationSubCategoryDescription =:notificationSubCategory "
			+ "AND np.ExpirationTimestamp >= CURRENT_TIMESTAMP ) AS dynaTable "
			+ "WHERE dynaTable.rowNumber = 1", nativeQuery=true)
	List<NotificationParameter> findDistinctNotificationParameter(@Param("notificationCategory") String notificationCategory,
			@Param("notificationSubCategory") String notificationSubCategory);
	
	@Query(value=DISTINCT_NOTIFICATION_PARAMETERS_QUERY
            + "WHERE np.ExpirationTimestamp >= CURRENT_TIMESTAMP ) AS dynaTable "
            + "WHERE dynaTable.rowNumber = 1", nativeQuery=true)
    List<NotificationParameter> findDistinctNotificationParameter();
}
