/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.entities.City;
import com.diljeet.myProject.interfaces.CityService;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author diljeet
 */
@Stateless
public class CityServiceBean implements CityService {

    private static final Logger logger = Logger.getLogger(CityServiceBean.class.getCanonicalName());
//    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUsersServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Override
    public Response addCity(City city) {
        if (city == null) {
            return null;
        } else {
            try {
                city.setDateCityCreated(new Date());
                em.persist(city);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @Override
    public List<City> getAllCities() {
        List<City> cities = null;
        try {
            cities = em.createNamedQuery("getAllCities").getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return cities;
    }

    @Override
    public List<City> getActiveCities() {
        List<City> cities = null;
        try {
            cities = em.createNamedQuery("getActiveCities").getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return cities;
    }

    @Override
    public void updateCity(City city) {
        try {
            City existingCity = em.find(City.class, city.getId());
            if (existingCity != null) {
                existingCity.setCity(city.getCity());
                existingCity.setState(city.getState());
                existingCity.setIsActive(city.isIsActive());
                existingCity.setIsDefault(city.isIsDefault());
                existingCity.setDateCityUpdated(new Date());
            } else {
                logger.severe("Could not find City");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating City {0}", e.toString());
        }
    }

    @Override
    public void deleteCity(Long id) {
        try {
            City existingCity = em.find(City.class, id);
            if (existingCity != null) {
                em.remove(existingCity);
            } else {
                logger.info("Could not find City");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting City {0}", e.toString());
        }
    }
}
