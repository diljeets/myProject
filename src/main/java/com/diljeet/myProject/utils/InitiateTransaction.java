/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.utils;

/**
 *
 * @author diljeet
 */
public class InitiateTransaction {
    
    private String payableAmount;
    private String orderId;
    private String username;
    private String channelId;
    private String callbackUrl;

    public InitiateTransaction(){        
    }
    
    public InitiateTransaction(String payableAmount, String orderId, String username, String channelId, String callbackUrl) {
        this.payableAmount = payableAmount;
        this.orderId = orderId;
        this.username = username;
        this.channelId = channelId;
        this.callbackUrl = callbackUrl;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    
    
}
