/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.entities.Cart;
import com.diljeet.myProject.interfaces.CartService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

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
    
    private Cart cartItem;

    private List<Cart> cartItems;

    @EJB
    CartService cartService;

    @Inject
    MealPlanCategoryController mealPlanCategoryController;

    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public CartController() {
        cartItem = new Cart();
    }

    public String getItemsInCart() {
        return cartService.itemsInCart();
    }

    public void setItemsInCart(String itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public Cart getCartItem() {
        return cartItem;
    }

    public void setCartItem(Cart cartItem) {
        this.cartItem = cartItem;
    }
    
    public List<Cart> getCartItems() {
        return cartService.getItemsFromCart();
    }

    public void setCartItems(List<Cart> cartItems) {
        this.cartItems = cartItems;
    }

    public void addToCart(Cart cartItem) {
        cartService.addToCart(cartItem);
        mealPlanCategoryController.setMealPlanQuantity(1);
    }

    public void addToCart(Long mealPlanId,
            String mealPlanName,
            Double mealPlanRate,
            int mealPlanQuantity) {
        addToCart(new Cart(mealPlanId, mealPlanName, mealPlanRate, mealPlanQuantity));
    }

    public void removeFromCart(Cart cartItem) {
        cartService.removeFromCart(cartItem);
    }

//    public void updateMealPlanQuantity(AjaxBehaviorEvent event, Cart cartItem) {        
//        int mealPlanQuantity = (Integer) ((UIOutput) event.getSource()).getValue();
//        logger.log(Level.SEVERE, "Cart Meal Quantity is {0}", Integer.toString(mealPlanQuantity));
//        logger.log(Level.SEVERE, "Cart Meal Name is {0}", cartItem.getMealPlanName());
//        logger.log(Level.SEVERE, "Cart Meal Rate is {0}", Double.toString(cartItem.getMealPlanRate()));
//        logger.log(Level.SEVERE, "Cart Meal Total Rate is {0}", Double.toString(cartItem.getTotalMealPlanRate()));
//    }
}
