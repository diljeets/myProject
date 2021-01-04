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
public class PaymentRequestDetails {
    private String paymentMode;
    private String cardNumber;
    private String expiryDate;
    private String cvv;            
    private String channelCode;  

    public PaymentRequestDetails(){        
    }
    
    public PaymentRequestDetails(String paymentMode) {
        this.paymentMode = paymentMode;        
    }

    public PaymentRequestDetails(String paymentMode, String channelCode) {
        this.paymentMode = paymentMode;
        this.channelCode = channelCode;
    }
    
    public PaymentRequestDetails(String paymentMode, String cardNumber, String expiryDate, String cvv) {
        this.paymentMode = paymentMode;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
    
    
}
