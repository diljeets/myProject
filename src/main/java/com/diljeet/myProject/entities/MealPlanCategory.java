/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
//import org.jboss.security.auth.spi.Util;

/**
 *
 * @author diljeet
 */
@Table(name = "mealPlanCategory")
@Entity
@NamedQuery(name = "getMealPlanCategories",
        query = "Select c from MealPlanCategory c")
public class MealPlanCategory implements Serializable {

    private static final Logger logger = Logger.getLogger(MealPlanCategory.class.getCanonicalName());

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotEmpty
    private String mealPlanName;
    
    private BigDecimal mealPlanRate;

    public MealPlanCategory() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMealPlanName() {
        return mealPlanName;
    }

    public void setMealPlanName(String mealPlanName) {
        this.mealPlanName = mealPlanName;
    }

    public BigDecimal getMealPlanRate() {
        return mealPlanRate;
    }

    public void setMealPlanRate(BigDecimal mealPlanRate) {
        this.mealPlanRate = mealPlanRate;
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MealPlanCategory)) {
            return false;
        }
        MealPlanCategory other = (MealPlanCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.test.entity.TestUsers[ id=" + id + " ]";
    }

}
