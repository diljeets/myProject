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
public class FetchBinDetails {

    private String orderId;
    private String firstSixCardDigits;
    private String transactionToken;

    public FetchBinDetails() {
    }

    public FetchBinDetails(String orderId, String firstSixCardDigits, String transactionToken) {
        this.orderId = orderId;
        this.firstSixCardDigits = firstSixCardDigits;
        this.transactionToken = transactionToken;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFirstSixCardDigits() {
        return firstSixCardDigits;
    }

    public void setFirstSixCardDigits(String firstSixCardDigits) {
        this.firstSixCardDigits = firstSixCardDigits;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

}
