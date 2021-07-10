/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.entities.City;
import com.diljeet.myProject.interfaces.CityService;
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
@Named(value = "cityController")
@SessionScoped
public class CityController implements Serializable {

    private static final Logger logger = Logger.getLogger(CityController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private City city;
    private List<City> cities;

    @EJB
    CityService cityService;

    @PostConstruct
    public void init() {
        city = new City();
        setCities(cityService.getAllCities());
    }

    public CityController() {
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void addCity(City city) {
        cityService.addCity(city);
        setCities(cityService.getAllCities());
        clearFields();
    }

    public void deleteCity(Long cityId) {
        cityService.deleteCity(cityId);
        setCities(cityService.getAllCities());
    }

    public void onRowEdit(RowEditEvent<City> event) {
        cityService.updateCity(event.getObject());
        setCities(cityService.getAllCities());
    }

    public void onRowCancel(RowEditEvent<City> event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", Long.toString(event.getObject().getId()));
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void clearFields() {
        city.setCity(null);
        city.setState(null);
        city.setIsActive(false);
        city.setIsDefault(false);
    }

}
