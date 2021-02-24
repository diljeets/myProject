/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.ejb.PaymentGatewayBean;
import com.diljeet.myProject.entities.CustomerTransaction;
import java.util.logging.Logger;
import com.diljeet.myProject.interfaces.OrderStatusService;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Stateless
public class OrderStatusServiceBean implements OrderStatusService {

    private static final Logger logger = Logger.getLogger(OrderStatusServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @EJB
    PaymentGatewayBean paymentGatewayBean;
    
    public OrderStatusServiceBean() {
    }

    @PostConstruct
    public void init() {
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

}
