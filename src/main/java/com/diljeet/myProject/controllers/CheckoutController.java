/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.interfaces.CheckoutService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

/**
 *
 * @author diljeet
 */
@Named(value = "checkoutController")
@SessionScoped
public class CheckoutController implements Serializable {

    private static final Logger logger = Logger.getLogger(CheckoutController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;
    
    private CustomerOrder customerOrder;
    
    private String deliveryTime;

    private String deliveryAddress;

    @Inject
    CartController cartController;

    @EJB
    CheckoutService checkoutService;

    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public CheckoutController() {
        customerOrder = new CustomerOrder();
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }    
    
    public String getDeliveryAddress() {
        return checkoutService.getDeliveryAddress();
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public CartController getCartController() {
        return cartController;
    }

    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }

    public void addDeliveryAddress(RegisteredUsersAddress selectedAddress) {
        checkoutService.addDeliveryAddress(selectedAddress);
    }
    
    public void addDeliveryTime(AjaxBehaviorEvent event) {       
        String selectedTime = (String)((UIOutput)event.getSource()).getValue();                
        checkoutService.addDeliveryTime(selectedTime);
    }

    public void placeOrder(CustomerOrder customerOrder) {
        checkoutService.placeOrder(customerOrder);
    }

    public void placeOrder() {
        placeOrder(new CustomerOrder(getDeliveryTime(), 
                getDeliveryAddress(), 
                cartController.getCartItems()));
    }

}
