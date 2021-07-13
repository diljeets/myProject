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

    private String orderId;
    private String paymentMode;
    private String cardId;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String saveCard;
    private String channelCode;
    private String transactionToken;

    public PaymentRequestDetails() {
    }

    public PaymentRequestDetails(String orderId, String paymentMode, String transactionToken) {
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.transactionToken = transactionToken;
    }

    public PaymentRequestDetails(String orderId, String paymentMode, String channelCode, String transactionToken) {
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.channelCode = channelCode;
        this.transactionToken = transactionToken;
    }

    public PaymentRequestDetails(String orderId, String paymentMode, String cardId, String cvv, String transactionToken) {
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.cardId = cardId;
        this.cvv = cvv;
        this.transactionToken = transactionToken;
    }

    public PaymentRequestDetails(String orderId, String paymentMode, String cardNumber, String expiryDate, String cvv, String saveCard, String transactionToken) {
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.saveCard = saveCard;
        this.transactionToken = transactionToken;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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

    public String getSaveCard() {
        return saveCard;
    }

    public void setSaveCard(String saveCard) {
        this.saveCard = saveCard;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

}
