package com.jbhunt.infrastructure.notification.constants;

public enum NotificationTypeEnum {
    SHIPMENT_STATUS("214"), LOAD_TENDER("990");
    private String code;
    private NotificationTypeEnum(String code) {
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
}
