/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.RedirectFormController;
import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import com.diljeet.myProject.utils.RedirectForm;
import java.util.List;
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

    private RedirectForm redirectForm;

    @Inject
    HttpServletRequest req;

    @Inject
    RedirectFormController redirectFormController;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public RedirectForm getRedirectForm() {
        return redirectForm;
    }

    public void setRedirectForm(RedirectForm redirectForm) {
        this.redirectForm = redirectForm;
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
                logger.log(Level.INFO, resultMsg);
//                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", resultMsg);
//                FacesContext.getCurrentInstance().addMessage(null, msg);
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
    
    public void fetchOtherNetBankingPaymentChannels() {        
        try {
            client.target("http://localhost:8080/myProject/webapi/Checkout/fetchOtherNetBankingPaymentChannels")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());            
        }        
    }

    public void processTransaction(PaymentRequestDetails paymentRequestDetails) {
        if (paymentRequestDetails == null) {
            return;
        }
        String paymentMode = paymentRequestDetails.getPaymentMode();
        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Checkout/processTransaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(paymentRequestDetails, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                if (response.hasEntity()) {
                    JSONObject bodyObj = new JSONObject(response.readEntity(String.class));
                    JSONObject bankFormObj = bodyObj.getJSONObject("bankForm");
                    JSONObject redirectFormObj = bankFormObj.getJSONObject("redirectForm");
                    String actionUrl = redirectFormObj.getString("actionUrl");
                    String method = redirectFormObj.getString("method");
                    String type = redirectFormObj.getString("type");

                    JSONObject headersObj = redirectFormObj.getJSONObject("headers");
                    String content_type = headersObj.getString("Content-Type");

                    JSONObject contentObj = redirectFormObj.getJSONObject("content");
                    String md = contentObj.getString("MD");
                    String paReq;
                    String termUrl;
                    String sbmttype;
                    String pid;
                    String es;
                    if (paymentMode.equals("CREDIT_CARD") || paymentMode.equals("DEBIT_CARD")) {
                        paReq = contentObj.getString("PaReq");
                        termUrl = contentObj.getString("TermUrl");
                        redirectForm = new RedirectForm(
                                actionUrl,
                                method,
                                type,
                                content_type,
                                md,
                                paReq,
                                termUrl
                        );
                    }
                    if (paymentMode.equals("NET_BANKING")) {
                        sbmttype = contentObj.getString("SBMTTYPE");
                        pid = contentObj.getString("PID");
                        es = contentObj.getString("ES");
                        redirectForm = new RedirectForm(
                                actionUrl,
                                method,
                                type,
                                content_type,
                                md,
                                sbmttype,
                                pid,
                                es
                        );
                    }
                }

                //Redirect if paymode is BALANCE / CREDIT_CARD / DEBIT_CARD / NET_BANKING
                FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/myProject/redirect-form.xhtml");

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

}
