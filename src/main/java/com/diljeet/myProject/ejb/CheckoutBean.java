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
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.interfaces.CartService;
import com.diljeet.myProject.interfaces.CheckoutService;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptions;
import com.diljeet.myProject.utils.PaymentOptions;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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

    @Inject
    CheckoutController checkoutController;

    @Inject
    CartController cartController;

    @EJB
    CartService cartService;

    @EJB
    CheckoutService checkoutService;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

//    public void addDeliveryTime(String selectedTime) {
//        if (selectedTime == null) {
//            return;
//        }       
//        try {
//            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/setDeliveryTime")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .post(Entity.entity(selectedTime, MediaType.APPLICATION_JSON), Response.class);
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
////            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
////            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
//        }
//    }
//    
//    public String getDeliveryTime() {  
//        Response response = null;
//        String deliveryTime = null;
//        try {
//            response = client.target("http://localhost:8080/myProject/webapi/Checkout/getDeliveryTime")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get();
//            if(response.getStatus() == Response.Status.OK.getStatusCode()){
//                deliveryTime = response.readEntity(String.class);
//                logger.log(Level.SEVERE, "getDeliveryTime {0}",deliveryTime);
//            }
//            
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
////            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
////            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
//        }
//        return deliveryTime;
//    }
//    
//    public void addDeliveryAddress(RegisteredUsersAddress selectedAddress) {
//        if (selectedAddress == null) {
//            return;
//        }       
//        try {
//            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/setDeliveryAddress")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .post(Entity.entity(selectedAddress, MediaType.APPLICATION_JSON), Response.class);
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
////            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
////            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
//        }
//    }
//
//    public String getDeliveryAddress() {  
////        Response response = null;
//        String deliveryAddress = null;
//        try {
//            deliveryAddress = client.target("http://localhost:8080/myProject/webapi/Checkout/getDeliveryAddress")
//                    .request(MediaType.TEXT_PLAIN)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get(String.class);
////            if(response.getStatus() == Response.Status.OK.getStatusCode()){
////                deliveryTime = response.readEntity(String.class);
////                logger.log(Level.SEVERE, "getDeliveryTime {0}",deliveryTime);
////            }
////            
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
////            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
////            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
//        }
//        return deliveryAddress;
//    }
    public void initiateTransaction(String payableAmount) {
        if (payableAmount == null) {
            return;
        }
        InitiateTransaction initiateTransaction = new InitiateTransaction(payableAmount,
                "WEB",
                "http://localhost:8080/myProject/process-transaction-status.xhtml");
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/initiate-transaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(initiateTransaction, MediaType.APPLICATION_JSON), Response.class);
//                    .post(Entity.entity(payableAmount, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.log(Level.SEVERE, "Call PaymentOptions API");
//                fetchPaymentOptions();
            } else {
                logger.log(Level.SEVERE, "There is an error");
                String resultMsg = response.getHeaderString("resultMsg");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong. Please try again.");
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

    public List<PayChannelOptions> fetchPayChannelOptions() {
        List<PayChannelOptions> payChannelOptions = null;
        try {
            payChannelOptions = client.target("http://localhost:8080/myProject/webapi/Checkout/fetchPayChannelOptions")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get(new GenericType<List<PayChannelOptions>>() {
                    });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Banking Options. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return payChannelOptions;
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
//                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, resultMsg, null);
//                FacesContext.getCurrentInstance().addMessage(checkoutController.getSendOtpBtn().getClientId(), msg);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
//                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, resultMsg, null);
//                FacesContext.getCurrentInstance().addMessage(checkoutController.getSendOtpBtn().getClientId(), msg);
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        } catch (Exception e) {
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(checkoutController.getSendOtpBtn().getClientId(), msg);
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

    public void processTransaction(String paymentMode) {
        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Checkout/process-transaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(paymentMode, MediaType.APPLICATION_JSON), Response.class);

            if (response.getStatus() == Response.Status.OK.getStatusCode()
                    && response.hasEntity()) {
                String stringEntity = response.readEntity(String.class);
                JSONObject jsonObject = new JSONObject(stringEntity);
                JSONObject txnInfoObj = jsonObject.getJSONObject("txnInfo");

                String BANKNAME = txnInfoObj.getString("BANKNAME");
                String BANKTXNID = txnInfoObj.getString("BANKTXNID");
                String _CURRENCY = txnInfoObj.getString("CURRENCY");
                String GATEWAYNAME = txnInfoObj.getString("GATEWAYNAME");
//                    String _MID = txnInfoObj.getString("MID");
                String ORDERID = txnInfoObj.getString("ORDERID");
                String PAYMENTMODE = txnInfoObj.getString("PAYMENTMODE");
                String RESPCODE = txnInfoObj.getString("RESPCODE");
                String RESPMSG = txnInfoObj.getString("RESPMSG");
//                    String STATUS = txnInfoObj.getString("STATUS");
                String TXNAMOUNT = txnInfoObj.getString("TXNAMOUNT");
                String TXNDATE = txnInfoObj.getString("TXNDATE");
                String TXNID = txnInfoObj.getString("TXNID");

                CustomerTransaction customerTransaction = new CustomerTransaction(BANKNAME, BANKTXNID, _CURRENCY, GATEWAYNAME, ORDERID, PAYMENTMODE, RESPCODE, RESPMSG, TXNAMOUNT, TXNDATE, TXNID);

                placeOrder(new CustomerOrder(checkoutController.getDeliveryTime(),
                        checkoutController.getDeliveryAddress(),
                        cartController.getCartItems(),
                        cartController.getPayableAmount(),
                        customerTransaction));
            } else {
                String resultMsg = response.getHeaderString("resultMsg");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void placeOrder(CustomerOrder customerOrder) {
        checkoutService.placeOrder(customerOrder);
    }
//    public void processTransaction(String paymentMode) {
//        Response response = null;
//        try {
//            response = client.target("http://localhost:8080/myProject/webapi/Checkout/process-transaction")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .post(Entity.entity(paymentMode, MediaType.APPLICATION_JSON), Response.class);
//            
//            if (response.getStatus() == Response.Status.TEMPORARY_REDIRECT.getStatusCode()) {                
//                FacesContext.getCurrentInstance().getExternalContext().redirect(response.getLocation().toString());
//            } else {
//                String resultMsg = response.getHeaderString("resultMsg");
//                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong. Please try again.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//    }

}
