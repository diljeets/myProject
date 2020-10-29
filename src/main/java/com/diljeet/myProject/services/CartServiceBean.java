/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.entities.MealPlanCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ws.rs.core.Response;
import com.diljeet.myProject.interfaces.CartService;
import javax.annotation.PostConstruct;

/**
 *
 * @author diljeet
 */
@Stateful
public class CartServiceBean implements CartService {

    private static final Logger logger = Logger.getLogger(CartServiceBean.class.getCanonicalName());
    List<MealPlanCategory> meals = null;

    public CartServiceBean() {
        meals = new ArrayList<>();
    }

//    @PostConstruct
//    public void init() {
//        if (meals.isEmpty()) {
//            meals = new ArrayList<>();
//        }   
//    }
    @Override
    public Response addToCart(MealPlanCategory mealPlan) {
        try {
            meals.add(mealPlan);            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

//    @Override
//    public void removeFromCart(String mealPlanName) {
//        meals.remove(mealPlanName);
//    }
//
    @Override
    public String itemsInCart() {
        return Integer.toString(meals.size());
    }
    
    @Override
    public List<MealPlanCategory> getItemsFromCart() {       
        return meals;
    }

    @Override
    public void removeFromCart(MealPlanCategory mealPlan) {
        meals.remove(mealPlan);
    }
    
    
   
}
