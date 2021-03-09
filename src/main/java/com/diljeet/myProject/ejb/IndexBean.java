/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.customexceptions.ChangePasswordRequestAcceptedException;
import com.diljeet.myProject.customexceptions.ErrorCreatingUserException;
import com.diljeet.myProject.entities.RegisteredUsers;
import com.diljeet.myProject.customexceptions.NewUserCreatedException;
import com.diljeet.myProject.customexceptions.PasswordsDontMatchException;
import com.diljeet.myProject.customexceptions.UserAccountDoesNotExistException;
import com.diljeet.myProject.controllers.RegisteredUsersController;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class IndexBean {
    
    private static final Logger logger = Logger.getLogger(IndexBean.class.getCanonicalName());
    
    private Client client;
//
//    private FacesMessage msg;
//
//    @Inject
//    RegisteredUsersController registeredUsersController;

    @Inject
    HttpServletRequest req;
    
    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }
    
    @PreDestroy
    public void destroy() {
        client.close();
    }
    
//    public String getUser(String email) {
//        String customerName = null;
//        RegisteredUsers currentCustomer = null;
//        try {
//            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers")
//                    .path(email)
//                    .request(MediaType.APPLICATION_JSON)
//                    .header("Cookie", req.getHeader("Cookie"))
//                    .get();
//            if (response.getStatus() == Response.Status.FOUND.getStatusCode()) {
//                currentCustomer = response.readEntity(RegisteredUsers.class);
//                customerName = currentCustomer.getName();                
//            } else {
//                throw new UserAccountDoesNotExistException("User does not exist.");
//            }
//        } catch (UserAccountDoesNotExistException e) {
//            logger.log(Level.SEVERE, e.getMessage());
//        } catch (Exception e) {            
//            logger.log(Level.SEVERE, e.getMessage());
//        }
//        
//        return customerName;
//    }
    
}
