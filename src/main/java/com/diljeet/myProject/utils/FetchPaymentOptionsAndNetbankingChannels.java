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
public class FetchPaymentOptionsAndNetbankingChannels {

    private List<PaymentOptions> paymentOptions;
    private List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking;

    public FetchPaymentOptionsAndNetbankingChannels() {
    }

    public FetchPaymentOptionsAndNetbankingChannels(List<PaymentOptions> paymentOptions, List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking) {
        this.paymentOptions = paymentOptions;
        this.payChannelOptionsNetBanking = payChannelOptionsNetBanking;
    }

    public List<PaymentOptions> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(List<PaymentOptions> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public List<PayChannelOptionsNetBanking> getPayChannelOptionsNetBanking() {
        return payChannelOptionsNetBanking;
    }

    public void setPayChannelOptionsNetBanking(List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking) {
        this.payChannelOptionsNetBanking = payChannelOptionsNetBanking;
    }

}
