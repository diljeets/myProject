/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.entities.Cart;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ws.rs.core.Response;
import com.diljeet.myProject.interfaces.CartService;
import com.diljeet.myProject.utils.MyProjectUtils;
import java.util.Iterator;
import java.util.Objects;
import javax.annotation.PostConstruct;

/**
 *
 * @author diljeet
 */
@Stateful
public class CartServiceBean implements CartService {

    private static final Logger logger = Logger.getLogger(CartServiceBean.class.getCanonicalName());
    
    ArrayList<Cart> cartItems = null;
    
    private Double payableAmount;

    public CartServiceBean() {
        cartItems = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
    }

    @Override
    public Response addToCart(Cart cartItem) {
        try {
            Cart updatedCartItem = MyProjectUtils.calculateTotalMealPlanRate(cartItem);            
            
            if (cartItems.isEmpty()) {
                cartItems.add(updatedCartItem);
            } else {
                boolean isItemInCart = false;
                int itemIndex = 0;
                Iterator<Cart> itr = cartItems.iterator();                
                while(itr.hasNext()) {
                    Cart item = itr.next();
                    if (Objects.equals(item.getMealPlanId(), updatedCartItem.getMealPlanId())) {
                        isItemInCart = true;
                        itemIndex = cartItems.indexOf(item);
                    } 
                }
                if (isItemInCart) {
                    cartItems.remove(itemIndex);
                    cartItems.add(itemIndex, updatedCartItem);
                } else {
                    cartItems.add(updatedCartItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @Override
    public String itemsInCart() {
        return Integer.toString(cartItems.size());
    }

    @Override
    public List<Cart> getItemsFromCart() {
        return cartItems;
    }

    @Override
    public String getPayableAmount() {
        payableAmount = MyProjectUtils.calculatePayableAmount(cartItems);
        return Double.toString(payableAmount);
    }

    @Override
    public void removeFromCart(Cart cartItem) {
        cartItems.remove(cartItem);
    }

    @Override
    public void removeAllFromCart() {
        cartItems.clear();
    }

}
