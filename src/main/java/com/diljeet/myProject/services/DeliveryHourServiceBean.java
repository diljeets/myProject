/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.entities.DeliveryHour;
import com.diljeet.myProject.interfaces.DeliveryHourService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author diljeet
 */
@Stateless
public class DeliveryHourServiceBean implements DeliveryHourService {

    private static final Logger logger = Logger.getLogger(DeliveryHourServiceBean.class.getCanonicalName());
//    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUsersServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Override
    public Response addDeliveryHour(DeliveryHour deliveryHour) {
         if (deliveryHour == null) {
            return null;
        } else {
            try {
                em.persist(deliveryHour);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public List<DeliveryHour> getDeliveryHours() {
        List<DeliveryHour> deliveryHours = null;
        try {
            Query query = em.createNamedQuery("getDeliveryHours");
            deliveryHours = query.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return deliveryHours;
    }

    @Override
    public void updateDeliveryHourById(DeliveryHour updatedDeliveryHour) {
        Long hourId = updatedDeliveryHour.getId();
        try {
            DeliveryHour deliveryHour = em.find(DeliveryHour.class, hourId);
            if (deliveryHour != null) {
                em.merge(updatedDeliveryHour);
            } else {
                logger.log(Level.SEVERE, "Delivery Hour not Found.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void deleteDeliveryHourById(Long hourId) {        
        try {
            DeliveryHour deliveryHour = em.find(DeliveryHour.class, hourId);
            if (deliveryHour != null) {
                em.remove(deliveryHour);
            } else {
                logger.log(Level.SEVERE, "Delivery Hour not Found.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
