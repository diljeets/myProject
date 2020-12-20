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
public class PayChannelOptionsNetBanking {
    
    private boolean isHybridDisabled;
    private String channelName;
    private String iconUrl;        
    private String channelCode;

    public PayChannelOptionsNetBanking(){        
    }
    
    public PayChannelOptionsNetBanking(boolean isHybridDisabled, String channelName, String iconUrl, String channelCode) {
        this.isHybridDisabled = isHybridDisabled;
        this.channelName = channelName;
        this.iconUrl = iconUrl;
        this.channelCode = channelCode;
    }

    public boolean isIsHybridDisabled() {
        return isHybridDisabled;
    }

    public void setIsHybridDisabled(boolean isHybridDisabled) {
        this.isHybridDisabled = isHybridDisabled;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
    
}
