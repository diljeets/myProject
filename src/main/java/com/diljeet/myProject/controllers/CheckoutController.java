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
import com.diljeet.myProject.utils.PayChannelOptions;
import com.diljeet.myProject.utils.PaymentOptions;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    private PayChannelOptions payChannelOption;
    private List<PayChannelOptions> payChannelOptions;
    private boolean isModePaytm;
    private String paytmMobile;
    private String otp;
    private String currency;
    private String balance;
    private boolean isModeCC;
    private boolean isModeDC;
    private boolean isModeNB;
//    private UIComponent sendOtpBtn;

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
        return paymentGatewayBean.getPaymentOptions();
    }

    public void setPaymentOptions(List<PaymentOptions> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public PayChannelOptions getPayChannelOption() {
        return payChannelOption;
    }

    public void setPayChannelOption(PayChannelOptions payChannelOption) {
        this.payChannelOption = payChannelOption;
    }

    public List<PayChannelOptions> getPayChannelOptions() {
        return paymentGatewayBean.getPayChannelOptions();
    }

    public void setPayChannelOptions(List<PayChannelOptions> payChannelOptions) {
        this.payChannelOptions = payChannelOptions;
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

    public String getCurrency() {
        return paymentGatewayBean.getCurrency();
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return paymentGatewayBean.getBalance();
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public boolean isIsModeCC() {
        return isModeCC;
    }

    public void setIsModeCC(boolean isModeCC) {
        this.isModeCC = isModeCC;
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

//    public UIComponent getSendOtpBtn() {
//        return sendOtpBtn;
//    }
//
//    public void setSendOtpBtn(UIComponent sendOtpBtn) {
//        this.sendOtpBtn = sendOtpBtn;
//    }
    public CartController getCartController() {
        return cartController;
    }

    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }

    public void addDeliveryAddress(RegisteredUsersAddress selectedAddress) {
        checkoutService.addDeliveryAddress(selectedAddress);
    }

    public void addDeliveryTime(AjaxBehaviorEvent event) {
        String selectedTime = (String) ((UIOutput) event.getSource()).getValue();
        checkoutService.addDeliveryTime(selectedTime);
    }

    public void initiateTransaction(String payableAmount) {
        checkoutBean.initiateTransaction(payableAmount);
    }

    public void sendOTP(String paytmMobile) {
        checkoutBean.sendOTP(paytmMobile);
    }
    
    public void validateOtpAndFetchBalanceInfo(String otp) {
        checkoutBean.validateOtpAndFetchBalanceInfo(otp);
    }
    
    public void processTransaction(String paymentMode) {
        checkoutBean.processTransaction(paymentMode);
    }

    public void placeOrder(CustomerOrder customerOrder) {
        checkoutService.placeOrder(customerOrder);
    }

    public void placeOrder() {
        placeOrder(new CustomerOrder(getDeliveryTime(),
                getDeliveryAddress(),
                cartController.getCartItems(),
                cartController.getPayableAmount()));
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
//            logger.log(Level.SEVERE, "Pay mode is {0}", paymode);
        } else {
            isModeNB = true;
        }
    }

}
