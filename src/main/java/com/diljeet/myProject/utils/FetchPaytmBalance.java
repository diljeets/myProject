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
public class FetchPaytmBalance {

    private String orderId;
    private String transactionToken;

    public FetchPaytmBalance() {
    }

    public FetchPaytmBalance(String orderId, String transactionToken) {
        this.orderId = orderId;
        this.transactionToken = transactionToken;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

}
