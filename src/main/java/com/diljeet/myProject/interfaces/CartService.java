/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.Cart;
import java.util.List;
import javax.ejb.Local;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
    public Response addToCart(Cart cartItem); 
    
    @GET
    @Path("items-in-cart")
    @Produces({MediaType.TEXT_PLAIN})
    public String itemsInCart();
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cart> getItemsFromCart();
    
    @GET
    @Path("payable-amount")
    @Produces({MediaType.TEXT_PLAIN})
    public String getPayableAmount();

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeFromCart(Cart cartItem);
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeAllFromCart();
}
