/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.CartController;
import com.diljeet.myProject.controllers.CheckoutController;
import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.CustomerTransaction;
import com.diljeet.myProject.interfaces.CartService;
import com.diljeet.myProject.interfaces.CheckoutService;
import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class CheckoutBean {

    private static final Logger logger = Logger.getLogger(CheckoutBean.class.getCanonicalName());

    private Client client;

    private FacesMessage msg;

    @Inject
    HttpServletRequest req;

    @Context
    HttpServletResponse res;

    @Inject
    CheckoutController checkoutController;

    @Inject
    CartController cartController;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public void initiateTransaction(String payableAmount) {
        if (payableAmount == null) {
            return;
        }
        InitiateTransaction initiateTransaction = new InitiateTransaction(payableAmount,
                "WEB",
                "http://localhost:8080/myProject/webapi/Checkout/pgResponse");
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/initiateTransaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(initiateTransaction, MediaType.APPLICATION_JSON), Response.class);

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.log(Level.SEVERE, "Call PaymentOptions API");
            } else {
                logger.log(Level.SEVERE, "There is an error");
                logger.log(Level.SEVERE, "response code is {0}", Integer.toString(response.getStatus()));
                String resultMsg = response.getHeaderString("resultMsg");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "1Something went wrong. Please try again.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public List<PaymentOptions> fetchPaymentOptions() {
        List<PaymentOptions> paymentOptions = null;
        try {
            paymentOptions = client.target("http://localhost:8080/myProject/webapi/Checkout/fetchPaymentOptions")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get(new GenericType<List<PaymentOptions>>() {
                    });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Payment Options. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return paymentOptions;
    }

    public List<PayChannelOptionsPaytmBalance> fetchPayChannelOptionsPaytmBalance() {
        List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance = null;
        try {
            payChannelOptionsPaytmBalance = client.target("http://localhost:8080/myProject/webapi/Checkout/fetchPayChannelOptionsPaytmBalance")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get(new GenericType<List<PayChannelOptionsPaytmBalance>>() {
                    });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Balance Options. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return payChannelOptionsPaytmBalance;
    }

    public List<PayChannelOptionsNetBanking> fetchPayChannelOptionsNetBanking() {
        List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking = null;
        try {
            payChannelOptionsNetBanking = client.target("http://localhost:8080/myProject/webapi/Checkout/fetchPayChannelOptionsNetBanking")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get(new GenericType<List<PayChannelOptionsNetBanking>>() {
                    });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Banking Options. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return payChannelOptionsNetBanking;
    }

    public void sendOTP(String paytmMobile) {
        if (paytmMobile == null) {
            return;
        }
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/sendOTP")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(paytmMobile, MediaType.APPLICATION_JSON), Response.class);
            String resultMsg = response.getHeaderString("resultMsg");
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void validateOtpAndFetchPaytmBalance(String otp) {
        if (otp == null) {
            return;
        }
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/validateOTP/fetchBalance")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(otp, MediaType.APPLICATION_JSON), Response.class);
            String resultMsg = response.getHeaderString("resultMsg");
            if (response.getStatus() == Response.Status.NOT_ACCEPTABLE.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public boolean fetchBinDetails(String firstSixCardDigits) {
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/card/fetchBinDetails")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(firstSixCardDigits, MediaType.APPLICATION_JSON), Response.class);
            String resultMsg = response.getHeaderString("resultMsg");
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return false;
            }
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return true;
    }

    public List<CardDetails> fetchCardDetails() {
        List<CardDetails> cardDetails = null;
        try {
            cardDetails = client.target("http://localhost:8080/myProject/webapi/Checkout/fetchCardDetails")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get(new GenericType<List<CardDetails>>() {
                    });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Card Details. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return cardDetails;
    }

    public void processTransaction(String paymentMode) {

        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Checkout/processTransaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(paymentMode, MediaType.APPLICATION_JSON), Response.class);
            if (paymentMode.equals("BALANCE")) {
                if (response.getStatus() == Response.Status.OK.getStatusCode()
                        && response.hasEntity()) {
                    String stringEntity = response.readEntity(String.class);
                    JSONObject jsonObject = new JSONObject(stringEntity);
                    String callBackUrl = jsonObject.getString("callBackUrl");
                    JSONObject txnInfoObj = jsonObject.getJSONObject("txnInfo");

                    String BANKNAME = txnInfoObj.getString("BANKNAME");
                    String BANKTXNID = txnInfoObj.getString("BANKTXNID");
                    String CURRENCY = txnInfoObj.getString("CURRENCY");
                    String GATEWAYNAME = txnInfoObj.getString("GATEWAYNAME");
//                String MID = txnInfoObj.getString("MID");
                    String ORDERID = txnInfoObj.getString("ORDERID");
                    String PAYMENTMODE = txnInfoObj.getString("PAYMENTMODE");
                    String RESPCODE = txnInfoObj.getString("RESPCODE");
                    String RESPMSG = txnInfoObj.getString("RESPMSG");
//                String STATUS = txnInfoObj.getString("STATUS");
                    String TXNAMOUNT = txnInfoObj.getString("TXNAMOUNT");
                    String TXNDATE = txnInfoObj.getString("TXNDATE");
                    String TXNID = txnInfoObj.getString("TXNID");

                    if (RESPCODE.equals("01")) {
                        Response transactionStatusResponse = transactionStatus(ORDERID);
                        if (transactionStatusResponse.getStatus() == Response.Status.OK.getStatusCode()
                                && transactionStatusResponse.hasEntity()) {
                            String stringEntityInTransactionStatusResponse = transactionStatusResponse.readEntity(String.class);
                            JSONObject bodyObj = new JSONObject(stringEntityInTransactionStatusResponse);
                            JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                            String resultCode = resultInfoObj.getString("resultCode");
                            if (resultCode.equals("01")) {
                                CustomerTransaction customerTransaction = new CustomerTransaction(
                                        BANKNAME,
                                        BANKTXNID,
                                        CURRENCY,
                                        GATEWAYNAME,
                                        ORDERID,
                                        PAYMENTMODE,
                                        RESPCODE,
                                        RESPMSG,
                                        TXNAMOUNT,
                                        TXNDATE,
                                        TXNID
                                );
                                //Place Customer Order and Transaction details in Database
                                placeOrder(new CustomerOrder(checkoutController.getDeliveryTime(),
                                        checkoutController.getDeliveryAddress(),
                                        cartController.getCartItems(),
                                        cartController.getPayableAmount(),
                                        customerTransaction));
                            }
                        }
                    }

                    //Redirect to intimate Customer whether or not Order/Transaction is successful
                    FacesContext.getCurrentInstance().getExternalContext().redirect(callBackUrl);

                } else {
                    String resultMsg = response.getHeaderString("resultMsg");
                    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }

            if (paymentMode.equals("CREDIT_CARD")) {
                if (response.getStatus() == Response.Status.TEMPORARY_REDIRECT.getStatusCode()) {
                    String redirectUrl = response.getLocation().toString();
                    FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);
                } else {
                    String resultMsg = response.getHeaderString("resultMsg");
                    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public Response transactionStatus(String orderId) {
        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Checkout/transactionStatus")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(orderId, MediaType.APPLICATION_JSON), Response.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void placeOrder(CustomerOrder customerOrder) {
//        checkoutService.placeOrder(customerOrder);
        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Checkout/placeOrder")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(customerOrder, MediaType.APPLICATION_JSON), Response.class);
//                    .buildPost(Entity.entity(customerOrder, MediaType.APPLICATION_JSON))                  
//                    .submit(Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.log(Level.SEVERE, "Order Placed Successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
