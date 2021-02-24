/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.CustomerOrder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
//@Remote
@Path("/Order")
public interface OrderService {

    @POST
    @Path("pgResponse")
    public Response pgPostResponse(@Context HttpServletRequest req, @Context HttpServletResponse resp);

    @GET
    @Path("pgResponse")
    public void pgGetResponse(@Context HttpServletRequest req, @Context HttpServletResponse resp);

    @GET
    @Path("createAndPlaceCustomerOrder")
    public Response createAndPlaceCustomerOrder();

    @POST
    @Path("placeOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(CustomerOrder customerOrder);

}
