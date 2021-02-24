/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.OrderBean;
import com.diljeet.myProject.ejb.OrderStatusBean;
import com.diljeet.myProject.entities.Cart;
import com.diljeet.myProject.entities.CustomerOrder;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author diljeet
 */
@Named(value = "orderController")
@SessionScoped
public class OrderController implements Serializable {

    private static final Logger logger = Logger.getLogger(OrderController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    @Inject
    HttpServletRequest req;

    @Inject
    CheckoutController checkoutController;

    @Inject
    CartController cartController;

    @Inject
    TemplateController templateController;

    @EJB
    OrderBean orderBean;

    @EJB
    OrderStatusBean orderStatusBean;

    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public OrderController() {
    }
    
    public void createAndPlaceCustomerOrder() {
        String orderId = checkoutController.getOrderId();
        String deliveryTime = checkoutController.getDeliveryController().getDeliveryTime();
        String deliveryAddress = checkoutController.getDeliveryController().getDeliveryAddress();
        String paymentMode = checkoutController.getPaymentMode();
        List<Cart> cartItems = cartController.getCartItems();
        String payableAmount = cartController.getPayableAmount();
        String customerName = templateController.getCurrentCustomer();
        String username = req.getUserPrincipal().getName();
        if (paymentMode.equals("POD")) {
            orderBean.placeOrder(new CustomerOrder(orderId,
                    deliveryTime,
                    deliveryAddress,
                    paymentMode,
                    cartItems,
                    payableAmount,
                    customerName,
                    username
            ));
        } else {
            orderBean.placeOrder(new CustomerOrder(orderId,
                    deliveryTime,
                    deliveryAddress,
                    paymentMode,
                    cartItems,
                    payableAmount,
                    customerName,
                    username,
                    orderStatusBean.getCustomerTransactionStatus()
            ));
        }

    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }

}
