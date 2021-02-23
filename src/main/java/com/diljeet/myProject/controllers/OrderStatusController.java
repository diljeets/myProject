/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.OrderStatusBean;
import com.diljeet.myProject.entities.Cart;
import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.CustomerTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author diljeet
 */
@Named(value = "orderStatusController")
@RequestScoped
public class OrderStatusController implements Serializable {

    private static final Logger logger = Logger.getLogger(OrderStatusController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private CustomerTransaction customerTransaction;

//    @Inject
//    HttpServletRequest req;
//
//    @Inject
//    CheckoutController checkoutController;
//
//    @Inject
//    CartController cartController;
//
//    @Inject
//    TemplateController templateController;
//
    @EJB
    OrderStatusBean orderStatusBean;

    @PostConstruct
    public void init() {
        setCustomerTransaction(orderStatusBean.getCustomerTransactionStatus());
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public OrderStatusController() {
    }

    public CustomerTransaction getCustomerTransaction() {
//        return orderStatusBean.getCustomerTransactionStatus();
        return customerTransaction;
    }

    public void setCustomerTransaction(CustomerTransaction customerTransaction) {
        this.customerTransaction = customerTransaction;
    }

//    public void placeOrder(String paymentMode) {
//        String orderId = checkoutController.getOrderId();
//        String deliveryTime = checkoutController.getDeliveryController().getDeliveryTime();
//        String deliveryAddress = checkoutController.getDeliveryController().getDeliveryAddress();
//        String _paymentMode = paymentMode;
//        List<Cart> cartItems = cartController.getCartItems();
//        String payableAmount = cartController.getPayableAmount();
//        String customerName = templateController.getCurrentCustomer();
//        String username = req.getUserPrincipal().getName();
//        if (paymentMode.equals("POD")) {
//            orderStatusBean.placeOrder(new CustomerOrder(orderId,
//                    deliveryTime,
//                    deliveryAddress,
//                    _paymentMode,
//                    cartItems,
//                    payableAmount,
//                    customerName,
//                    username
//            ));
//        } else {
//            orderStatusBean.placeOrder(new CustomerOrder(orderId,
//                    deliveryTime,
//                    deliveryAddress,
//                    _paymentMode,
//                    cartItems,
//                    payableAmount,
//                    customerName,
//                    username,
//                    orderStatusBean.getCustomerTransactionStatus()
//            ));
//        }
//
//    }
//    public void placeOrder(String paymentMode) {
//        String orderId = checkoutController.getOrderId();
//        String deliveryTime = checkoutController.getDeliveryController().getDeliveryTime();
//        String deliveryAddress = checkoutController.getDeliveryController().getDeliveryAddress();
//        String _paymentMode = paymentMode;
//        List<Cart> cartItems = cartController.getCartItems();
//        String payableAmount = cartController.getPayableAmount();
//        String customerName = templateController.getCurrentCustomer();
//        String username = templateController.getUsername();
//        //Create CustomerOrder Object for PaymentMode POD
//        orderStatusBean.placeOrder(new CustomerOrder(orderId,
//                deliveryTime,
//                deliveryAddress,
//                _paymentMode,
//                cartItems,
//                payableAmount,
//                customerName,
//                username
//        ));
//    }
//
//    public void placeOrder(CustomerTransaction customerTransaction) {
//        String orderId = checkoutController.getOrderId();
//        String deliveryTime = checkoutController.getDeliveryController().getDeliveryTime();
//        String deliveryAddress = checkoutController.getDeliveryController().getDeliveryAddress();
//        String _paymentMode = customerTransaction.getPaymentMode();
//        List<Cart> cartItems = cartController.getCartItems();
//        String payableAmount = cartController.getPayableAmount();
//        String customerName = templateController.getCurrentCustomer();
//        String username = templateController.getUsername();
//        //Create CustomerOrder Object for PaymentMode PPI/CC/DB/NB
//        orderStatusBean.placeOrder(new CustomerOrder(orderId,
//                deliveryTime,
//                deliveryAddress,
//                _paymentMode,
//                cartItems,
//                payableAmount,
//                customerName,
//                username,
//                customerTransaction
//        ));
//    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }

}
