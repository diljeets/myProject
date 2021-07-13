/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.utils.FetchBinDetails;
import com.diljeet.myProject.utils.FetchPaymentOptions;
import com.diljeet.myProject.utils.FetchPaytmBalance;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import com.diljeet.myProject.utils.SendOtp;
import com.diljeet.myProject.utils.UpdateTransaction;
import com.diljeet.myProject.utils.ValidateOtp;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

//    @GET
//    @Path("fetchPaymentOptions")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<PaymentOptions> fetchPaymentOptions();
    @POST
    @Path("paymentOptions/fetch")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchPaymentOptions(FetchPaymentOptions fetchPaymentOptions);

    @POST
    @Path("transaction/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTransaction(UpdateTransaction updateTransaction);

//    @GET
//    @Path("fetchPayChannelOptionsPaytmBalance")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<PayChannelOptionsPaytmBalance> fetchPayChannelOptionsPaytmBalance();
//
//    @GET
//    @Path("fetchSavedInstruments")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<SavedInstruments> fetchSavedInstruments();
//
//    @GET
//    @Path("fetchPayChannelOptionsNetBanking")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<PayChannelOptionsNetBanking> fetchPayChannelOptionsNetBanking();
    @POST
    @Path("otp/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendOTP(SendOtp sendOtp);

    @POST
    @Path("otp/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateOtp(ValidateOtp validateOtp);

    @POST
    @Path("paymentOptions/balance/fetch")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchPaytmBalance(FetchPaytmBalance fetchPaytmBalance);

    @POST
    @Path("card/fetchBinDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchBinDetails(FetchBinDetails fetchBinDetails);

//    @GET
//    @Path("fetchCardDetails")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<CardDetails> fetchCardDetails();
    @GET
    @Path("netBankingPaymentChannels/other/fetch")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PayChannelOptionsNetBanking> fetchOtherNetBankingPaymentChannels(@HeaderParam("orderId") String orderId,
            @HeaderParam("transactionToken") String transactionToken);

    @POST
    @Path("processTransaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processTransaction(PaymentRequestDetails paymentRequestDetails);

    @POST
    @Path("transactionStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transactionStatus(String orderId);

}
