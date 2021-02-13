/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.OrderStatusBean;
import com.diljeet.myProject.entities.CustomerTransaction;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;

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
        return customerTransaction;
    }

    public void setCustomerTransaction(CustomerTransaction customerTransaction) {
        this.customerTransaction = customerTransaction;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }

}
