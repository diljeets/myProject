/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.CheckoutController;
import com.diljeet.myProject.entities.CustomerTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/OrderStatus/getCustomerTransactionStatus")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .get();
            if (response.getStatus() == Response.Status.FOUND.getStatusCode()
                    && response.hasEntity()) {
                customerTransaction = response.readEntity(CustomerTransaction.class);
                logger.log(Level.SEVERE, "customerTransaction Object found");
            } else {
                logger.log(Level.SEVERE, "customerTransaction Object not found");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not place Order");
        } finally {
            clear();
        }

        return customerTransaction;
    }   
    
    public void clear() {
        checkoutController.setIsModeCC(false);
        checkoutController.setIsModeDC(false);
        checkoutController.setIsModeNB(false);
        checkoutController.setIsModePaytm(false);
        checkoutController.setIsModePOD(false);
        checkoutController.setPaymentOption(null);
        checkoutController.setSavedInstrument(null);
    }

}
