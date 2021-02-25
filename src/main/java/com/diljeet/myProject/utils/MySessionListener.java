/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.utils;

import com.diljeet.myProject.controllers.RegisteredUsersAddressController;
import com.diljeet.myProject.interfaces.RegisteredUsersAddressService;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author diljeet
 */
@WebListener
public class MySessionListener implements HttpSessionListener {

    @Inject
    RegisteredUsersAddressController registeredUsersAddressController;
    
    @EJB
    RegisteredUsersAddressService registeredUsersAddressService;
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {        
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSessionListener.super.sessionDestroyed(se); //To change body of generated methods, choose Tools | Templates.
    }
    
}
