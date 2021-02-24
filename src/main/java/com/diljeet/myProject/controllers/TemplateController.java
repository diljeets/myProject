/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.RegisteredUsersBean;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author diljeet
 */
@Named(value = "templateController")
@SessionScoped
public class TemplateController implements Serializable {

    private static final Logger logger = Logger.getLogger(TemplateController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private String currentCustomer;
    
    @Inject
    HttpServletRequest req;

    @EJB
    RegisteredUsersBean registeredUsersBean;    

    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public TemplateController() {
    }

    public String getCurrentCustomer() {
        String user = null;
        try {
            user = req.getUserPrincipal().getName();            
        } catch (NullPointerException e) {
            return "Guest";
        } catch (Exception e) {
            e.printStackTrace();
        }       

        return registeredUsersBean.getUser(user);    
    }

    public void setCurrentCustomer(String currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }

}
