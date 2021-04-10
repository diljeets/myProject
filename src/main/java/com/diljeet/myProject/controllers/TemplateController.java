/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.RegisteredUsersBean;
import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
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
    private String tabIndex;
    private boolean hasQueryString;    
    private boolean account;
//    private String originalURL;

    @Inject
    HttpServletRequest req;
    
    @EJB
    RegisteredUsersBean registeredUsersBean;
    
    @PostConstruct
    public void init() {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

//        originalURL = request.getRequestURL().toString();
//
//        if (originalURL == null) {
//            originalURL = externalContext.getRequestContextPath() + "/index.xhtml";
        //} else {
//            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);
//
//            if (originalQuery != null) {
//                originalURL += "?" + originalQuery;
//            }
//        }
//        logger.log(Level.SEVERE, "Original URL is {0}", originalURL);
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
    
    public String getTabIndex() {
        return tabIndex;
    }
    
    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }
    
//    public boolean getHasQueryString() {
//        
//        hasQueryString = !FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().isEmpty();
//        
//        logger.log(Level.SEVERE, "has param is {0}", Boolean.toString(hasQueryString));
//        
//        return hasQueryString;
//    }
//    
//    public void setHasQueryString(boolean hasQueryString) {
//        this.hasQueryString = hasQueryString;
//    }

    public boolean isHasQueryString() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        hasQueryString = !externalContext.getRequestParameterMap().isEmpty();        
        if (hasQueryString) {
            String tab = externalContext.getRequestParameterMap().get("tab");
            setTabIndex(tab);
        }
        return hasQueryString;
    }

    public void setHasQueryString(boolean hasQueryString) {
        this.hasQueryString = hasQueryString;
    }
    
    public boolean getAccount() {
        return account;
    }
    
    public void setAccount(boolean account) {
        this.account = account;
    }

//    public String getOriginalURL() {
//        return originalURL;
//    }
//
//    public void setOriginalURL(String originalURL) {
//        this.originalURL = originalURL;
//    }
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }
    
}
