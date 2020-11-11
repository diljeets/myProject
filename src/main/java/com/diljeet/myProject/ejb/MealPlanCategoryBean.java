/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.entities.MealPlanCategory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class MealPlanCategoryBean {

    private static final Logger logger = Logger.getLogger(MealPlanCategoryBean.class.getCanonicalName());

    private Client client;

    private FacesMessage msg;

    @Inject
    HttpServletRequest req; 
    
    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public void addMealPlan(MealPlanCategory mealPlan) {
        if (mealPlan == null) {
            return;
        }
        try {
            Response response = client.target("http://localhost:8080/myProject/webapi/MealPlanCategory")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(mealPlan, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                logger.log(Level.SEVERE, "Meal Plan Successfully added");
            } else {
                logger.log(Level.SEVERE, "Error adding Meal Plan");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(registeredUsersController.getCreateBtn().getClientId(), msg);
        }

    }

    public List<MealPlanCategory> getMealPlanCategories() {
        List<MealPlanCategory> mealPlans = null;
        mealPlans = client.target("http://localhost:8080/myProject/webapi/MealPlanCategory")
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .header("Cookie", req.getHeader("Cookie"))
                .get(new GenericType<List<MealPlanCategory>>() {
                });

        return mealPlans;
    }

}
