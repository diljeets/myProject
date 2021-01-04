/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.controllers.TemplateController;
import com.diljeet.myProject.ejb.PaymentGatewayBean;
import com.diljeet.myProject.entities.Cart;
import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.CustomerTransaction;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import com.diljeet.myProject.interfaces.CheckoutService;
import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.MyProjectUtils;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author diljeet
 */
@Stateful
public class CheckoutServiceBean implements CheckoutService {

    private static final Logger logger = Logger.getLogger(CheckoutServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @EJB
    PaymentGatewayBean paymentGatewayBean;

    @Inject
    HttpServletRequest req;

    @Inject
    TemplateController templateController;

    private String deliveryAddress;
    private String deliveryTime;
//    private List<Cart> cartItems;

    public CheckoutServiceBean() {
//        cartItems = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
    }

    @Override
    public void addDeliveryTime(String selectedTime) {
        this.deliveryTime = selectedTime;
    }
//
//    @Override
//    public Response getDeliveryTime() {
//        logger.log(Level.SEVERE, "getDeliveryTime service bean {0}", deliveryTime);
//        return Response.ok().entity(deliveryTime).build();
//    }

    @Override
    public void addDeliveryAddress(RegisteredUsersAddress selectedAddress) {
        deliveryAddress = selectedAddress.getHouseNo() + ", "
                + selectedAddress.getBuildingNo() + ", "
                + selectedAddress.getStreet() + ", "
                + selectedAddress.getCity() + ", "
                + selectedAddress.getState() + "-"
                + selectedAddress.getPincode();
    }

    @Override
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    @Override
    public Response initiateTransaction(InitiateTransaction initiateTransaction) {
        String orderId = MyProjectUtils.createOrderId();
        String username = req.getUserPrincipal().getName();
        String payableAmount = initiateTransaction.getPayableAmount();
        String channelId = initiateTransaction.getChannelId();
        String callbackUrl = initiateTransaction.getCallbackUrl();
        Response response = null;
        try {
            response = paymentGatewayBean.initiateTransaction(orderId, payableAmount, username, channelId, callbackUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<PaymentOptions> fetchPaymentOptions() {
        List<PaymentOptions> paymentOptions = null;
        try {
            paymentOptions = paymentGatewayBean.getPaymentOptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentOptions;
    }

    @Override
    public List<PayChannelOptionsPaytmBalance> fetchPayChannelOptionsPaytmBalance() {
        List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance = null;
        try {
            payChannelOptionsPaytmBalance = paymentGatewayBean.getPayChannelOptionsPaytmBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payChannelOptionsPaytmBalance;
    }

    @Override
    public List<PayChannelOptionsNetBanking> fetchPayChannelOptionsNetBanking() {
        List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking = null;
        try {
            payChannelOptionsNetBanking = paymentGatewayBean.getPayChannelOptionsNetBanking();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payChannelOptionsNetBanking;
    }

    @Override
    public Response sendOTP(String paytmMobile) {
        Response response = null;
        try {
            response = paymentGatewayBean.sendOTP(paytmMobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response validateOtpAndFetchPaytmBalance(String otp) {
        Response response = null;
        try {
            response = paymentGatewayBean.validateOtpAndFetchPaytmBalance(otp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response fetchBinDetails(String firstSixCardDigits) {
        Response response = null;
        try {
            response = paymentGatewayBean.fetchBinDetails(firstSixCardDigits);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<CardDetails> fetchCardDetails() {
        List<CardDetails> cardDetails = null;
        try {
            cardDetails = paymentGatewayBean.getCardDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardDetails;
    }

    @Override
    public void fetchOtherNetBankingPaymentChannels() {
        try {
            paymentGatewayBean.fetchOtherNetBankingPaymentChannels();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Response processTransaction(PaymentRequestDetails paymentRequestDetails) {
        Response response = null;
        try {
            response = paymentGatewayBean.processTransaction(paymentRequestDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void pgGetResponse(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect(req.getContextPath() + "/order-status.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(CheckoutServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Response pgPostResponse(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> mapData = req.getParameterMap();
        JSONObject parameters = new JSONObject();
        mapData.forEach((key, val) -> parameters.put(key, val[0]));
        logger.log(Level.SEVERE, "Final payment response is {0}", parameters.toString());

        //Get Customer Transaction Status
        paymentGatewayBean.getCustomerTransactionStatus(parameters.toString());

        return Response
                .seeOther(URI.create("/Checkout/pgResponse"))
                .build();
    }

    @Override
    public Response getCustomerTransactionStatus() {
        try {
            CustomerTransaction customerTransaction = paymentGatewayBean.getCustomerTransaction();
            if (customerTransaction != null) {
                return Response
                        .status(Response.Status.FOUND)
                        .entity(customerTransaction)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response
                .status(Response.Status.NOT_FOUND)
                .build();
    }

    @Override
    public Response transactionStatus(String orderId) {
        Response response = null;
        try {
            response = paymentGatewayBean.transactionStatus(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response placeOrder(CustomerOrder customerOrder) {
        String orderId = customerOrder.getCustomerTransaction().getOrderId();
//        String payableAmount = customerOrder.getPayableAmount();
        String customerName = templateController.getCurrentCustomer();
        String username = req.getUserPrincipal().getName();
        try {
//            paymentGatewayBean.initiateTransaction(orderId, payableAmount, username);
            customerOrder.setCustomerName(customerName);
            customerOrder.setUsername(username);
            customerOrder.setDateOrderCreated(new Date());
            customerOrder.setOrderId(orderId);
            List<Cart> cartItems = customerOrder.getOrders();
            for (Cart cartItem : cartItems) {
                cartItem.setCustomerOrder(customerOrder);
            }
            em.persist(customerOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

}
