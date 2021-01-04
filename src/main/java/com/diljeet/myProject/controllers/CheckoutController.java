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
import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
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
    private String paymentMode;
    private boolean isModePaytm;
    private String paytmMobile;
    private String otp;
    private boolean isModeCC;
    private String ccNumber;
    private String ccExpiryMonth;
    private String ccExpiryYear;
    private String ccCvv;
    private boolean isModeDC;
    private String dcNumber;
    private String dcExpiryMonth;
    private String dcExpiryYear;
    private String dcCvv;
    private boolean isCardValid;
    private String issuingBank;
    private String channelName;
    private String isCvvRequired;
    private String isExpRequired;
    private String cardIconUrl;
    private String isCardActive;
    private boolean isModeNB;
    private String channelCode;

//    @Inject
//    CartController cartController;
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
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

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcExpiryMonth() {
        return ccExpiryMonth;
    }

    public void setCcExpiryMonth(String ccExpiryMonth) {
        this.ccExpiryMonth = ccExpiryMonth;
    }

    public String getCcExpiryYear() {
        return ccExpiryYear;
    }

    public void setCcExpiryYear(String ccExpiryYear) {
        this.ccExpiryYear = ccExpiryYear;
    }

    public String getCcCvv() {
        return ccCvv;
    }

    public void setCcCvv(String ccCvv) {
        this.ccCvv = ccCvv;
    }

    public String getDcNumber() {
        return dcNumber;
    }

    public void setDcNumber(String dcNumber) {
        this.dcNumber = dcNumber;
    }

    public String getDcExpiryMonth() {
        return dcExpiryMonth;
    }

    public void setDcExpiryMonth(String dcExpiryMonth) {
        this.dcExpiryMonth = dcExpiryMonth;
    }

    public String getDcExpiryYear() {
        return dcExpiryYear;
    }

    public void setDcExpiryYear(String dcExpiryYear) {
        this.dcExpiryYear = dcExpiryYear;
    }

    public String getDcCvv() {
        return dcCvv;
    }

    public void setDcCvv(String dcCvv) {
        this.dcCvv = dcCvv;
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

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

//    public CartController getCartController() {
//        return cartController;
//    }
//
//    public void setCartController(CartController cartController) {
//        this.cartController = cartController;
//    }
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
        if (paymentMode.equals("BALANCE")) {
            checkoutBean.processTransaction(new PaymentRequestDetails(
                    paymentMode
            ));
        } else if (paymentMode.equals("CREDIT_CARD")) {
            String ccExpiryDate = ccExpiryMonth + ccExpiryYear;
            checkoutBean.processTransaction(new PaymentRequestDetails(
                    paymentMode,
                    ccNumber,
                    ccExpiryDate,
                    ccCvv
            ));
        } else if (paymentMode.equals("DEBIT_CARD")) {
            String dcExpiryDate = dcExpiryMonth + dcExpiryYear;
            checkoutBean.processTransaction(new PaymentRequestDetails(
                    paymentMode,
                    dcNumber,
                    dcExpiryDate,
                    dcCvv
            ));
        } else {
            checkoutBean.processTransaction(new PaymentRequestDetails(
                    paymentMode,
                    channelCode
            ));
        }
    }

//    public void processTransaction(String paymentMode) {
//        checkoutBean.processTransaction(new PaymentRequestDetails(
//                paymentMode
//        ));
//    }
//    public void processTransaction(String paymentMode, String cardNumber, String cardExpiryMonth, String cardExpiryYear, String cardCvv) {
//        String cardExpiryDate = cardExpiryMonth + cardExpiryYear;
//        checkoutBean.processTransaction(new PaymentRequestDetails(
//                paymentMode,
//                cardNumber,
//                cardExpiryDate,
//                cardCvv
//        ));
//    }
//
//    public void processTransaction(String paymentMode, String channelCode) {
//        checkoutBean.processTransaction(new PaymentRequestDetails(
//                paymentMode,
//                channelCode
//        ));
//    }

    public void onRowSelect(SelectEvent<PaymentOptions> event) {
        isModePaytm = false;
        isModeCC = false;
        isModeDC = false;
        isModeNB = false;
        paymentMode = event.getObject().getPaymentMode();
        if (paymentMode.equals("BALANCE")) {
            isModePaytm = true;
        } else if (paymentMode.equals("CREDIT_CARD")) {
            isModeCC = true;
        } else if (paymentMode.equals("DEBIT_CARD")) {
            isModeDC = true;
        } else {
            isModeNB = true;
        }
    }

    public void onRowSelectPayChannelOptionNetBanking(SelectEvent<PayChannelOptionsNetBanking> event) {
        String selectedChannelCode = event.getObject().getChannelCode();
        if (selectedChannelCode.equals("OTHERS")) {
            paymentGatewayBean.fetchNetBankingPaymentChannels();
        } else {
            channelCode = selectedChannelCode;
        }
    }

    public void fetchBinDetails(AjaxBehaviorEvent event) {
        String cardDigits = (String) ((UIOutput) event.getSource()).getValue();
        if (cardDigits.length() == 6) {
            isCardValid = checkoutBean.fetchBinDetails(cardDigits);
            if (isCardValid) {
                List<CardDetails> cardDetails = checkoutBean.fetchCardDetails();
                Iterator<CardDetails> itr = cardDetails.iterator();
                while (itr.hasNext()) {
                    CardDetails cardDetail = itr.next();
                    issuingBank = cardDetail.getIssuingBank();
                    channelName = cardDetail.getChannelName();
                    isCvvRequired = cardDetail.getIsCvvRequired();
                    isExpRequired = cardDetail.getIsExpRequired();
                    cardIconUrl = cardDetail.getCardIconUrl();
                    isCardActive = cardDetail.getIsActive();
                }
            }
        } else {
            isCardValid = false;
        }
    }

//    public void fetchNetBankingPaymentChannels() {
//        paymentGatewayBean.fetchNetBankingPaymentChannels();
//    }
}
