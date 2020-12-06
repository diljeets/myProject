/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.CheckoutController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/initiate-transaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(payableAmount, MediaType.APPLICATION_JSON), Response.class);
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
    
    public void validateOtpAndFetchBalanceInfo(String otp) {
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
    
    public void processTransaction(String paymentMode) {       
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/process-transaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(paymentMode, MediaType.APPLICATION_JSON),Response.class);
//            String resultMsg = response.getHeaderString("resultMsg");
//            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
//                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", resultMsg);
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//            } else {
//                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//            }
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

}
