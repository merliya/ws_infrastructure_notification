package com.jbhunt.infrastructure.notification.repository;


import com.jbhunt.infrastructure.notification.entity.NotificationLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "inlineUserNotificationSubscriptionDetail", types = NotificationLog.class)
public interface InlineUserNotificationSubscriptionDetailProjection {

    @Value("#{target.userNotificationSubscriptionDetail?.userNotificationSubscriptionDetailID ?:0}")
    int getUserNotificationSubscriptionDetailId();
    int getNotificationLogID();
    String getNotificationLogContent();
    LocalDateTime getReadReceiptTimestamp();
    String getSubscribedUserID();
    LocalDateTime getLastUpdateTimestampString();

}
