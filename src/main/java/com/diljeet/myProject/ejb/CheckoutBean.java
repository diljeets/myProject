/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.CartController;
import com.diljeet.myProject.controllers.CheckoutController;
import com.diljeet.myProject.controllers.OrderController;
import com.diljeet.myProject.controllers.RedirectFormController;
import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.FetchBalanceAndInstruments;
import com.diljeet.myProject.utils.FetchBinDetails;
import com.diljeet.myProject.utils.FetchPaymentOptions;
import com.diljeet.myProject.utils.FetchPaymentOptionsAndNetbankingChannels;
import com.diljeet.myProject.utils.FetchPaytmBalance;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import com.diljeet.myProject.utils.RedirectForm;
import com.diljeet.myProject.utils.SavedInstruments;
import com.diljeet.myProject.utils.SendOtp;
import com.diljeet.myProject.utils.UpdateTransaction;
import com.diljeet.myProject.utils.ValidateOtp;
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
    CheckoutController checkoutController;

    @Inject
    CartController cartController;

    @Inject
    RedirectFormController redirectFormController;

    @Inject
    OrderController orderController;

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

    public String createOrderId() {
        String orderId = null;
        try {
            orderId = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/createOrderId")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    public void initiateTransaction(String payableAmount, String orderId) {
        if (payableAmount == null) {
            return;
        }
        InitiateTransaction initiateTransaction = new InitiateTransaction(payableAmount,
                orderId,
                req.getUserPrincipal().getName(),
                "WEB",
                "http://192.168.43.80:8080/myProject/webapi/Order/pgResponse");
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/initiateTransaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(initiateTransaction, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                if (response.hasEntity()) {
                    String transactionToken = response.readEntity(String.class);
                    checkoutController.setTransactionToken(transactionToken);
                    FetchPaymentOptionsAndNetbankingChannels fetchPaymentOptionsAndNetbankingChannels
                            = fetchPaymentOptions(checkoutController.getOrderId(), checkoutController.getTransactionToken());
                    setPaymentOptionsAndNetbankingChannels(fetchPaymentOptionsAndNetbankingChannels);
                } else {
                    FetchPaymentOptionsAndNetbankingChannels fetchPaymentOptionsAndNetbankingChannels = updateTransaction(checkoutController.getOrderId(), cartController.getPayableAmount(), req.getUserPrincipal().getName(), checkoutController.getTransactionToken());
                    setPaymentOptionsAndNetbankingChannels(fetchPaymentOptionsAndNetbankingChannels);
                }
            } else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Your Session has Expired. Kindly Login again.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "1Something went wrong. Please try again.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    private void setPaymentOptionsAndNetbankingChannels(FetchPaymentOptionsAndNetbankingChannels fetchPaymentOptionsAndNetbankingChannels) {
        if (fetchPaymentOptionsAndNetbankingChannels != null) {
            List<PaymentOptions> paymentOptions = fetchPaymentOptionsAndNetbankingChannels.getPaymentOptions();
            List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking = fetchPaymentOptionsAndNetbankingChannels.getPayChannelOptionsNetBanking();
            if (paymentOptions != null) {
                checkoutController.setPaymentOptions(paymentOptions);
            }
            if (payChannelOptionsNetBanking != null) {
                checkoutController.setPayChannelOptionsNetBanking(payChannelOptionsNetBanking);
            }
        }
    }

//    public List<PaymentOptions> fetchPaymentOptions() {
//        List<PaymentOptions> paymentOptions = null;
//        try {
//            paymentOptions = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/fetchPaymentOptions")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get(new GenericType<List<PaymentOptions>>() {
//                    });
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Payment Options. Please try again.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//        return paymentOptions;
//    }
    public FetchPaymentOptionsAndNetbankingChannels fetchPaymentOptions(String orderId, String transactionToken) {
        FetchPaymentOptionsAndNetbankingChannels fetchPaymentOptionsAndNetbankingChannels = null;
        FetchPaymentOptions fetchPaymentOptions = new FetchPaymentOptions(orderId, transactionToken);
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/paymentOptions/fetch")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(fetchPaymentOptions, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                if (response.hasEntity()) {
                    fetchPaymentOptionsAndNetbankingChannels = response.readEntity(FetchPaymentOptionsAndNetbankingChannels.class);
                }
            } else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Your Session has Expired. Kindly Login again.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong. Please try again.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Payment Options. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return fetchPaymentOptionsAndNetbankingChannels;
    }

    public FetchPaymentOptionsAndNetbankingChannels updateTransaction(String orderId, String payableAmount, String username, String transactionToken) {
        FetchPaymentOptionsAndNetbankingChannels fetchPaymentOptionsAndNetbankingChannels = null;
        UpdateTransaction updateTransaction = new UpdateTransaction(orderId, payableAmount, username, transactionToken);
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/transaction/update")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(updateTransaction, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                fetchPaymentOptionsAndNetbankingChannels = fetchPaymentOptions(orderId, transactionToken);
            } else if (response.getStatus() == Response.Status.NOT_ACCEPTABLE.getStatusCode()) {
                checkoutController.setOrderId(createOrderId());
                initiateTransaction(cartController.getPayableAmount(), checkoutController.getOrderId());
            } else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Your Session has Expired. Kindly Login again.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong. Please try again.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Payment Options. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return fetchPaymentOptionsAndNetbankingChannels;
    }

//    public List<PayChannelOptionsPaytmBalance> fetchPayChannelOptionsPaytmBalance() {
//        List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance = null;
//        try {
//            payChannelOptionsPaytmBalance = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/fetchPayChannelOptionsPaytmBalance")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get(new GenericType<List<PayChannelOptionsPaytmBalance>>() {
//                    });
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Balance Options. Please try again.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//        return payChannelOptionsPaytmBalance;
//    }
//
//    public List<SavedInstruments> fetchSavedInstruments() {
//        List<SavedInstruments> savedInstruments = null;
//        try {
//            savedInstruments = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/fetchSavedInstruments")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get(new GenericType<List<SavedInstruments>>() {
//                    });
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Saved Cards. Please try again.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//        return savedInstruments;
//    }
//
//    public List<PayChannelOptionsNetBanking> fetchPayChannelOptionsNetBanking() {
//        List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking = null;
//        try {
//            payChannelOptionsNetBanking = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/fetchPayChannelOptionsNetBanking")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get(new GenericType<List<PayChannelOptionsNetBanking>>() {
//                    });
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Banking Options. Please try again.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//        return payChannelOptionsNetBanking;
//    }
    public void sendOTP(String paytmMobile) {
        if (paytmMobile == null) {
            return;
        }
        SendOtp sendOtp = new SendOtp(checkoutController.getOrderId(), paytmMobile, checkoutController.getTransactionToken());
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/otp/send")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(sendOtp, MediaType.APPLICATION_JSON), Response.class);
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

    public void validateOtp(String otp) {
        ValidateOtp validateOtp = null;
        if (otp == null) {
            return;
        }
        if (validateOtp == null) {
            validateOtp = new ValidateOtp(checkoutController.getOrderId(), otp, checkoutController.getTransactionToken());
        }
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/otp/validate")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(validateOtp, MediaType.APPLICATION_JSON), Response.class);
            String resultMsg = response.getHeaderString("resultMsg");
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                FetchBalanceAndInstruments fetchBalanceAndInstruments = fetchPaytmBalance(checkoutController.getOrderId(), checkoutController.getTransactionToken());
                if (fetchBalanceAndInstruments != null) {
                    List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance = fetchBalanceAndInstruments.getPayChannelOptionsPaytmBalance();
                    List<SavedInstruments> savedInstruments = fetchBalanceAndInstruments.getSavedInstruments();
                    if (payChannelOptionsPaytmBalance != null) {
                        checkoutController.setPayChannelOptionsPaytmBalance(payChannelOptionsPaytmBalance);
                    }
                    if (savedInstruments != null) {
                        checkoutController.setSavedInstruments(savedInstruments);
                    }
                    FacesContext.getCurrentInstance().getExternalContext().redirect("http://192.168.43.80:8080/myProject/select-payment-option.xhtml");
                }

            } else if (response.getStatus() == Response.Status.NOT_ACCEPTABLE.getStatusCode()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public FetchBalanceAndInstruments fetchPaytmBalance(String orderId, String transactionToken) {
        FetchBalanceAndInstruments fetchBalanceAndInstruments = null;
        FetchPaytmBalance fetchPaytmBalance = new FetchPaytmBalance(orderId, transactionToken);
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/paymentOptions/balance/fetch")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(fetchPaytmBalance, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                if (response.hasEntity()) {
                    fetchBalanceAndInstruments = response.readEntity(FetchBalanceAndInstruments.class);
                }
            } else if (response.getStatus() == Response.Status.NOT_ACCEPTABLE.getStatusCode()) {
                String resultMsg = response.getHeaderString("resultMsg");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", resultMsg);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Payment Options. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return fetchBalanceAndInstruments;
    }

    public boolean fetchBinDetails(String firstSixCardDigits) {
        if (firstSixCardDigits == null) {
            return false;
        }
        FetchBinDetails fetchBinDetails = new FetchBinDetails(checkoutController.getOrderId(), firstSixCardDigits, checkoutController.getTransactionToken());
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/card/fetchBinDetails")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(fetchBinDetails, MediaType.APPLICATION_JSON), Response.class);
            String resultMsg = response.getHeaderString("resultMsg");
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                if (response.hasEntity()) {
                    List<CardDetails> cardDetails = response.readEntity(new GenericType<List<CardDetails>>() {
                    });
                    if (cardDetails != null) {
                        checkoutController.setCardDetails(cardDetails);
                    }
                }
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

//    public List<CardDetails> fetchCardDetails() {
//        List<CardDetails> cardDetails = null;
//        try {
//            cardDetails = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/fetchCardDetails")
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get(new GenericType<List<CardDetails>>() {
//                    });
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage());
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Could not fetch Card Details. Please try again.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//        return cardDetails;
//    }
    public void fetchOtherNetBankingPaymentChannels(String orderId, String transactionToken) {
        List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking = null;
        try {
            payChannelOptionsNetBanking = client
                    .target("http://192.168.43.80:8080/myProject/webapi/Checkout/netBankingPaymentChannels/other/fetch")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .header("orderId", orderId)
                    .header("transactionToken", transactionToken)
                    .get(new GenericType<List<PayChannelOptionsNetBanking>>() {
                    });

            if (payChannelOptionsNetBanking != null) {
                checkoutController.setPayChannelOptionsNetBanking(payChannelOptionsNetBanking);
            }
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
            response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/processTransaction")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(paymentRequestDetails, MediaType.APPLICATION_JSON), Response.class
                    );
            if (response.getStatus() == Response.Status.TEMPORARY_REDIRECT.getStatusCode()) {
                //Redirect if paymode is BALANCE and Customer Transaction is unsuccessful
                FacesContext.getCurrentInstance().getExternalContext().redirect(response.getLocation().toString());
            } else if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                if (response.hasEntity()) {
                    JSONObject bodyObj = new JSONObject(response.readEntity(String.class
                    ));
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

                    //Redirect if paymode is CREDIT_CARD / DEBIT_CARD / NET_BANKING
                    FacesContext.getCurrentInstance().getExternalContext().redirect("http://192.168.43.80:8080/myProject/redirect-form.xhtml");
                } else {
                    //Save Customer Order in Database and Redirect if paymode is BALANCE and Customer Transaction is successful
                    orderController.createAndPlaceCustomerOrder();
                    FacesContext.getCurrentInstance().getExternalContext().redirect(response.getLocation().toString());
                }
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
            response = client.target("http://192.168.43.80:8080/myProject/webapi/Checkout/transactionStatus")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(orderId, MediaType.APPLICATION_JSON), Response.class
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
