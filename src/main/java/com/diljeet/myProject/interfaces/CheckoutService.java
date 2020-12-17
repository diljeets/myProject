/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptions;
import com.diljeet.myProject.utils.PaymentOptions;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
    @Path("initiate-transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response initiateTransaction(InitiateTransaction initiateTransaction);

    @GET
    @Path("fetchPaymentOptions")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PaymentOptions> fetchPaymentOptions();

    @GET
    @Path("fetchPayChannelOptions")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PayChannelOptions> fetchPayChannelOptions();

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
    @Path("process-transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processTransaction(String paymentMode);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void placeOrder(CustomerOrder customerOrder);
}
