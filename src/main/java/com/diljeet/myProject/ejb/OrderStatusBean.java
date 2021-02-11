/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.CartController;
import com.diljeet.myProject.controllers.CheckoutController;
import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.CustomerTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class OrderStatusBean {

    private static final Logger logger = Logger.getLogger(OrderStatusBean.class.getCanonicalName());

    private Client client;

    @Inject
    HttpServletRequest req;

    @Inject
    CheckoutController checkoutController;

    @Inject
    CartController cartController;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public CustomerTransaction getCustomerTransactionStatus() {
        CustomerTransaction customerTransaction = null;
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/Checkout/getCustomerTransactionStatus")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get();
            if (response.getStatus() == Response.Status.FOUND.getStatusCode()
                    && response.hasEntity()) {
                customerTransaction = response.readEntity(CustomerTransaction.class);
                logger.log(Level.SEVERE, "orderId for customer order is {0}", customerTransaction.getOrderId());
//                logger.log(Level.SEVERE, "Delivery time is {0}", checkoutController.getDeliveryTime());
                if ((customerTransaction.getRespCode()).equals("01")) {
                    placeOrder(new CustomerOrder(checkoutController.getOrderId(),
                            checkoutController.getDeliveryController().getDeliveryTime(),
                            checkoutController.getDeliveryController().getDeliveryAddress(),
                            checkoutController.getPaymentMode(),
                            cartController.getCartItems(),
                            cartController.getPayableAmount(),
                            customerTransaction
                    ));
                }
            } else {
                logger.log(Level.SEVERE, "customerTransaction Object is not found");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not place Order");
        }

        return customerTransaction;
    }

    public void placeOrder() {
        logger.log(Level.SEVERE, "inside placeOrder method");
        placeOrder(new CustomerOrder(checkoutController.getOrderId(),
                checkoutController.getDeliveryController().getDeliveryTime(),
                checkoutController.getDeliveryController().getDeliveryAddress(),
                checkoutController.getPaymentMode(),
                cartController.getCartItems(),
                cartController.getPayableAmount()
        ));
    }

    public void placeOrder(CustomerOrder customerOrder) {
//        checkoutService.placeOrder(customerOrder);
        String paymentMode = customerOrder.getPaymentMode();
        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Checkout/placeOrder")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(customerOrder, MediaType.APPLICATION_JSON), Response.class);
//                    .buildPost(Entity.entity(customerOrder, MediaType.APPLICATION_JSON))                  
//                    .submit(Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.log(Level.SEVERE, "Order Placed Successfully");
                if (paymentMode.equals("POD"))
                    FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/myProject/order-status.xhtml");
            } else {
                logger.log(Level.SEVERE, "Could not place Order.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
        }

    }

    public void clear() {
        cartController.removeAllFromCart();
        cartController.setPayableAmount(null);
        checkoutController.setOrderId(null);
        checkoutController.setPaymentMode(null);
        checkoutController.setIsModeCC(false);
        checkoutController.setIsModeDC(false);
        checkoutController.setIsModeNB(false);
        checkoutController.setIsModePaytm(false);
        checkoutController.setIsModePOD(false);
        checkoutController.setPaymentOption(null);
        checkoutController.setSavedInstrument(null);
    }

}
