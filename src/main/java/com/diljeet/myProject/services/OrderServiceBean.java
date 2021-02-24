/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.controllers.OrderController;
import com.diljeet.myProject.ejb.PaymentGatewayBean;
import com.diljeet.myProject.entities.Cart;
import com.diljeet.myProject.entities.CustomerOrder;
import com.diljeet.myProject.entities.CustomerTransaction;
import com.diljeet.myProject.interfaces.OrderService;
import java.util.logging.Logger;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author diljeet
 */
@Stateless
public class OrderServiceBean implements OrderService {

    private static final Logger logger = Logger.getLogger(OrderServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Inject
    OrderController orderController;

    @EJB
    PaymentGatewayBean paymentGatewayBean;

    public OrderServiceBean() {
    }

    @PostConstruct
    public void init() {
    }

    @Override
    public Response pgPostResponse(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> mapData = req.getParameterMap();
        JSONObject parameters = new JSONObject();
        mapData.forEach((key, val) -> parameters.put(key, val[0]));
        logger.log(Level.SEVERE, "Final payment response is {0}", parameters.toString());

        //Get Customer Transaction Status
        CustomerTransaction customerTransaction = paymentGatewayBean.getCustomerTransactionStatus(parameters.toString());

        //Create and Place Order if transaction is successful if paymentMode is either CREDIT_CARD/ DEBIT_CARD/ NET_BANKING
        String respCode = customerTransaction.getRespCode();
        if (respCode.equals("01")) {
            return Response
                    .seeOther(URI.create("/Order/createAndPlaceCustomerOrder"))
                    .build();
        }

        //Do not Create and Place Order but redirect Customer to Transaction Status page showing unsuccessful Transaction message if paymentMode is either                  CREDIT_CARD/ DEBIT_CARD/ NET_BANKING
        return Response
                .seeOther(URI.create("/Order/pgResponse"))
                .build();
    }

    @Override
    public void pgGetResponse(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect(req.getContextPath() + "/order-status.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response createAndPlaceCustomerOrder() {
        //Create and Place Order
        orderController.createAndPlaceCustomerOrder();
        //Redirect to show Order Status to Customer
        return Response
                .seeOther(URI.create("/Order/pgResponse"))
                .build();
    }

    @Override
    public Response placeOrder(CustomerOrder customerOrder) {
        try {
            String paymentMode = customerOrder.getPaymentMode();
            customerOrder.setDateOrderCreated(new Date());
            List<Cart> cartItems = customerOrder.getOrders();
            for (Cart cartItem : cartItems) {
                cartItem.setCustomerOrder(customerOrder);
            }
            em.persist(customerOrder);
            if (paymentMode.equals("POD")) {
                CustomerTransaction customerTransaction = new CustomerTransaction(
                        customerOrder.getOrderId(),
                        customerOrder.getPaymentMode(),
                        "01",
                        customerOrder.getPayableAmount()
                );
                paymentGatewayBean.setCustomerTransaction(customerTransaction);
            }

            return Response
                    .status(Response.Status.CREATED)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
