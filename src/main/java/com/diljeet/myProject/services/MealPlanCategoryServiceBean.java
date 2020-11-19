/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.entities.MealPlanCategory;
import com.diljeet.myProject.interfaces.MealPlanCategoryService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
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
public class MealPlanCategoryServiceBean implements MealPlanCategoryService {

    private static final Logger logger = Logger.getLogger(MealPlanCategoryServiceBean.class.getCanonicalName());
//    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUsersServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Override    
    public Response addMealPlan(MealPlanCategory mealPlan) {        
        if (mealPlan == null) {
            return null;
        } else {
            try {
                em.persist(mealPlan);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @Override        
    public List<MealPlanCategory> getMealPlanCategories() {
        List<MealPlanCategory> mealPlans = null;
        try {
            mealPlans = em.createNamedQuery("getMealPlanCategories").getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return mealPlans;
    }

}
