/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.MealPlanCategoryBean;
import com.diljeet.myProject.entities.MealPlanCategory;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

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

    private int mealPlanQuantity;

    @EJB
    MealPlanCategoryBean mealPlanCategoryBean;

    @PostConstruct
    public void init() {
        mealPlan = new MealPlanCategory();
//        setMealPlans(mealPlanCategoryBean.getMealPlanCategories());
        setMealPlanQuantity(1);
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

    public int getMealPlanQuantity() {
        return mealPlanQuantity;
    }

    public void setMealPlanQuantity(int mealPlanQuantity) {
        this.mealPlanQuantity = mealPlanQuantity;
    }    

    public void addMealPlan(MealPlanCategory mealPlan) {
        mealPlanCategoryBean.addMealPlan(mealPlan);
        clearFields();
    }

    public void clearFields() {
        mealPlan.setMealPlanName(null);
        mealPlan.setMealPlanRate(null);
    }

}
