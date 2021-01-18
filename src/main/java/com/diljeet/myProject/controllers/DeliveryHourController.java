/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.entities.DeliveryHour;
import com.diljeet.myProject.interfaces.DeliveryHourService;
import com.diljeet.myProject.utils.DeliveryTimeService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author diljeet
 */
@Named(value = "deliveryHourController")
@SessionScoped
public class DeliveryHourController implements Serializable {

    private static final Logger logger = Logger.getLogger(DeliveryHourController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private DeliveryHour deliveryHour;
    private List<DeliveryHour> deliveryHours;
    private List<String> hoursData;
    private List<String> minutesData;
    private List<String> meridiemsData;
//    private String hours;
//    private String minutes;
//    private boolean isMeridiem;  

    @Inject
    DeliveryTimeService deliveryTimeService;

    @EJB
    DeliveryHourService deliveryHourService;

    @PostConstruct
    public void init() {
        deliveryHour = new DeliveryHour();
        hoursData = deliveryTimeService.getHours();
        minutesData = deliveryTimeService.getMinutes();
        meridiemsData = deliveryTimeService.getMeridiems();
        setDeliveryHours(deliveryHourService.getDeliveryHours());
    }

    public DeliveryHourController() {
    }

    public DeliveryHour getDeliveryHour() {
        return deliveryHour;
    }

    public void setDeliveryHour(DeliveryHour deliveryHour) {
        this.deliveryHour = deliveryHour;
    }

    public List<DeliveryHour> getDeliveryHours() {
        return deliveryHours;
    }

    public void setDeliveryHours(List<DeliveryHour> deliveryHours) {
        this.deliveryHours = deliveryHours;
    }

    public List<String> getHoursData() {
        return hoursData;
    }

    public List<String> getMinutesData() {
        return minutesData;
    }

    public List<String> getMeridiemsData() {
        return meridiemsData;
    }

//    public String getHours() {
//        return hours;
//    }
//
//    public void setHours(String hours) {
//        this.hours = hours;
//    }
//
//    public String getMinutes() {
//        return minutes;
//    }
//
//    public void setMinutes(String minutes) {
//        this.minutes = minutes;
//    }
//    public boolean isIsMeridiem() {
//        return isMeridiem;
//    }
//
//    public void setIsMeridiem(boolean isMeridiem) {
//        this.isMeridiem = isMeridiem;
//    }
    public void addDeliveryHour(DeliveryHour deliveryHour) {
//        String deliveryTime = hours + ":" + minutes;
//        String meridiem;
//        if (isMeridiem)
//            meridiem = "PM";
//        else 
//            meridiem = "AM";

//        deliveryHour.setDeliveryTime(deliveryTime);
//        deliveryHour.setMeridiem(meridiem);
        deliveryHourService.addDeliveryHour(deliveryHour);
        setDeliveryHours(deliveryHourService.getDeliveryHours());
        clear();
    }

    public void deleteDeliveryHourById(Long hourId) {
        deliveryHourService.deleteDeliveryHourById(hourId);
        setDeliveryHours(deliveryHourService.getDeliveryHours());
    }

    public void updateDeliveryHourById(RowEditEvent<DeliveryHour> event) {
        DeliveryHour updatedDeliveryHour = event.getObject();
//        String deliveryTime = hours + ":" + minutes;
//        updatedDeliveryHour.setDeliveryTime(deliveryTime);
        deliveryHourService.updateDeliveryHourById(updatedDeliveryHour);
        setDeliveryHours(deliveryHourService.getDeliveryHours());
        clear();
    }

    public void onRowCancel(RowEditEvent<DeliveryHour> event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", Long.toString(event.getObject().getId()));
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void clear() {
//        setHours(null);
//        setMinutes(null);
//        setIsMeridiem(false);
//        deliveryHour.setDeliveryTime(null);
        deliveryHour.setHours(null);
        deliveryHour.setMinutes(null);
        deliveryHour.setMeridiem(null);
        deliveryHour.setIsActive(false);

    }

}
