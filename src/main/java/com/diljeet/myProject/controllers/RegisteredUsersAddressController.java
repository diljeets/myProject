/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.interfaces.RegisteredUsersAddressService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author diljeet
 */
@Named(value = "registeredUsersAddressController")
@SessionScoped
public class RegisteredUsersAddressController implements Serializable {

    private static final Logger logger = Logger.getLogger(RegisteredUsersAddressController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private RegisteredUsersAddress address;

    private List<RegisteredUsersAddress> addresses;    
    
    @EJB
    RegisteredUsersAddressService registeredUsersAddressService;    
    
    @PostConstruct
    public void init() {
        address = new RegisteredUsersAddress();
    }
    
    public RegisteredUsersAddressController() {
    }

    public RegisteredUsersAddress getAddress() {
        return address;
    }

    public void setAddress(RegisteredUsersAddress address) {
        this.address = address;
    }

    public List<RegisteredUsersAddress> getAddresses() {
        return registeredUsersAddressService.getAllRegisteredAddressByUsername();
//        return registeredUsersAddressService.getAllRegisteredAddress();
    }

    public void setAddresses(List<RegisteredUsersAddress> addresses) {
        this.addresses = addresses;
    }
    
    public void addAddress(RegisteredUsersAddress address){
//        logger.log(Level.SEVERE, address.getHouseNo());
//        logger.log(Level.SEVERE, address.getCity());
        registeredUsersAddressService.addAddress(address);
        clear();
    }
    
     public void onRowSelect(SelectEvent<RegisteredUsersAddress> event) {
         setAddress(event.getObject());
//        FacesMessage msg = new FacesMessage("Car Selected", event.getObject().getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    private void clear() {
        address.setHouseNo(null);
        address.setBuildingNo(null);
        address.setStreet(null);
        address.setCity(null);
        address.setState(null);
        address.setPincode(null);
    }
   
}
