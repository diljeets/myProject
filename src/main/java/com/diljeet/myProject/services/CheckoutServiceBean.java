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
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import com.diljeet.myProject.interfaces.CheckoutService;
import com.diljeet.myProject.utils.InitiateTransaction;
import com.diljeet.myProject.utils.MyProjectUtils;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.RequestDispatcher;
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
//    private String orderId;

    public CheckoutServiceBean() {
    }

    @PostConstruct
    public void init() {
    }

    @Override
    public void addDeliveryTime(String selectedTime) {
        deliveryTime = selectedTime;
    }

    @Override
    public String getDeliveryTime() {
        return deliveryTime;
    }

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

//    @Override
//    public void initiateTransaction(String payableAmount, String channelId) {
//        String orderId = MyProjectUtils.createOrderId();
//        String username = req.getUserPrincipal().getName();        
//        try {
//            paymentGatewayBean.initiateTransaction(orderId, payableAmount, username, channelId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void initiateTransaction(InitiateTransaction initiateTransaction) {
        String orderId = MyProjectUtils.createOrderId();
        String username = req.getUserPrincipal().getName();
        String payableAmount = initiateTransaction.getPayableAmount();
        String channelId = initiateTransaction.getChannelId();
        String callbackUrl = initiateTransaction.getCallbackUrl();
        try {
            paymentGatewayBean.initiateTransaction(orderId, payableAmount, username, channelId, callbackUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    @Override
//    public Response processTransaction(String paymentMode, HttpServletRequest req, HttpServletResponse res) {
//        Response response = null;
//        try {
//            response = paymentGatewayBean.processTransaction(paymentMode, req, res);
//            String callbackUrl = response.getHeaderString("callbackUrl");
//            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher(callbackUrl);
//            dispatcher.forward(req,res);
//        } catch (Exception e) {
//             e.printStackTrace();
//        }
//        return null;
//    }
    @Override
    public Response processTransaction(String paymentMode) {
        Response response = null;
        try {
            response = paymentGatewayBean.processTransaction(paymentMode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

//    @Override
//    public String processTransaction(String paymentMode, 
//            HttpServletRequest req, 
//            HttpServletResponse res) {
//        String returnUrl = null;
//        try {
//            returnUrl = paymentGatewayBean.processTransaction(paymentMode);            
//            res.sendRedirect(returnUrl);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }        
//        return returnUrl;
//    }
//    @Override
//    public void placeOrder(CustomerOrder customerOrder) {
//        String orderId = MyProjectUtils.createOrderId();
//        String payableAmount = customerOrder.getPayableAmount();
//        String customerName = templateController.getCurrentCustomer();
//        String username = req.getUserPrincipal().getName();
//        try {
//            paymentGatewayBean.initiateTransaction(orderId, payableAmount, username);
//            customerOrder.setCustomerName(customerName);
//            customerOrder.setUsername(username);
//            customerOrder.setDateOrderCreated(new Date());
//            customerOrder.setOrderId(orderId);
//            List<Cart> cartItems = customerOrder.getOrders();
//            for (Cart cartItem : cartItems) {
//                cartItem.setCustomerOrder(customerOrder);
//            }
////            em.persist(customerOrder);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    
}
