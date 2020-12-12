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
    private String channelId;
    private String callbackUrl;

    public InitiateTransaction(){        
    }
    
    public InitiateTransaction(String payableAmount, String channelId, String callbackUrl) {
        this.payableAmount = payableAmount;
        this.channelId = channelId;
        this.callbackUrl = callbackUrl;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
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
