/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.Locality;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
@Path("/Locality")
public interface LocalityService {

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addLocality(@HeaderParam("cityId") Long cityId, Locality locality);

    @GET
    @Path("/get/{cityId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Locality> getLocalitiesByCityId(@PathParam("cityId") Long cityId);

    @PUT
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateLocality(Locality updatedLocality);

    @DELETE
    @Path("delete/{localityId}")
    @Produces({MediaType.APPLICATION_JSON})
    public void deleteLocality(@PathParam("localityId") Long localityId);
}
