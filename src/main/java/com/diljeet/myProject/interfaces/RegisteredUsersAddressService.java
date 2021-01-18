/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.interfaces;

import com.diljeet.myProject.entities.RegisteredUsersAddress;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author diljeet
 */
@Path("/RegisteredUsersAddress")
public interface RegisteredUsersAddressService {

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void addAddress(RegisteredUsersAddress address);

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<RegisteredUsersAddress> getAllRegisteredAddress();
    
    @GET
    @Path("getByUsername")
    @Produces({MediaType.APPLICATION_JSON})    
    public List<RegisteredUsersAddress> getAllRegisteredAddressByUsername();
    
    @PUT
    @Path("updateAddressById")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateAddressById(RegisteredUsersAddress updatedAddress);
    
    @DELETE
    @Path("deleteAddressById")
    public void deleteRegisteredAddressById(int addressId);
    
    

}
