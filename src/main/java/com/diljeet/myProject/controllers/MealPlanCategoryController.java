/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.IndexBean;
import com.diljeet.myProject.ejb.MealPlanCategoryBean;
import com.diljeet.myProject.ejb.RegisteredUsersBean;
import com.diljeet.myProject.entities.MealPlanCategory;
import com.diljeet.myProject.entities.RegisteredUsers;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author diljeet
 */
@Named(value = "mealPlanCategoryController")
@SessionScoped
public class MealPlanCategoryController implements Serializable {

    private static final Logger logger = Logger.getLogger(MealPlanCategoryController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private MealPlanCategory mealPlan;
    
    private List<MealPlanCategory> mealPlans;

    @EJB
    MealPlanCategoryBean mealPlanCategoryBean;

//    @Inject
//    HttpServletRequest req;  
    @PostConstruct
    public void init() {
        mealPlan = new MealPlanCategory();
        setMealPlans(mealPlanCategoryBean.getMealPlanCategories());
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public MealPlanCategoryController() {
    }

//    public String logout() {
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//        return "/login.xhtml";
//    }
    public MealPlanCategory getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(MealPlanCategory mealPlan) {
        this.mealPlan = mealPlan;
    }

    public List<MealPlanCategory> getMealPlans() {
        return mealPlanCategoryBean.getMealPlanCategories();
    }

    public void setMealPlans(List<MealPlanCategory> mealPlans) {
        this.mealPlans = mealPlans;
    }    
    
    public void addMealPlan(MealPlanCategory mealPlan){
        mealPlanCategoryBean.addMealPlan(mealPlan);
        cleatFields();
    }    
    
    public void cleatFields(){
        mealPlan.setMealPlanName(null);
        mealPlan.setMealPlanRate(null);
    }

}
