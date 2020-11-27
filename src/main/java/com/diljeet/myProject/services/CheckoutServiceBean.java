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
import com.diljeet.myProject.utils.MyProjectUtils;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

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
        deliveryAddress = selectedAddress.getHouseNo() + ", " +
                selectedAddress.getBuildingNo() +  ", " +
                selectedAddress.getStreet() +  ", " +
                selectedAddress.getCity() +  ", " +
                selectedAddress.getState() + "-" +
                selectedAddress.getPincode();
    }

    @Override
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    @Override
    public void placeOrder(CustomerOrder customerOrder) {
        String orderId = MyProjectUtils.createOrderId();
        String payableAmount = customerOrder.getPayableAmount();
        String customerName = templateController.getCurrentCustomer();
        String username = req.getUserPrincipal().getName();
        try {                
            paymentGatewayBean.initiateTransaction(orderId, payableAmount, username);
            customerOrder.setCustomerName(customerName);
            customerOrder.setUsername(username);
            customerOrder.setDateOrderCreated(new Date());
            customerOrder.setOrderId(orderId);
            List<Cart> cartItems = customerOrder.getOrders();
            for(Cart cartItem : cartItems){
                cartItem.setCustomerOrder(customerOrder);
            }
//            em.persist(customerOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    

}
