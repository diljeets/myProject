/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.entities.RegisteredUsers;
import com.diljeet.myProject.entities.RegisteredUsersAddress;
import com.diljeet.myProject.interfaces.RegisteredUsersAddressService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author diljeet
 */
@Stateless
@RolesAllowed("Administrator")
public class RegisteredUsersAddressServiceBean implements RegisteredUsersAddressService {

    private static final Logger logger = Logger.getLogger(RegisteredUsersAddressServiceBean.class.getCanonicalName());
//    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUsersServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Resource(lookup = "java:jboss/mail/gmailSession")
    public Session mailSession;

    @Inject
    HttpServletRequest req;

    @Override
    public void addAddress(RegisteredUsersAddress address) {
        try {
            if (address != null) {
                String username = req.getUserPrincipal().getName();
                Query query = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
                query.setParameter("username", username);
                Object accountExists = query.getSingleResult();
                RegisteredUsers registeredUser = (RegisteredUsers) accountExists;
                registeredUser.addAddress(address);
                em.persist(registeredUser);
            } else {
                logger.log(Level.SEVERE, "Could not find Address");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RegisteredUsersAddress> getAllRegisteredAddress() {
        List<RegisteredUsersAddress> addresses = null;
        try {
            addresses = em.createNamedQuery("getAllRegisteredAddress").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    @RolesAllowed({"Administrator","Customer"})
    public List<RegisteredUsersAddress> getAllRegisteredAddressByUsername() {
        List<RegisteredUsersAddress> addresses = null;
        String username = req.getUserPrincipal().getName();
        Query userQuery = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
        userQuery.setParameter("username", username);
        Object accountExists = userQuery.getSingleResult();
        RegisteredUsers registeredUser = (RegisteredUsers) accountExists;
        if (registeredUser != null) {
            try {
                Query addressQuery = em.createNamedQuery("getAllRegisteredAddressByUsername");
                addressQuery.setParameter("username", username);
                addresses = addressQuery.getResultList();
            } catch (Exception e) {
                logger.log(Level.INFO, "Error retrieving addresses {0}", e.toString());
            }
        } else {
            logger.info("User does not exist");
        }

        return addresses;
    }

    @Override
    public void updateAddressById(RegisteredUsersAddress updatedAddress) {
        Long addressId = updatedAddress.getId();
        try {
            Query query = em.createNamedQuery("getRegisteredAddressById");
            query.setParameter("addressId", addressId);
            Object registeredUserAddress = query.getSingleResult();
            if (registeredUserAddress != null) {
                em.merge(updatedAddress);
            } else {
                logger.log(Level.SEVERE, "No Address Found");
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Error updating address {0}", e.toString());
        }
    }

    @Override
    public void deleteRegisteredAddressById(int addressId) {
        logger.log(Level.SEVERE, "Address ID to delete is {0}", Integer.toString(addressId));
        try {
            Query query = em.createNamedQuery("getRegisteredAddressById");
            query.setParameter("addressId", addressId);
            Object registeredUserAddress = query.getSingleResult();
            if (registeredUserAddress != null) {
                em.remove(registeredUserAddress);
            } else {
                logger.log(Level.SEVERE, "No Address Found");
            }

        } catch (Exception e) {
            logger.log(Level.INFO, "Error deleting address {0}", e.toString());
        }
    }

}
