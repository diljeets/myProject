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
public class UpdateTransaction {

    private String orderId;
    private String payableAmount;
    private String username;
    private String transactionToken;

    public UpdateTransaction() {
    }

    public UpdateTransaction(String orderId, String payableAmount, String username, String transactionToken) {
        this.orderId = orderId;
        this.payableAmount = payableAmount;
        this.username = username;
        this.transactionToken = transactionToken;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

}
