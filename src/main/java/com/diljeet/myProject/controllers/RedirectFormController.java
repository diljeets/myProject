/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.CheckoutBean;
import com.diljeet.myProject.ejb.PaymentGatewayBean;
import com.diljeet.myProject.ejb.RegisteredUsersBean;
import com.diljeet.myProject.entities.MealPlanCategory;
import com.diljeet.myProject.utils.RedirectForm;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author diljeet
 */
@Named(value = "redirectFormController")
@RequestScoped
public class RedirectFormController implements Serializable {

    private static final Logger logger = Logger.getLogger(RedirectFormController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private RedirectForm redirectForm;   
    
    @EJB
    CheckoutBean checkoutBean;
    
    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public RedirectFormController() {
//        redirectForm = new RedirectForm();
    }   

    public RedirectForm getRedirectForm() {
        return checkoutBean.getRedirectForm();
    }

    public void setRedirectForm(RedirectForm redirectForm) {
        this.redirectForm = redirectForm;
    }
    

}
