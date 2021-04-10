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
import com.diljeet.myProject.controllers.TemplateController;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class RegisteredUsersBean {

    private static final Logger logger = Logger.getLogger(RegisteredUsersBean.class.getCanonicalName());

    private Client client;

    private FacesMessage msg;

    @Inject
    RegisteredUsersController registeredUsersController;

    @Inject
    TemplateController templateController;

    @Inject
    HttpServletRequest req;

    @Context
    HttpServletResponse res;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public void createUser(RegisteredUsers user) {
        if (user == null) {
            return;
        }
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                throw new NewUserCreatedException("User created successfully. Activation link has been sent to registerd email id. Kindly click on the link to activate your account.");

            } else if (response.getStatus() == Response.Status.PRECONDITION_FAILED.getStatusCode()) {
                throw new PasswordsDontMatchException("Error creating User. Password and Retype Password don't match.");
            } else if (response.getStatus() == Response.Status.FOUND.getStatusCode()) {
                throw new SQLIntegrityConstraintViolationException("Error creating User. User already exists.");
            } else {
                throw new ErrorCreatingUserException("Error creating User");
            }
        } catch (NewUserCreatedException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        } catch (ErrorCreatingUserException | PasswordsDontMatchException | SQLIntegrityConstraintViolationException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        }

    }

    public void loginUser(RegisteredUsers user) {
        if (user == null) {
            return;
        }
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers/login")
                    .request(MediaType.APPLICATION_JSON)
                    .header("channel", "WEB")
                    .post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {
                FacesContext context = FacesContext.getCurrentInstance();
                ExternalContext externalContext = context.getExternalContext();
                HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
                if (response.hasEntity()) {
                    request.login(user.getUsername(), user.getPassword());
                    RegisteredUsers existingUser = response.readEntity(RegisteredUsers.class);                    
                    externalContext.getSessionMap().put("user", existingUser);
                    externalContext.redirect("http://192.168.43.80:8080/myProject/index.xhtml");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RegisteredUsers> getAllUsers() {
        List<RegisteredUsers> users = null;
        users = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers")
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<RegisteredUsers>>() {
                });

        return users;
    }

    public String getUser(String username) {
//        logger.log(Level.SEVERE, "Username is {0}", username);
        String customerName = null;
        RegisteredUsers currentCustomer = null;
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers")
                    .path(username)
                    .request(MediaType.APPLICATION_JSON)
                    //                    .header("Cookie", req.getHeader("Cookie"))
                    .get();
            if (response.getStatus() == Response.Status.FOUND.getStatusCode()) {
                currentCustomer = response.readEntity(RegisteredUsers.class);
                customerName = currentCustomer.getName();
            } else {
                throw new UserAccountDoesNotExistException("User does not exist.");
            }
        } catch (UserAccountDoesNotExistException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return customerName;
    }

    public void forgotPassword(String username) {
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers/retrieve-password/")
                    .path(username)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/myProject/login.xhtml?sentPassword=true");
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()
                    || response.getStatus() == Response.Status.NOT_MODIFIED.getStatusCode()) {
                throw new UserAccountDoesNotExistException("Account/User either is inactive or does not exist.");
            }
        } catch (UserAccountDoesNotExistException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getResetPasswordBtn().getClientId(), msg);
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getResetPasswordBtn().getClientId(), msg);
        }

    }

    public void changePassword(RegisteredUsers user) {
        try {
            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers/change-password")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.PRECONDITION_FAILED.getStatusCode()) {
                throw new PasswordsDontMatchException("Password and Retype Password don't match.");
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new UserAccountDoesNotExistException("Account/User either is inactive or does not exist.");
            } else if (response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {
                throw new ChangePasswordRequestAcceptedException("For Security reasons we have sent you a mail asking for your consent to go ahead with Password change request. Kindly click on the link to proceed.");
            }
        } catch (ChangePasswordRequestAcceptedException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
            templateController.logout();
        } catch (UserAccountDoesNotExistException | PasswordsDontMatchException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
        }

    }

//    public void changePassword(RegisteredUsers user) {   
//        try {
//            Response response = client.target("http://192.168.43.80:8080/myProject/webapi/RegisteredUsers/")
//                    .path(user.getUsername())                 
//                    .request(MediaType.APPLICATION_JSON)
//                    .post(Entity.entity(user, MediaType.APPLICATION_JSON),Response.class);
//            if (response.getStatus() == Response.Status.PRECONDITION_FAILED.getStatusCode()) {
//                throw new PasswordsDontMatchException("Password and Retype Password don't match.");
//            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
//                throw new UserAccountDoesNotExistException("Account/User either is inactive or does not exist.");
//            } else if (response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {
//                throw new ChangePasswordRequestAcceptedException("For Security reasons we have sent you a mail asking for your consent to go ahead with Password change request. Kindly click on the link to proceed.");
//            }
//        } catch (ChangePasswordRequestAcceptedException e) {
//            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
//            templateController.logout();
//        } catch (UserAccountDoesNotExistException | PasswordsDontMatchException e) {
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
//        } catch (Exception e) {
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
//        }
//
//    }
}
