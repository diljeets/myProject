/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.RegisteredUsersBean;
import com.diljeet.myProject.entities.RegisteredUsers;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;

/**
 *
 * @author diljeet
 */
@Named(value = "registeredUsersController")
@SessionScoped
public class RegisteredUsersController implements Serializable {

    private static final Logger logger = Logger.getLogger(RegisteredUsersController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private RegisteredUsers user;

    private UIComponent createBtn;

    private UIComponent resetPasswordBtn;

    private UIComponent changePasswordBtn;

    @EJB
    private RegisteredUsersBean registeredUsersBean;

    @PostConstruct
    public void init() {
        user = new RegisteredUsers();
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public RegisteredUsersController() {
    }

    public RegisteredUsers getUser() {
        return user;
    }

    public void setUser(RegisteredUsers user) {
        this.user = user;
    }

    public UIComponent getCreateBtn() {
        return createBtn;
    }

    public void setCreateBtn(UIComponent createBtn) {
        this.createBtn = createBtn;
    }

    public UIComponent getResetPasswordBtn() {
        return resetPasswordBtn;
    }

    public void setResetPasswordBtn(UIComponent resetPasswordBtn) {
        this.resetPasswordBtn = resetPasswordBtn;
    }

    public UIComponent getChangePasswordBtn() {
        return changePasswordBtn;
    }

    public void setChangePasswordBtn(UIComponent changePasswordBtn) {
        this.changePasswordBtn = changePasswordBtn;
    }  

    public void clearFields() {
        user.setName(null);
        user.setMobile(null);
        user.setUsername(null);
        user.setPassword(null);
        user.setConfirmPassword(null);
        user.setRole(null);
        user.setSalt(null);
    }

    public void createUser(RegisteredUsers user) {
        registeredUsersBean.createUser(user);
        clearFields();
    }
    
    public void loginUser(RegisteredUsers user) {
        registeredUsersBean.loginUser(user);
        clearFields();
    }

    public void forgotPassword(String username) {
        registeredUsersBean.forgotPassword(username);
        clearFields();
    }

    public void changePassword(RegisteredUsers user) {
        registeredUsersBean.changePassword(user);
        clearFields();
    }
   

}
