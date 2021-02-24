/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import com.diljeet.myProject.utils.SavedInstruments;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
//@Remote
@Path("/Checkout")
public interface CheckoutService {
    
    @GET
    @Path("createOrderId")
    @Produces({MediaType.APPLICATION_JSON})
    public String createOrderId();

    @POST
    @Path("initiateTransaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response initiateTransaction(InitiateTransaction initiateTransaction);

    @GET
    @Path("fetchPaymentOptions")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PaymentOptions> fetchPaymentOptions();

    @GET
    @Path("fetchPayChannelOptionsPaytmBalance")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PayChannelOptionsPaytmBalance> fetchPayChannelOptionsPaytmBalance();
    
    @GET
    @Path("fetchSavedInstruments")
    @Produces({MediaType.APPLICATION_JSON})
    public List<SavedInstruments> fetchSavedInstruments();

    @GET
    @Path("fetchPayChannelOptionsNetBanking")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PayChannelOptionsNetBanking> fetchPayChannelOptionsNetBanking();

    @POST
    @Path("sendOTP")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendOTP(String paytmMobile);

    @POST
    @Path("validateOTP/fetchBalance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateOtpAndFetchPaytmBalance(String otp);

    @POST
    @Path("card/fetchBinDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchBinDetails(String firstSixCardDigits);

    @GET
    @Path("fetchCardDetails")
    @Produces({MediaType.APPLICATION_JSON})
    public List<CardDetails> fetchCardDetails();
    
    @GET
    @Path("fetchOtherNetBankingPaymentChannels")    
    public void fetchOtherNetBankingPaymentChannels();
    
    @POST
    @Path("processTransaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processTransaction(PaymentRequestDetails paymentRequestDetails); 

    @POST
    @Path("transactionStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transactionStatus(String orderId);

}
