/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.entities.DeliveryHour;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.interfaces.DeliveryHourService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author diljeet
 */
@Named(value = "deliveryController")
@SessionScoped
public class DeliveryController implements Serializable {

    private static final Logger logger = Logger.getLogger(DeliveryController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private List<DeliveryHour> deliveryHours;
    private String deliveryTime;
    private RegisteredUsersAddress selectedAddress;
    private String deliveryAddress;
    private boolean isDeliveryAddressSelected;
    
    @EJB
    DeliveryHourService deliveryHourService;

    @PostConstruct
    public void init() {
        setDeliveryHours(deliveryHourService.getDeliveryHours());
        setDeliveryTime("12:00 PM");
        isDeliveryAddressSelected = true;
    }
   
    public DeliveryController() {
    }

    public List<DeliveryHour> getDeliveryHours() {
        return deliveryHours;
    }

    public void setDeliveryHours(List<DeliveryHour> deliveryHours) {
        this.deliveryHours = deliveryHours;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public void selectDeliveryTime(SelectEvent event) {
        deliveryTime = event.getObject().toString();        
    }
    
    public RegisteredUsersAddress getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(RegisteredUsersAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public boolean isIsDeliveryAddressSelected() {
        return isDeliveryAddressSelected;
    }

    public void setIsDeliveryAddressSelected(boolean isDeliveryAddressSelected) {
        this.isDeliveryAddressSelected = isDeliveryAddressSelected;
    }

    public void selectDeliveryAddress(SelectEvent<RegisteredUsersAddress> event) {
        isDeliveryAddressSelected = false;
        selectedAddress = event.getObject();
        deliveryAddress = selectedAddress.getHouseNo() + ", "
                + selectedAddress.getBuildingNo() + ", "
                + selectedAddress.getStreet() + ", "
                + selectedAddress.getCity() + ", "
                + selectedAddress.getState() + "-"
                + selectedAddress.getPincode();
    }
    
}
