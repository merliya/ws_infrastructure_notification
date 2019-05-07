package com.jbhunt.infrastructure.notification.constants;

public class NotificationApplicationConstants {

	public static final String APPLICATION_NAME_PLATFORM = "Platform Notifications";
	public static final String SUBSCRIBED_USER_TYPE_INTERNAL = "Internal";
	public static final String SUBSCRIBED_USER_TYPE_EXTERNAL = "External";
	public static final String INTERNAL_ONLY_NOTIFICATION_TYPE = "Internal Only";
	public static final String EMAILINT = "EmailInt";
	
	public static final String ACTIVE_STATUS = "Active";
	public static final String INACTIVE_STATUS = "Inactive";
	
	public static final String MEDIA_TYPE_JSON = "application/hal+json, application/json";
	
	public static final String NATIONAL_ACCOUNT="National Account";
	public static final String SHIPPER="Origin";
	public static final String RECEIVER ="Destination";
	public static final String INTERMEDIATE = "Intermediate Stop";
	public static final String BILL_TO="Bill To";
	public static final String LINE_OF_BUSINESS= "Line of Business";
	public static final String ORDER_NUMBER= "Order Number";
	public static final String ORIGIN_MARKETING_AREA="Origin Marketing Area";
	public static final String DESTINATION_MARKETING_AREA="Destination Marketing Area";
	public static final String ORIGIN_CAPACITY_AREA="Origin Capacity Area";
	public static final String DESTINATION_CAPACITY_AREA="Destination Capacity Area";
	public static final String CORPORATE_ACCOUNT ="Corporate Account";
	public static final String SOLICITOR ="Solicitor";

	public static final String INAPP_HEADER_USER_ID = "userId";
	public static final String INAPP_HEADER_SUBSCRIPTION_DETAIL_ID = "subscriptionDetailId";
	public static final String BUSINESS_UNIT = "Business Unit";
	public static final String SERVICE_OFFERING = "Service Offering";
	public static final String ORDER_OWNER = "Order Owner";
	public static final String TRADING_PARTNER = "Trading Partner";
 
	public static final String TASK_ASS="Task Assignment";
	public static final String DAYS_EXPIRY="Days Till Expiration";
	public static final String WEEKS_LST_EFF_DATE="Weeks From Latest Active Effective Date";
	public static final String SCAC="SCAC";
	public static final String ASSOCIATED_USER="Associated User";
    public static final short NUMBER_99 = 99;
    
    public static final String LOCATION_PROFILE_DTO="locationProfileDTO";

	private NotificationApplicationConstants() {
		// Intentionally left blank
	}



	 public static final String ORDER_NUMBER_CRITERIA = "orderNumber";
	 public static final String STOP_NUMBER= "stopNumber";
	 public static final String REFERENCE_VALUE = "referenceValue";
	 public static final String PROCESSED_DATE = "processedDateTime";
	 public static final String EVENT_SUB_TYPE = "eventSubType";
	 public static final String EDITSUCCESS = "Record Edited Successfully";
	 public static final String SUCCESSSTATUS = "Success";
	 public static final String SUBSCRIPTION_CREATOR = "Notification Creator";
	 public static final String CONTACT_TYPE = "ContactType";
	 public static final String UNSUBSCRIBE_URL = "UnsubscribeUrl";
	 public static final String CURRENT_YEAR = "CurrentYear";

}
