/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.utils;

import java.util.List;

/**
 *
 * @author diljeet
 */
public class FetchBalanceAndInstruments {

    private List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance;
    private List<SavedInstruments> savedInstruments;

    public FetchBalanceAndInstruments() {
    }

    public FetchBalanceAndInstruments(List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance, List<SavedInstruments> savedInstruments) {
        this.payChannelOptionsPaytmBalance = payChannelOptionsPaytmBalance;
        this.savedInstruments = savedInstruments;
    }

    public List<PayChannelOptionsPaytmBalance> getPayChannelOptionsPaytmBalance() {
        return payChannelOptionsPaytmBalance;
    }

    public void setPayChannelOptionsPaytmBalance(List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance) {
        this.payChannelOptionsPaytmBalance = payChannelOptionsPaytmBalance;
    }

    public List<SavedInstruments> getSavedInstruments() {
        return savedInstruments;
    }

    public void setSavedInstruments(List<SavedInstruments> savedInstruments) {
        this.savedInstruments = savedInstruments;
    }

}
