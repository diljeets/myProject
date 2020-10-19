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
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
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
public class RegisteredUsersBean {

    private static final Logger logger = Logger.getLogger(RegisteredUsersBean.class.getCanonicalName());

//    @PersistenceContext(name = "my-persistence-unit")
//    private EntityManager em;
//    
//    public void createUser(TestUsers user) {
//        if(user == null){
//            return;
//        }
////        logger.log(Level.SEVERE, user.getUsername());
////        logger.log(Level.SEVERE, user.getPassword());
////        logger.log(Level.SEVERE, user.getRole());
//        try {
//            if(user.getRole() == null)
//                user.setRole("user");
//            em.persist(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    public List<TestUsers> getUser() {
//        logger.log(Level.SEVERE, "Coming from service");
//        List<TestUsers> users = null;
//        try {
//            users = em.createNamedQuery("getAllUsers").getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return users;
//    }
    private Client client;

    private FacesMessage msg;

    @Inject
    RegisteredUsersController registeredUsersController;

//    @Inject
//    HttpServletRequest req;
//
//    @
//    HttpServletResponse res;    
    
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
//        boolean isValid = false;
        try {
//            try {
//                InternetAddress email = new InternetAddress(user.getUsername());
//                email.validate();
//                isValid = true;
//            } catch (AddressException e) {
//                throw new AddressException("Not valid");
//            }
//            if (isValid) {
                Response response = client.target("http://localhost:8080/myProject/webapi/RegisteredUsers")
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
//            } //else {
//                throw new AddressException("Not a valid Email Address");
//            }
        } catch (NewUserCreatedException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        } catch (ErrorCreatingUserException | PasswordsDontMatchException | SQLIntegrityConstraintViolationException e) {
//            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        } catch (Exception e) {
//            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        }

    }

    public List<RegisteredUsers> getUser() {
        List<RegisteredUsers> users = null;
        users = client.target("http://localhost:8080/myProject/webapi/RegisteredUsers")
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<RegisteredUsers>>() {
                });

        return users;
    }

    public void forgotPassword(String email) {     
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/RegisteredUsers/retrieve-password/")
                    .path(email)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/myProject/login.xhtml?sentPassword=true");
//                res.sendRedirect(req.getContextPath() + "/login.xhtml?sentPassword=true");
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
            Response response = client.target("http://localhost:8080/myProject/webapi/RegisteredUsers/")
                    .path(user.getEmail())                 
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(user, MediaType.APPLICATION_JSON),Response.class);
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
        } catch (UserAccountDoesNotExistException | PasswordsDontMatchException e) {
//            testUsersController.setIsProgressBar(false);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getChangePasswordBtn().getClientId(), msg);
        }

    }

}
