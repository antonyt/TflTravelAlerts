package com.tfltravelalerts.notification;

/**
*
* This request class is used to signal that we want
* to register for push notifications (aka GCM)
*
*/
public class RegisterForPushNotificationsRequest {
    
    final private String lines; 
    public RegisterForPushNotificationsRequest(String lines) {
        this.lines = lines;
    }

    public String getLinesString() {
        return lines;
    }
}
