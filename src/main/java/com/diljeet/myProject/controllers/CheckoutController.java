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
import com.diljeet.myProject.utils.SavedInstruments;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.List;
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
    private String orderId;
    private PaymentOptions paymentOption;
    private List<PaymentOptions> paymentOptions;
    private PayChannelOptionsPaytmBalance payChannelOptionPaytmBalance;
    private List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance;
    private SavedInstruments savedInstrument;
    private List<SavedInstruments> savedInstruments;
    private PayChannelOptionsNetBanking payChannelOptionNetBanking;
    private List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking;
    private PaymentRequestDetails paymentRequestDetails;
    private String paymentMode;
    private boolean isModePaytm;
    private String paytmMobile;
    private String otp;
    private boolean isValidPaytmMobile;
    private boolean isPaymentThroughSavedCard;
    private String cardId;
    private String cardCvv;
    private boolean isModeCC;
    private String maskedCCNumber;
    private String maskedCCExpiryDate;
    private String ccCvv;
    private boolean saveCC;
    private boolean isModeDC;
    private String maskedDCNumber;
    private String maskedDCExpiryDate;
    private String dcCvv;
    private boolean saveDC;
    private boolean isCardValid;
    private String issuingBank;
    private String channelName;
    private String isCvvRequired;
    private String isExpRequired;
    private String cardIconUrl;
    private String isCardActive;
    private boolean isModeNB;
    private String channelCode;
    private boolean isModePOD;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public SavedInstruments getSavedInstrument() {
        return savedInstrument;
    }

    public void setSavedInstrument(SavedInstruments savedInstrument) {
        this.savedInstrument = savedInstrument;
    }

    public List<SavedInstruments> getSavedInstruments() {
        return checkoutBean.fetchSavedInstruments();
    }

    public void setSavedInstruments(List<SavedInstruments> savedInstruments) {
        this.savedInstruments = savedInstruments;
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

    public PaymentRequestDetails getPaymentRequestDetails() {
        return paymentRequestDetails;
    }

    public void setPaymentRequestDetails(PaymentRequestDetails paymentRequestDetails) {
        this.paymentRequestDetails = paymentRequestDetails;
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

    public boolean isIsPaymentThroughSavedCard() {
        return isPaymentThroughSavedCard;
    }

    public void setIsPaymentThroughSavedCard(boolean isPaymentThroughSavedCard) {
        this.isPaymentThroughSavedCard = isPaymentThroughSavedCard;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public boolean isIsModeCC() {
        return isModeCC;
    }

    public void setIsModeCC(boolean isModeCC) {
        this.isModeCC = isModeCC;
    }

    public String getMaskedCCNumber() {
        return maskedCCNumber;
    }

    public void setMaskedCCNumber(String maskedCCNumber) {
        this.maskedCCNumber = maskedCCNumber;
    }

    public String getMaskedCCExpiryDate() {
        return maskedCCExpiryDate;
    }

    public void setMaskedCCExpiryDate(String maskedCCExpiryDate) {
        this.maskedCCExpiryDate = maskedCCExpiryDate;
    }

    public String getCcCvv() {
        return ccCvv;
    }

    public void setCcCvv(String ccCvv) {
        this.ccCvv = ccCvv;
    }

    public boolean isSaveCC() {
        return saveCC;
    }

    public void setSaveCC(boolean saveCC) {
        this.saveCC = saveCC;
    }

    public String getMaskedDCNumber() {
        return maskedDCNumber;
    }

    public void setMaskedDCNumber(String maskedDCNumber) {
        this.maskedDCNumber = maskedDCNumber;
    }

    public String getMaskedDCExpiryDate() {
        return maskedDCExpiryDate;
    }

    public void setMaskedDCExpiryDate(String maskedDCExpiryDate) {
        this.maskedDCExpiryDate = maskedDCExpiryDate;
    }

    public String getDcCvv() {
        return dcCvv;
    }

    public void setDcCvv(String dcCvv) {
        this.dcCvv = dcCvv;
    }

    public boolean isSaveDC() {
        return saveDC;
    }

    public void setSaveDC(boolean saveDC) {
        this.saveDC = saveDC;
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

    public boolean isIsModePOD() {
        return isModePOD;
    }

    public void setIsModePOD(boolean isModePOD) {
        this.isModePOD = isModePOD;
    }

    public DeliveryController getDeliveryController() {
        return deliveryController;
    }

    public void setDeliveryController(DeliveryController deliveryController) {
        this.deliveryController = deliveryController;
    }

    public void initiateTransaction(String payableAmount) {
        if (orderId == null) {
            orderId = checkoutBean.createOrderId();
        }
        checkoutBean.initiateTransaction(payableAmount, orderId);
    }

    public void sendOTP(String paytmMobile) {
        checkoutBean.sendOTP(paytmMobile);
    }

    public void validateOtpAndFetchPaytmBalance(String maskedOtp) {
        //Remove masking from maskedOtp and retrieve actual otp        
        String[] otpArray = maskedOtp.split("  ");
        StringBuilder otpBuilder = new StringBuilder();
        for (String otpChar : otpArray) {
            otpBuilder.append(otpChar);
        }
        String otpString = otpBuilder.toString();
        checkoutBean.validateOtpAndFetchPaytmBalance(otpString);
    }

    public void processTransaction(String paymentMode) {
        if (paymentMode.equals("BALANCE") || paymentMode.equals("POD")) {
            paymentRequestDetails = new PaymentRequestDetails(
                    paymentMode
            );
            try {
                //Redirect if paymode is BALANCE/POD
                FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/myProject/redirect-form.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (paymentMode.equals("CREDIT_CARD")) {
            if (isPaymentThroughSavedCard) {
                paymentRequestDetails = new PaymentRequestDetails(
                        paymentMode,
                        cardId,
                        cardCvv
                );
                checkoutBean.processTransaction(paymentRequestDetails);
            } else {
                String ccNumber = removeMaskingFromCardNumber(maskedCCNumber);
                String ccExpiryDate = removeMaskingFromCardExpiryDate(maskedCCExpiryDate);
                String saveCard = saveCC ? "1" : "0";
                paymentRequestDetails = new PaymentRequestDetails(
                        paymentMode,
                        ccNumber,
                        ccExpiryDate,
                        ccCvv,
                        saveCard
                );
                checkoutBean.processTransaction(paymentRequestDetails);
            }
        } else if (paymentMode.equals("DEBIT_CARD")) {
            if (isPaymentThroughSavedCard) {
                paymentRequestDetails = new PaymentRequestDetails(
                        paymentMode,
                        cardId,
                        cardCvv
                );
                checkoutBean.processTransaction(paymentRequestDetails);
            } else {
                String dcNumber = removeMaskingFromCardNumber(maskedDCNumber);
                String dcExpiryDate = removeMaskingFromCardExpiryDate(maskedDCExpiryDate);
                String saveCard = saveDC ? "1" : "0";
                paymentRequestDetails = new PaymentRequestDetails(
                        paymentMode,
                        dcNumber,
                        dcExpiryDate,
                        dcCvv,
                        saveCard
                );
                checkoutBean.processTransaction(paymentRequestDetails);
            }
        } else {
            paymentRequestDetails = new PaymentRequestDetails(
                    paymentMode,
                    channelCode
            );
            checkoutBean.processTransaction(paymentRequestDetails);
        }
    }

    public void onRowSelectPaymentOption(SelectEvent<PaymentOptions> event) {
        isPaymentThroughSavedCard = false;
        isModePaytm = false;
        isModeCC = false;
        isModeDC = false;
        isModeNB = false;
        isModePOD = false;
        paymentMode = event.getObject().getPaymentMode();
        if (paymentMode.equals("BALANCE")) {
            isModePaytm = true;
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('paytmSidebar').show()");
        } else if (paymentMode.equals("CREDIT_CARD")) {
            isModeCC = true;
        } else if (paymentMode.equals("DEBIT_CARD")) {
            isModeDC = true;
        } else if (paymentMode.equals("NET_BANKING")) {
            isModeNB = true;
        } else {
            isModePOD = true;
        }
    }

    public void onRowSelectPayChannelOptionNetBanking(SelectEvent<PayChannelOptionsNetBanking> event) {
        isPaymentThroughSavedCard = false;
        isModePaytm = false;
        isModeCC = false;
        isModeDC = false;
        isModeNB = true;
        isModePOD = false;
        String selectedChannelCode = event.getObject().getChannelCode();
        if (selectedChannelCode.equals("OTHERS")) {
            checkoutBean.fetchOtherNetBankingPaymentChannels();
        } else {
            channelCode = selectedChannelCode;
        }
    }

    public void onRowSelectPayChannelOptionsPaytmBalance(SelectEvent<PayChannelOptionsPaytmBalance> event) {
        isPaymentThroughSavedCard = false;
        isModePaytm = true;
        isModeCC = false;
        isModeDC = false;
        isModeNB = false;
        isModePOD = false;
        paymentMode = event.getObject().getPaymentMode();
    }

    public void onRowSelectSavedInstruments(SelectEvent<SavedInstruments> event) {
        isPaymentThroughSavedCard = true;
        isModePaytm = false;
        isModeCC = false;
        isModeDC = false;
        isModeNB = false;
        isModePOD = false;
        paymentMode = event.getObject().getCardType();
        cardId = event.getObject().getCardId();
        if (paymentMode.equals("CREDIT_CARD")) {
            isModeCC = true;
        } else {
            isModeDC = true;
        }
    }

    public void fetchBinDetails(AjaxBehaviorEvent event) {
        String maskedCardNumber = (String) ((UIOutput) event.getSource()).getValue();
        String cardDigits = removeMaskingFromCardNumber(maskedCardNumber);
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

    public String removeMaskingFromCardNumber(String maskedNumber) {
        String[] cardNumberArray = maskedNumber.split("-");
        StringBuilder cardNumberBuilder = new StringBuilder();
        for (String cardNumberStr : cardNumberArray) {
            cardNumberBuilder.append(cardNumberStr);
        }
        return cardNumberBuilder.toString();
    }

    public String removeMaskingFromCardExpiryDate(String maskedDate) {
        String[] cardExpiryDateArray = maskedDate.split("/");
        StringBuilder cardExpiryDateBuilder = new StringBuilder();
        for (String cardExpiryDateStr : cardExpiryDateArray) {
            cardExpiryDateBuilder.append(cardExpiryDateStr);
        }
        return cardExpiryDateBuilder.toString();
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
    }

//    public void validateEnterOtp(FacesContext context, UIComponent comp, String maskedOtp) {
//        if (maskedOtp.length() < 6) {
//            ((UIInput) comp).setValid(false);
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter a valid 6-digit OTP");
//            context.addMessage(comp.getClientId(context), message);            
//        }
//    }
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
