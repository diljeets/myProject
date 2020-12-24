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
public class CardDetails {
    private String issuingBank;
    private String issuingBankCode;
    private String paymentMode;
    private String channelName;
    private String channelCode;
    private String isCvvRequired;
    private String isExpRequired;
    private String isActive;
    private String hasLowSuccessRateStatus;
    private String hasLowSuccessRateMsg;
    private String cardIconUrl;

    public CardDetails(){        
    }
    
    public CardDetails(String issuingBank, String issuingBankCode, String paymentMode, String channelName, String channelCode, String isCvvRequired, String isExpRequired, String isActive, String hasLowSuccessRateStatus, String hasLowSuccessRateMsg, String cardIconUrl) {
        this.issuingBank = issuingBank;
        this.issuingBankCode = issuingBankCode;
        this.paymentMode = paymentMode;
        this.channelName = channelName;
        this.channelCode = channelCode;
        this.isCvvRequired = isCvvRequired;
        this.isExpRequired = isExpRequired;
        this.isActive = isActive;
        this.hasLowSuccessRateStatus = hasLowSuccessRateStatus;
        this.hasLowSuccessRateMsg = hasLowSuccessRateMsg;
        this.cardIconUrl = cardIconUrl;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getIssuingBankCode() {
        return issuingBankCode;
    }

    public void setIssuingBankCode(String issuingBankCode) {
        this.issuingBankCode = issuingBankCode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getIsCvvRequired() {
        return isCvvRequired;
    }

    public void setIsCvvRequired(String isCvvRequired) {
        this.isCvvRequired = isCvvRequired;
    }

    public String getIsExpRequired() {
        return isExpRequired;
    }

    public void setIsExpRequired(String isExpRequired) {
        this.isExpRequired = isExpRequired;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getHasLowSuccessRateStatus() {
        return hasLowSuccessRateStatus;
    }

    public void setHasLowSuccessRateStatus(String hasLowSuccessRateStatus) {
        this.hasLowSuccessRateStatus = hasLowSuccessRateStatus;
    }

    public String getHasLowSuccessRateMsg() {
        return hasLowSuccessRateMsg;
    }

    public void setHasLowSuccessRateMsg(String hasLowSuccessRateMsg) {
        this.hasLowSuccessRateMsg = hasLowSuccessRateMsg;
    }

    public String getCardIconUrl() {
        return cardIconUrl;
    }

    public void setCardIconUrl(String cardIconUrl) {
        this.cardIconUrl = cardIconUrl;
    }
    
    
}
