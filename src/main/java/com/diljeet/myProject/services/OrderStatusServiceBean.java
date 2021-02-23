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
import java.util.logging.Logger;
import com.diljeet.myProject.interfaces.OrderStatusService;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

//    @Inject
//    HttpServletRequest req;
//
//    @Inject
//    TemplateController templateController;
//    private String deliveryTime;
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

//    @Override
//    public Response placeOrder(CustomerOrder customerOrder) {
////        String orderId = customerOrder.getCustomerTransaction().getOrderId();
////        String payableAmount = customerOrder.getPayableAmount();
//
//        try {
////            paymentGatewayBean.initiateTransaction(orderId, payableAmount, username);
//            String paymentMode = customerOrder.getPaymentMode();
////            String customerName = templateController.getCurrentCustomer();
////            String username = req.getUserPrincipal().getName();
////            customerOrder.setCustomerName(customerName);
////            customerOrder.setUsername(username);
//            customerOrder.setDateOrderCreated(new Date());
////            customerOrder.setOrderId(orderId);
//            List<Cart> cartItems = customerOrder.getOrders();
//            for (Cart cartItem : cartItems) {
//                cartItem.setCustomerOrder(customerOrder);
//            }
//            em.persist(customerOrder);
//            if (paymentMode.equals("POD")) {
//                CustomerTransaction customerTransaction = new CustomerTransaction(
//                        customerOrder.getOrderId(),
//                        customerOrder.getPaymentMode(),
//                        "01",
//                        customerOrder.getPayableAmount()
//                );
//                paymentGatewayBean.setCustomerTransaction(customerTransaction);
//            }
//
//            return Response
//                    .status(Response.Status.CREATED)
//                    .build();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}
