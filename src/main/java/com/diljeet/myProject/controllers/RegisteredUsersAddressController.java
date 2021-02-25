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
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author diljeet
 */
@Named(value = "registeredUsersAddressController")
@ViewScoped
public class RegisteredUsersAddressController implements Serializable {

    private static final Logger logger = Logger.getLogger(RegisteredUsersAddressController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private RegisteredUsersAddress address;
    private List<RegisteredUsersAddress> addresses;    
    
    @Inject
    TemplateController templateController;

    @EJB
    RegisteredUsersAddressService registeredUsersAddressService;

    @PostConstruct
    public void init() {
        address = new RegisteredUsersAddress();
        if (!templateController.getCurrentCustomer().equals("Guest")){
            setAddresses(registeredUsersAddressService.getAllRegisteredAddressByUsername());
        } else {
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('credentials-sidebar').show()");
        }            
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
        return addresses;
    }

    public void setAddresses(List<RegisteredUsersAddress> addresses) {
        this.addresses = addresses;
    }    

    public void addAddress(RegisteredUsersAddress address) {
        registeredUsersAddressService.addAddress(address);
        setAddresses(registeredUsersAddressService.getAllRegisteredAddressByUsername());
        clear();
    }

    public void deleteRegisteredAddressById(int addressId) {
        registeredUsersAddressService.deleteRegisteredAddressById(addressId);
        setAddresses(registeredUsersAddressService.getAllRegisteredAddressByUsername());
    }

    public void updateRegisteredAddressById(RowEditEvent<RegisteredUsersAddress> event) {
        registeredUsersAddressService.updateAddressById(event.getObject());
        setAddresses(registeredUsersAddressService.getAllRegisteredAddressByUsername());
    }

    public void onRowCancel(RowEditEvent<RegisteredUsersAddress> event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", Long.toString(event.getObject().getId()));
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
