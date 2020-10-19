/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.RegisteredUsers;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Path("/RegisteredUsers")
public interface RegisteredUsersService {

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(RegisteredUsers user) throws NoSuchAlgorithmException;

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<RegisteredUsers> getAllUsers();
    
    @GET
    @Path("{email}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser(@PathParam("email") String email);
    
    @POST
    @Path("{email}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getUserByUsername(@PathParam("email") String email , RegisteredUsers user);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/retrieve-password/{email}")
    public Response forgotPassword(@PathParam("email") String email);
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/change-password/{email}/{password}")
    public void changePassword(@PathParam("email") String encodedEmail, 
            @PathParam("password") String encodedPassword,
            @Context HttpServletRequest req,
            @Context HttpServletResponse res);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/activate-account/{email}")
    public void activateAccount(@PathParam("email") String encodedEmail,
            @Context HttpServletRequest req,
            @Context HttpServletResponse res);

}
