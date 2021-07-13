/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.ejb.PaymentGatewayBean;
import com.diljeet.myProject.interfaces.CheckoutService;
import com.diljeet.myProject.utils.FetchBinDetails;
import com.diljeet.myProject.utils.FetchPaymentOptions;
import com.diljeet.myProject.utils.FetchPaytmBalance;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.MyProjectUtils;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import com.diljeet.myProject.utils.SendOtp;
import com.diljeet.myProject.utils.UpdateTransaction;
import com.diljeet.myProject.utils.ValidateOtp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Stateless
public class CheckoutServiceBean implements CheckoutService {

    private static final Logger logger = Logger.getLogger(CheckoutServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @EJB
    PaymentGatewayBean paymentGatewayBean;

    public CheckoutServiceBean() {
    }

    @PostConstruct
    public void init() {
    }

    @Override
    public String createOrderId() {
        return MyProjectUtils.createOrderId();
    }

    @Override
    public Response initiateTransaction(InitiateTransaction initiateTransaction) {
        String orderId = initiateTransaction.getOrderId();
        logger.log(Level.SEVERE, "initiateTransaction orderId is {0}", orderId);
        String username = initiateTransaction.getUsername();
        logger.log(Level.SEVERE, "initiateTransaction username is {0}", username);
        String payableAmount = initiateTransaction.getPayableAmount();
        String channelId = initiateTransaction.getChannelId();
        String callbackUrl = initiateTransaction.getCallbackUrl();
        Response response = null;
        try {
            response = paymentGatewayBean.initiateTransaction(orderId, payableAmount, username, channelId, callbackUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

//    @Override
//    public List<PaymentOptions> fetchPaymentOptions() {
//        List<PaymentOptions> paymentOptions = null;
//        try {
//            paymentOptions = paymentGatewayBean.getPaymentOptions();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return paymentOptions;
//    }
    @Override
    public Response fetchPaymentOptions(FetchPaymentOptions fetchPaymentOptions) {
        Response response = null;
        String orderId = fetchPaymentOptions.getOrderId();
        String transactionToken = fetchPaymentOptions.getTransactionToken();
        try {
            response = paymentGatewayBean.fetchPaymentOptions(orderId, transactionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response updateTransaction(UpdateTransaction updateTransaction) {
        Response response = null;
        String orderId = updateTransaction.getOrderId();
        String payableAmount = updateTransaction.getPayableAmount();
        String username = updateTransaction.getUsername();
        String transactionToken = updateTransaction.getTransactionToken();
        try {
            response = paymentGatewayBean.updateTransaction(orderId, payableAmount, username, transactionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

//    @Override
//    public List<PayChannelOptionsPaytmBalance> fetchPayChannelOptionsPaytmBalance() {
//        List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance = null;
//        try {
//            payChannelOptionsPaytmBalance = paymentGatewayBean.getPayChannelOptionsPaytmBalance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return payChannelOptionsPaytmBalance;
//    }
//
//    @Override
//    public List<SavedInstruments> fetchSavedInstruments() {
//        List<SavedInstruments> savedInstruments = null;
//        try {
//            savedInstruments = paymentGatewayBean.getSavedInstruments();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return savedInstruments;
//    }
//
//    @Override
//    public List<PayChannelOptionsNetBanking> fetchPayChannelOptionsNetBanking() {
//        List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking = null;
//        try {
//            payChannelOptionsNetBanking = paymentGatewayBean.getPayChannelOptionsNetBanking();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return payChannelOptionsNetBanking;
//    }
    @Override
    public Response sendOTP(SendOtp sendOtp) {
        String orderId = sendOtp.getOrderId();
        String paytmMobile = sendOtp.getPaytmMobile();
        String transactionToken = sendOtp.getTransactionToken();
        Response response = null;
        try {
            response = paymentGatewayBean.sendOTP(orderId, paytmMobile, transactionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response validateOtp(ValidateOtp validateOtp) {
        String orderId = validateOtp.getOrderId();
        String otp = validateOtp.getOtp();
        String transactionToken = validateOtp.getTransactionToken();
        Response response = null;
        try {
            response = paymentGatewayBean.validateOtp(orderId, otp, transactionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response fetchPaytmBalance(FetchPaytmBalance fetchPaytmBalance) {
        Response response = null;
        String orderId = fetchPaytmBalance.getOrderId();
        String transactionToken = fetchPaytmBalance.getTransactionToken();
        try {
            response = paymentGatewayBean.fetchPaytmBalance(orderId, transactionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response fetchBinDetails(FetchBinDetails fetchBinDetails) {
        Response response = null;
        String orderId = fetchBinDetails.getOrderId();
        String firstSixCardDigits = fetchBinDetails.getFirstSixCardDigits();
        String transactionToken = fetchBinDetails.getTransactionToken();
        try {
            response = paymentGatewayBean.fetchBinDetails(orderId, firstSixCardDigits, transactionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

//    @Override
//    public List<CardDetails> fetchCardDetails() {
//        List<CardDetails> cardDetails = null;
//        try {
//            cardDetails = paymentGatewayBean.getCardDetails();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return cardDetails;
//    }
    @Override
    public List<PayChannelOptionsNetBanking> fetchOtherNetBankingPaymentChannels(String orderId, String transactionToken) {
        List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking = null;
        try {
            payChannelOptionsNetBanking = paymentGatewayBean.fetchOtherNetBankingPaymentChannels(orderId, transactionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payChannelOptionsNetBanking;
    }

    @Override
    public Response processTransaction(PaymentRequestDetails paymentRequestDetails) {
        Response response = null;
        try {
            response = paymentGatewayBean.processTransaction(paymentRequestDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response transactionStatus(String orderId) {
        Response response = null;
        try {
            response = paymentGatewayBean.transactionStatus(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
