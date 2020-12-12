/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.CheckoutController;
import com.diljeet.myProject.utils.InitiateTransaction;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
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

//    @Inject
//    CheckoutController checkoutController;
    @Inject
    HttpServletRequest req;

//    @Inject
//    ServletRequest servletRequest;
//    
//    @Inject
//    ServletResponse servletResponse;
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
                "http://localhost:8080/myProject/callback.xhtml");
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/initiate-transaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(initiateTransaction, MediaType.APPLICATION_JSON), Response.class);
//                    .post(Entity.entity(payableAmount, MediaType.APPLICATION_JSON), Response.class);
//            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
//                logger.log(Level.SEVERE, "OTP sent successfully");
//            } else {
//                logger.log(Level.SEVERE, "Error sending OTP");
//            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        }

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
//        logger.log(Level.SEVERE, "I m fetchbin {0}", firstSixCardDigits);
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

//    public void processTransaction(String paymentMode) {
//        Response response = null;
//        try {
//            response = client.target("http://localhost:8080/myProject/webapi/Checkout/process-transaction")
//                    .path(paymentMode)
//                    .request(MediaType.TEXT_HTML)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
//            FacesContext.getCurrentInstance().addMessage(null, msg);            
//        }
//    }

    public void processTransaction(String paymentMode) {
        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Checkout/process-transaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(paymentMode, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.TEMPORARY_REDIRECT.getStatusCode()) {                
                FacesContext.getCurrentInstance().getExternalContext().redirect(response.getLocation().toString());
            }
//            String callback = response.getHeaderString("callbackUrl");
//            RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(callback);
//            dispatcher.forward(servletRequest, servletResponse);
//            String resultMsg = response.getHeaderString("resultMsg");
//            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
//                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", resultMsg);
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//            } else {
//                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
//        return returnUrl;
    }

}
