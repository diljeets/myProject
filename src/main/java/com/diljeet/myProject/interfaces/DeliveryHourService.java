/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.DeliveryHour;
import com.diljeet.myProject.entities.MealPlanCategory;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Path("/DeliveryHour")
public interface DeliveryHourService {

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addDeliveryHour(DeliveryHour deliveryHour);

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<DeliveryHour> getDeliveryHours();
    
    @PUT
    @Path("updateDeliveryHour")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateDeliveryHourById(DeliveryHour updatedDeliveryHour);
    
    @DELETE
    @Path("deleteDeliveryHour")
    @Consumes({MediaType.APPLICATION_JSON})
    public void deleteDeliveryHourById(Long hourId);
    

}
