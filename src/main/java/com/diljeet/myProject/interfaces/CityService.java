/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.City;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Path("/City")
public interface CityService {

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addCity(City city);

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<City> getAllCities();

    @GET
    @Path("/active/all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<City> getActiveCities();

    @PUT
    @Path("update")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateCity(City city);

    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public void deleteCity(@PathParam("id") Long id);
}
