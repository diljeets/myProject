/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.entities.City;
import com.diljeet.myProject.entities.Locality;
import com.diljeet.myProject.interfaces.LocalityService;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Stateless
public class LocalityServiceBean implements LocalityService {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final Logger logger = Logger.getLogger(LocalityServiceBean.class.getCanonicalName());

//     private int customerId;
    @PersistenceContext(name = "my_persistence_unit")
    EntityManager em;

    public LocalityServiceBean() {
    }

    @Override
    public Response addLocality(Long cityId, Locality locality) {
        try {
            if (locality != null) {
                City city = em.find(City.class, cityId);
                locality.setDateLocalityCreated(new Date());
                city.addLocality(locality);
                em.persist(city);
            } else {
                logger.info("Could not find Locality");
                return null;
            }

        } catch (Exception e) {
            //e.printStackTrace();
            logger.log(Level.INFO, "Error saving Locality {0}", e.toString());
            return null;
        }
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @Override
    public List<Locality> getLocalitiesByCityId(Long cityId) {
        List<Locality> localities = null;
        City city = em.find(City.class, cityId);
        if (city != null) {
            try {
                Query query = em.createNamedQuery("getLocalitiesByCityId");
                query.setParameter("cityId", cityId);
                localities = query.getResultList();
            } catch (Exception e) {
                logger.log(Level.INFO, "Error retrieving localities {0}", e.toString());
            }
        } else {
            logger.info("City does not exist");
        }
        return localities;
    }

    @Override
    public void updateLocality(Locality updatedLocality) {
        try {
            Locality oldLocality = em.find(Locality.class, updatedLocality.getId());
            if (oldLocality != null) {
                oldLocality.setLocality(updatedLocality.getLocality());
                oldLocality.setIsActive(updatedLocality.isIsActive());
                oldLocality.setIsDefault(updatedLocality.isIsDefault());
                oldLocality.setDateLocalityUpdated(new Date());
            } else {
                logger.info("Could not find Locality");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    @Override
    public void deleteLocality(Long localityId) {
        try {
            Locality locality = em.find(Locality.class, localityId);
            if (locality != null) {
                em.remove(locality);
            } else {
                logger.info("Could not find Locality");
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Error deleting Locality {0}", e.toString());
        }
    }
}
