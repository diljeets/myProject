/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.RegisteredUsersBean;
import com.diljeet.myProject.entities.MealPlanCategory;
import java.io.Serializable;
import java.util.List;
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
@Named(value = "indexController")
@SessionScoped
public class IndexController implements Serializable {

    private static final Logger logger = Logger.getLogger(IndexController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private String currentCustomer;

    private List<MealPlanCategory> mealPlans;

    @EJB
    RegisteredUsersBean registeredUsersBean;

    @Inject
    MealPlanCategoryController mealPlanCategoryController;

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
        return registeredUsersBean.getUser(req.getUserPrincipal().getName());
    }

    public void setCurrentCustomer(String currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public List<MealPlanCategory> getMealPlans() {
        return mealPlanCategoryController.getMealPlans();
    }

    public void setMealPlans(List<MealPlanCategory> mealPlans) {
        this.mealPlans = mealPlans;
    }

    public MealPlanCategoryController getMealPlanCategoryController() {
        return mealPlanCategoryController;
    }

    public void setMealPlanCategoryController(MealPlanCategoryController mealPlanCategoryController) {
        this.mealPlanCategoryController = mealPlanCategoryController;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }

}
