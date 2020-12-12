/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.utils.InitiateTransaction;
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
    @Consumes(MediaType.TEXT_PLAIN)
    public void addDeliveryTime(String selectedTime);

    @GET
    @Path("delivery-time")
    @Produces({MediaType.TEXT_PLAIN})
    public String getDeliveryTime();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDeliveryAddress(RegisteredUsersAddress selectedAddress);

    @GET
    @Path("delivery-address")
    @Produces({MediaType.TEXT_PLAIN})
    public String getDeliveryAddress();

//    @POST
//    @Path("{channelId}/initiate-transaction")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void initiateTransaction(String payableAmount, @PathParam("channelId") String channelId);
    @POST
    @Path("initiate-transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public void initiateTransaction(InitiateTransaction initiateTransaction);

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

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void placeOrder(CustomerOrder customerOrder);
}
