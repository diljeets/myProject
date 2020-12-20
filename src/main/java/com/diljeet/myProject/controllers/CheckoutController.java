/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.CheckoutBean;
import com.diljeet.myProject.ejb.PaymentGatewayBean;
import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.interfaces.CheckoutService;
import com.diljeet.myProject.utils.CardBinDetails;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author diljeet
 */
@Named(value = "checkoutController")
@SessionScoped
public class CheckoutController implements Serializable {

    private static final Logger logger = Logger.getLogger(CheckoutController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private CustomerOrder customerOrder;
    private String deliveryTime;
    private String deliveryAddress;
    private PaymentOptions paymentOption;
    private List<PaymentOptions> paymentOptions;
    private PayChannelOptionsPaytmBalance payChannelOptionPaytmBalance;
    private List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance;
    private PayChannelOptionsNetBanking payChannelOptionNetBanking;
    private List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking;
    private boolean isModePaytm;
    private String paytmMobile;
    private String otp;    
    private boolean isModeCC;
    private String creditcardNumber;
    private boolean isCardValid;
    private String issuingBank;
    private String channelName;
    private String isCvvRequired;
    private String isExpRequired;
    private String cardIconUrl;
    private String isCardActive;
    private boolean isModeDC;
    private boolean isModeNB;

    @Inject
    CartController cartController;

    @EJB
    CheckoutBean checkoutBean;

    @EJB
    CheckoutService checkoutService;

    @EJB
    PaymentGatewayBean paymentGatewayBean;

    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public CheckoutController() {
        customerOrder = new CustomerOrder();
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryAddress() {
        return checkoutService.getDeliveryAddress();
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PaymentOptions getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(PaymentOptions paymentOption) {
        this.paymentOption = paymentOption;
    }

    public List<PaymentOptions> getPaymentOptions() {
        return checkoutBean.fetchPaymentOptions();
    }

    public void setPaymentOptions(List<PaymentOptions> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public PayChannelOptionsPaytmBalance getPayChannelOptionPaytmBalance() {
        return payChannelOptionPaytmBalance;
    }

    public void setPayChannelOptionPaytmBalance(PayChannelOptionsPaytmBalance payChannelOptionPaytmBalance) {
        this.payChannelOptionPaytmBalance = payChannelOptionPaytmBalance;
    }

    public List<PayChannelOptionsPaytmBalance> getPayChannelOptionsPaytmBalance() {
        return checkoutBean.fetchPayChannelOptionsPaytmBalance();
    }

    public void setPayChannelOptionsPaytmBalance(List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance) {
        this.payChannelOptionsPaytmBalance = payChannelOptionsPaytmBalance;
    }

    public PayChannelOptionsNetBanking getPayChannelOptionNetBanking() {
        return payChannelOptionNetBanking;
    }

    public void setPayChannelOptionNetBanking(PayChannelOptionsNetBanking payChannelOptionNetBanking) {
        this.payChannelOptionNetBanking = payChannelOptionNetBanking;
    }

    public List<PayChannelOptionsNetBanking> getPayChannelOptionsNetBanking() {
        return checkoutBean.fetchPayChannelOptionsNetBanking();
    }

    public void setPayChannelOptionsNetBanking(List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking) {
        this.payChannelOptionsNetBanking = payChannelOptionsNetBanking;
    }

    public boolean isIsModePaytm() {
        return isModePaytm;
    }

    public void setIsModePaytm(boolean isModePaytm) {
        this.isModePaytm = isModePaytm;
    }

    public String getPaytmMobile() {
        return paytmMobile;
    }

    public void setPaytmMobile(String paytmMobile) {
        this.paytmMobile = paytmMobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    public boolean isIsModeCC() {
        return isModeCC;
    }

    public void setIsModeCC(boolean isModeCC) {
        this.isModeCC = isModeCC;
    }

    public String getCreditcardNumber() {
        return creditcardNumber;
    }

    public void setCreditcardNumber(String creditcardNumber) {
        this.creditcardNumber = creditcardNumber;
    }

    public boolean isIsCardValid() {
        return isCardValid;
    }

    public void setIsCardValid(boolean isCardValid) {
        this.isCardValid = isCardValid;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getIsCvvRequired() {
        return isCvvRequired;
    }

    public void setIsCvvRequired(String isCvvRequired) {
        this.isCvvRequired = isCvvRequired;
    }

    public String getIsExpRequired() {
        return isExpRequired;
    }

    public void setIsExpRequired(String isExpRequired) {
        this.isExpRequired = isExpRequired;
    }

    public String getCardIconUrl() {
        return cardIconUrl;
    }

    public void setCardIconUrl(String cardIconUrl) {
        this.cardIconUrl = cardIconUrl;
    }

    public String getIsCardActive() {
        return isCardActive;
    }

    public void setIsCardActive(String isCardActive) {
        this.isCardActive = isCardActive;
    }

    public boolean isIsModeDC() {
        return isModeDC;
    }

    public void setIsModeDC(boolean isModeDC) {
        this.isModeDC = isModeDC;
    }

    public boolean isIsModeNB() {
        return isModeNB;
    }

    public void setIsModeNB(boolean isModeNB) {
        this.isModeNB = isModeNB;
    }

    public CartController getCartController() {
        return cartController;
    }

    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }

    public void addDeliveryAddress(RegisteredUsersAddress selectedAddress) {
        checkoutService.addDeliveryAddress(selectedAddress);
    }

    public void addDeliveryTime(SelectEvent event) {
        String selectedTime = event.getObject().toString();
        checkoutService.addDeliveryTime(selectedTime);
    }

    public void initiateTransaction(String payableAmount) {
        checkoutBean.initiateTransaction(payableAmount);
    }

    public void sendOTP(String paytmMobile) {
        checkoutBean.sendOTP(paytmMobile);
    }

    public void validateOtpAndFetchPaytmBalance(String otp) {
        checkoutBean.validateOtpAndFetchPaytmBalance(otp);
    }

    public void processTransaction(String paymentMode) {
        checkoutBean.processTransaction(paymentMode);
    }

    public void onRowSelect(SelectEvent<PaymentOptions> event) {
        isModePaytm = false;
        isModeCC = false;
        isModeDC = false;
        isModeNB = false;
        String paymode = event.getObject().getPaymentMode();
        if (paymode.equals("BALANCE")) {
            isModePaytm = true;
        } else if (paymode.equals("CREDIT_CARD")) {
            isModeCC = true;
        } else if (paymode.equals("DEBIT_CARD")) {
            isModeDC = true;
        } else {
            isModeNB = true;
        }
    }

    public void fetchBinDetails(AjaxBehaviorEvent event) {
        String cardDigits = (String) ((UIOutput) event.getSource()).getValue();
        if (creditcardNumber.length() == 6) {
            isCardValid = checkoutBean.fetchBinDetails(cardDigits);
            if (isCardValid) {
                List<CardBinDetails> cardBinDetails = paymentGatewayBean.getCardBinDetails();
                Iterator<CardBinDetails> itr = cardBinDetails.iterator();
                while (itr.hasNext()) {
                    CardBinDetails cardDetails = itr.next();
                    issuingBank = cardDetails.getIssuingBank();
                    channelName = cardDetails.getChannelName();
                    isCvvRequired = cardDetails.getIsCvvRequired();
                    isExpRequired = cardDetails.getIsExpRequired();
                    cardIconUrl = cardDetails.getCardIconUrl();
                    isCardActive = cardDetails.getIsActive();
                }
            }
        } else {
            isCardValid = false;
        }
    }

}
