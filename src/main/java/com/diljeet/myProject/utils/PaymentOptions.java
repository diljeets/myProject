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
public class PaymentOptions {

    private String paymentMode;
    private String displayName;
    private boolean isHybridDisabled;
    private boolean onboarding;
    private String priority;

    public PaymentOptions() {
    }

    public PaymentOptions(String paymentMode, String displayName) {
        this.paymentMode = paymentMode;
        this.displayName = displayName;
    }

    public PaymentOptions(String paymentMode, String displayName, boolean isHybridDisabled, boolean onboarding, String priority) {
        this.paymentMode = paymentMode;
        this.displayName = displayName;
        this.isHybridDisabled = isHybridDisabled;
        this.onboarding = onboarding;
        this.priority = priority;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isIsHybridDisabled() {
        return isHybridDisabled;
    }

    public void setIsHybridDisabled(boolean isHybridDisabled) {
        this.isHybridDisabled = isHybridDisabled;
    }

    public boolean isOnboarding() {
        return onboarding;
    }

    public void setOnboarding(boolean onboarding) {
        this.onboarding = onboarding;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
