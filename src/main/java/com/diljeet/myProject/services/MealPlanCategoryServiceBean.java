/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.customexceptions.UserAccountDoesNotExistException;
import com.diljeet.myProject.entities.MealPlanCategory;
import com.diljeet.myProject.interfaces.RegisteredUsersService;
import com.diljeet.myProject.entities.RegisteredUsers;
import com.diljeet.myProject.interfaces.MealPlanCategoryService;
import com.diljeet.myProject.utils.MyProjectUtils;
import static com.diljeet.myProject.utils.MyProjectUtils.manipulateEncodedPassword;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author diljeet
 */
@Stateless
@RolesAllowed("Administrator")
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
    @RolesAllowed("Administrator")
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
