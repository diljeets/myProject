/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.MealPlanCategory;
import java.util.List;
import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
//@Remote
@Path("/Cart")
public interface CartService {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addToCart(MealPlanCategory mealPlan);    

    @GET
    @Path("items-in-cart")
    @Produces({MediaType.TEXT_PLAIN})
    public String itemsInCart();
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MealPlanCategory> getItemsFromCart();

    public void removeFromCart(MealPlanCategory mealPlan);
}
