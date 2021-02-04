/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.CheckoutBean;
import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.interfaces.CheckoutService;
import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
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
//    private String deliveryTime;
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
    private boolean isValidPaytmMobile;
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

    @Inject
    DeliveryController deliveryController;

    @EJB
    CheckoutBean checkoutBean;

    @EJB
    CheckoutService checkoutService;

    @PostConstruct
    public void init() {
//        setDeliveryTime("12:00");
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

//    public String getDeliveryTime() {
//        return deliveryTime;
//    }
//
//    public void setDeliveryTime(String deliveryTime) {
//        this.deliveryTime = deliveryTime;
//    }
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

    public boolean isIsValidPaytmMobile() {
        return isValidPaytmMobile;
    }

    public void setIsValidPaytmMobile(boolean isValidPaytmMobile) {
        this.isValidPaytmMobile = isValidPaytmMobile;
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

    public DeliveryController getDeliveryController() {
        return deliveryController;
    }

    public void setDeliveryController(DeliveryController deliveryController) {
        this.deliveryController = deliveryController;
    }

//    public void addDeliveryTime(SelectEvent event) {
//        String selectedTime = event.getObject().toString();
//        checkoutService.addDeliveryTime(selectedTime);
//    }
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
        logger.log(Level.SEVERE, "checkout controller Payment mode is {0}", paymentMode);
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

    public void onRowSelectPaymentOption(SelectEvent<PaymentOptions> event) {
        isModePaytm = false;
        isModeCC = false;
        isModeDC = false;
        isModeNB = false;
        paymentMode = event.getObject().getPaymentMode();
        if (paymentMode.equals("BALANCE")) {
            isModePaytm = true;
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('paytmSidebar').show()");
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
            checkoutBean.fetchOtherNetBankingPaymentChannels();
        } else {
            channelCode = selectedChannelCode;
        }
    }

    public void fetchBinDetails(AjaxBehaviorEvent event) {
        String cardDigits = (String) ((UIOutput) event.getSource()).getValue();
        logger.log(Level.SEVERE, "CC number is {0}", cardDigits);
        String firstSixCardDigits = cardDigits.substring(0, 6);
        if (firstSixCardDigits.length() == 6) {
            isCardValid = checkoutBean.fetchBinDetails(firstSixCardDigits);
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

//    public void fetchBinDetails(AjaxBehaviorEvent event) {
//        String cardDigits = (String) ((UIOutput) event.getSource()).getValue();
//        if (cardDigits.length() == 6) {
//            isCardValid = checkoutBean.fetchBinDetails(cardDigits);
//            if (isCardValid) {
//                List<CardDetails> cardDetails = checkoutBean.fetchCardDetails();
//                Iterator<CardDetails> itr = cardDetails.iterator();
//                while (itr.hasNext()) {
//                    CardDetails cardDetail = itr.next();
//                    issuingBank = cardDetail.getIssuingBank();
//                    channelName = cardDetail.getChannelName();
//                    isCvvRequired = cardDetail.getIsCvvRequired();
//                    isExpRequired = cardDetail.getIsExpRequired();
//                    cardIconUrl = cardDetail.getCardIconUrl();
//                    isCardActive = cardDetail.getIsActive();
//                }
//            }
//        } else {
//            isCardValid = false;
//        }
//    }
    public void validatePaytmMobileNumber(FacesContext context, UIComponent comp, String mobileNumber) {
        isValidPaytmMobile = true;        
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(mobileNumber);
        boolean isMatched = matcher.matches();
        if (isMatched) {
            if (mobileNumber.length() < 10) {
                ((UIInput) comp).setValid(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid 10-digit Mobile Number");
                context.addMessage(comp.getClientId(context), message);
                isValidPaytmMobile = false;
            } 
        } else {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid Mobile Number");
            context.addMessage(comp.getClientId(context), message); 
            isValidPaytmMobile = false;
        }        
        logger.log(Level.SEVERE, "isValidationFailed is {0}", Boolean.toString(isValidPaytmMobile));
    }

    public void validateCardNumber(FacesContext context, UIComponent comp, String cardNumber) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(cardNumber);
        boolean isMatched = matcher.matches();
        if (isMatched) {
            if (cardNumber.length() < 16) {
                ((UIInput) comp).setValid(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid 16-digit Card Number");
                context.addMessage(comp.getClientId(context), message);
            }
        } else {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid Card Number");
            context.addMessage(comp.getClientId(context), message);
        }
    }

    public void validateExpiryMonth(FacesContext context, UIComponent comp, String expiryMonth) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(expiryMonth);
        boolean isMatched = matcher.matches();
        if (isMatched) {
            if (expiryMonth.length() < 2) {
                ((UIInput) comp).setValid(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid Month between 01 and 12 e.g 01");
                context.addMessage(comp.getClientId(context), message);
            } else {
                if (!((Integer.parseInt(expiryMonth) > 0) && (Integer.parseInt(expiryMonth) <= 12))) {
                    ((UIInput) comp).setValid(false);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid Month between 01 and 12");
                    context.addMessage(comp.getClientId(context), message);
                }
            }
        } else {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid Month e.g 01");
            context.addMessage(comp.getClientId(context), message);
        }
    }

    public void validateExpiryYear(FacesContext context, UIComponent comp, String expiryYear) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(expiryYear);
        boolean isMatched = matcher.matches();
        if (isMatched) {
            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear();
            if (Integer.parseInt(expiryYear) < currentYear) {
                ((UIInput) comp).setValid(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Expiry Year cannot have a past value");
                context.addMessage(comp.getClientId(context), message);
            }
        } else {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid Year e.g 2050");
            context.addMessage(comp.getClientId(context), message);
        }
    }

    public void validateCvv(FacesContext context, UIComponent comp, String cvv) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(cvv);
        boolean isMatched = matcher.matches();
        if (isMatched) {
            if (cvv.length() < 3) {
                ((UIInput) comp).setValid(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid 3-digit CVV number e.g 123");
                context.addMessage(comp.getClientId(context), message);
            }
        } else {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid CVV number e.g 123");
            context.addMessage(comp.getClientId(context), message);
        }
    }

}
