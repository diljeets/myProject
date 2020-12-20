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
public class PayChannelOptionsPaytmBalance {    
    
    private String balance;
    private String displayName;        
    private String imageUrl;

    public PayChannelOptionsPaytmBalance(){        
    }

    public PayChannelOptionsPaytmBalance(String balance, String displayName, String imageUrl) {
        this.balance = balance;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    
    
}
