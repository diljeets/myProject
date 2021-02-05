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
public class SavedInstruments {

    private String iconUrl;
    private String issuingBank;
    private String displayName;
    private String priority;
//    private String paymentOfferDetails;
//    private String savedCardEmisubventionDetail;
    private String channelCode;
    private String channelName;
    private boolean oneClickSupported;
    private boolean isEmiAvailable;
    private boolean isHybridDisabled;
    private boolean isEmiHybridDisabled;
    private String authMode;
    private String cardId;
    private String cardType;
    private String expiryDate;
    private String firstSixDigit;
    private String lastFourDigit;
    private String status;
    private String cvvLength;
    private boolean cvvRequired;
    private boolean indian;

    public SavedInstruments() {
    }

    public SavedInstruments(String iconUrl, String issuingBank, String displayName, String priority, String channelCode, String channelName, boolean oneClickSupported, boolean isEmiAvailable, boolean isHybridDisabled, boolean isEmiHybridDisabled, String authMode, String cardId, String cardType, String expiryDate, String firstSixDigit, String lastFourDigit, String status, String cvvLength, boolean cvvRequired, boolean indian) {
        this.iconUrl = iconUrl;
        this.issuingBank = issuingBank;
        this.displayName = displayName;
        this.priority = priority;
//        this.paymentOfferDetails = paymentOfferDetails;
//        this.savedCardEmisubventionDetail = savedCardEmisubventionDetail;
        this.channelCode = channelCode;
        this.channelName = channelName;
        this.oneClickSupported = oneClickSupported;
        this.isEmiAvailable = isEmiAvailable;
        this.isHybridDisabled = isHybridDisabled;
        this.isEmiHybridDisabled = isEmiHybridDisabled;
        this.authMode = authMode;
        this.cardId = cardId;
        this.cardType = cardType;
        this.expiryDate = expiryDate;
        this.firstSixDigit = firstSixDigit;
        this.lastFourDigit = lastFourDigit;
        this.status = status;
        this.cvvLength = cvvLength;
        this.cvvRequired = cvvRequired;
        this.indian = indian;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

//    public String getPaymentOfferDetails() {
//        return paymentOfferDetails;
//    }
//
//    public void setPaymentOfferDetails(String paymentOfferDetails) {
//        this.paymentOfferDetails = paymentOfferDetails;
//    }
//
//    public String getSavedCardEmisubventionDetail() {
//        return savedCardEmisubventionDetail;
//    }
//
//    public void setSavedCardEmisubventionDetail(String savedCardEmisubventionDetail) {
//        this.savedCardEmisubventionDetail = savedCardEmisubventionDetail;
//    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isOneClickSupported() {
        return oneClickSupported;
    }

    public void setOneClickSupported(boolean oneClickSupported) {
        this.oneClickSupported = oneClickSupported;
    }

    public boolean isIsEmiAvailable() {
        return isEmiAvailable;
    }

    public void setIsEmiAvailable(boolean isEmiAvailable) {
        this.isEmiAvailable = isEmiAvailable;
    }

    public boolean isIsHybridDisabled() {
        return isHybridDisabled;
    }

    public void setIsHybridDisabled(boolean isHybridDisabled) {
        this.isHybridDisabled = isHybridDisabled;
    }

    public boolean isIsEmiHybridDisabled() {
        return isEmiHybridDisabled;
    }

    public void setIsEmiHybridDisabled(boolean isEmiHybridDisabled) {
        this.isEmiHybridDisabled = isEmiHybridDisabled;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFirstSixDigit() {
        return firstSixDigit;
    }

    public void setFirstSixDigit(String firstSixDigit) {
        this.firstSixDigit = firstSixDigit;
    }

    public String getLastFourDigit() {
        return lastFourDigit;
    }

    public void setLastFourDigit(String lastFourDigit) {
        this.lastFourDigit = lastFourDigit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCvvLength() {
        return cvvLength;
    }

    public void setCvvLength(String cvvLength) {
        this.cvvLength = cvvLength;
    }

    public boolean isCvvRequired() {
        return cvvRequired;
    }

    public void setCvvRequired(boolean cvvRequired) {
        this.cvvRequired = cvvRequired;
    }

    public boolean isIndian() {
        return indian;
    }

    public void setIndian(boolean indian) {
        this.indian = indian;
    }

}
