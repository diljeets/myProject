/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.IndexBean;
import com.diljeet.myProject.ejb.RegisteredUsersBean;
import com.diljeet.myProject.entities.RegisteredUsers;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author diljeet
 */
@Named(value = "indexController")
@SessionScoped
public class IndexController implements Serializable {

    private static final Logger logger = Logger.getLogger(IndexController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private String currentCustomer;

    @EJB
    IndexBean indexBean;
    
    @Inject
    HttpServletRequest req;

    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public IndexController() {
    }

    public String getCurrentCustomer() {
        return indexBean.getUser(req.getUserPrincipal().getName());
    }

    public void setCurrentCustomer(String currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
    
     public String logout(){        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }

}
