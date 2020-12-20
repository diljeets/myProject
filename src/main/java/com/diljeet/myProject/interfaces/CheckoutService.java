/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
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

    @POST
    @Path("setDeliveryTime")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDeliveryTime(String selectedTime);

//    @GET
//    @Path("getDeliveryTime")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getDeliveryTime();
    @POST
    @Path("addDeliveryAddress")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDeliveryAddress(RegisteredUsersAddress selectedAddress);

    @GET
    @Path("getDeliveryAddress")
    @Produces({MediaType.TEXT_PLAIN})
    public String getDeliveryAddress();

//    @POST
//    @Path("{channelId}/initiate-transaction")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void initiateTransaction(String payableAmount, @PathParam("channelId") String channelId);
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

    @POST
    @Path("processTransaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processTransaction(String paymentMode);

    @POST 
    @Path("placeOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(CustomerOrder customerOrder);
}
