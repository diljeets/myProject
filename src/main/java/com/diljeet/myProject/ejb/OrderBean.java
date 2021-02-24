/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.controllers.CartController;
import com.diljeet.myProject.controllers.CheckoutController;
import com.diljeet.myProject.entities.CustomerOrder;
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
public class OrderBean {

    private static final Logger logger = Logger.getLogger(OrderBean.class.getCanonicalName());

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

    public void placeOrder(CustomerOrder customerOrder) {
        String paymentMode = customerOrder.getPaymentMode();
        Response response = null;
        try {
            response = client.target("http://localhost:8080/myProject/webapi/Order/placeOrder")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(customerOrder, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                logger.log(Level.SEVERE, "Order Placed Successfully");
            } else {
                logger.log(Level.SEVERE, "Could not place Order.");
            }
            if (paymentMode.equals("POD")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(req.getContextPath() + "/webapi/Order/pgResponse");
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
