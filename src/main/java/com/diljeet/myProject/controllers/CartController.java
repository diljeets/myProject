/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.ejb.IndexBean;
import com.diljeet.myProject.ejb.RegisteredUsersBean;
import com.diljeet.myProject.entities.MealPlanCategory;
import com.diljeet.myProject.entities.RegisteredUsers;
import com.diljeet.myProject.interfaces.CartService;
import com.diljeet.myProject.services.CartServiceBean;
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
@Named(value = "cartController")
@SessionScoped
public class CartController implements Serializable {

    private static final Logger logger = Logger.getLogger(CartController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private String itemsInCart;    

    private List<MealPlanCategory> mealPlans;
    
    @EJB
    CartService cartService;

    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public CartController() {
    }

    public String getItemsInCart() {
        return cartService.itemsInCart();
    }

    public void setItemsInCart(String itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public List<MealPlanCategory> getMealPlans() {
        return cartService.getItemsFromCart();
    }

    public void setMealPlans(List<MealPlanCategory> mealPlans) {
        this.mealPlans = mealPlans;
    }

    public void addToCart(MealPlanCategory mealPlan){
        cartService.addToCart(mealPlan);
    }
   
    public void removeFromCart(MealPlanCategory mealPlan){
        cartService.removeFromCart(mealPlan);
    }

}
