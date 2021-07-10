/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.entities.Locality;
import com.diljeet.myProject.interfaces.LocalityService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author diljeet
 */
@Named(value = "localityController")
@SessionScoped
public class LocalityController implements Serializable {

    private static final Logger logger = Logger.getLogger(LocalityController.class.getCanonicalName());

    private static final long serialVersionUID = 2142383151318489373L;

    private Long cityId;
    private Locality locality;
    private List<Locality> localities;
    @EJB
    LocalityService localityService;

    @PostConstruct
    public void init() {
        locality = new Locality();
    }

    public LocalityController() {

    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public List<Locality> getLocalities() {
        return localities;
    }

    public void setLocalities(List<Locality> localities) {
        this.localities = localities;
    }

    public void getLocalitiesByCityId(Long cityId) {
        setLocalities(localityService.getLocalitiesByCityId(cityId));
    }

    public void addLocality(Long cityId, Locality locality) {
        localityService.addLocality(cityId, locality);
        setLocalities(localityService.getLocalitiesByCityId(cityId));
        clearFields();
    }

    public void deleteLocality(Long localityId) {
        localityService.deleteLocality(localityId);
        setLocalities(localityService.getLocalitiesByCityId(cityId));
    }
    
    private void clearFields() {
        locality.setLocality(null);
        locality.setIsActive(false);
        locality.setIsDefault(false);
    }

    public void onRowEdit(RowEditEvent<Locality> event) {
        localityService.updateLocality(event.getObject());
        setLocalities(localityService.getLocalitiesByCityId(cityId));
    }

    public void onRowCancel(RowEditEvent<Locality> event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", Long.toString(event.getObject().getId()));
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
