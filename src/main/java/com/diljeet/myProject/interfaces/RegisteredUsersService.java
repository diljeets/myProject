/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.RegisteredUsers;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
    
    @POST
    @Path("login")    
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response loginUser(@HeaderParam("channel") String channel,
            RegisteredUsers user,
            @Context HttpServletRequest req,
            @Context HttpServletResponse res);
    
    @GET
    @Path("loginRedirect")
    public void loginRedirect(@Context HttpServletRequest req, @Context HttpServletResponse resp);

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<RegisteredUsers> getAllUsers();
    
    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON})    
    public Response getUser(@PathParam("username") String email);
    
//    @POST
//    @Path("{username}")
//    @Produces({MediaType.APPLICATION_JSON})
//    @Consumes({MediaType.APPLICATION_JSON})
//    public Response getUserByUsername(@PathParam("username") String email , RegisteredUsers user);
    
    @POST
    @Path("change-password")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response changePasswordByUsername(RegisteredUsers user);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/retrieve-password/{username}")
    public Response forgotPassword(@PathParam("username") String email);
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/change-password/{username}/{password}")
    public void changePassword(@PathParam("username") String encodedEmail, 
            @PathParam("password") String encodedPassword,
            @Context HttpServletRequest req,
            @Context HttpServletResponse res);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/activate-account/{username}")
    public void activateAccount(@PathParam("username") String encodedEmail,
            @Context HttpServletRequest req,
            @Context HttpServletResponse res);

}
