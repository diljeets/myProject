/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.entities;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author diljeet
 */
@Table(name = "cart")
@Entity
//@NamedQuery(name = "getMealPlanCategories",
//        query = "Select c from Cart c")
@XmlRootElement
public class Cart implements Serializable {

    private static final Logger logger = Logger.getLogger(Cart.class.getCanonicalName());

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long mealPlanId;

    private String mealPlanName;

    private Double mealPlanRate;
    
    private Double totalMealPlanRate;

    private int mealPlanQuantity;
    
    @ManyToOne
    @JoinColumn(name = "CUSTOMERORDER_ID")
    private CustomerOrder customerOrder;

    @PostConstruct
    public void init() {
    }

    public Cart() {        
    }

    public Cart(Long mealPlanId,
            String mealPlanName,
            Double mealPlanRate,
            int mealPlanQuantity) {
        this.mealPlanId = mealPlanId;
        this.mealPlanName = mealPlanName;
        this.mealPlanRate = mealPlanRate;
        this.mealPlanQuantity = mealPlanQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(Long mealPlanId) {
        this.mealPlanId = mealPlanId;
    }
    
    public String getMealPlanName() {
        return mealPlanName;
    }

    public void setMealPlanName(String mealPlanName) {
        this.mealPlanName = mealPlanName;
    }

    public Double getMealPlanRate() {
        return mealPlanRate;
    }

    public void setMealPlanRate(Double mealPlanRate) {
        this.mealPlanRate = mealPlanRate;
    }

    public Double getTotalMealPlanRate() {
        return totalMealPlanRate;
    }

    public void setTotalMealPlanRate(Double totalMealPlanRate) {
        this.totalMealPlanRate = totalMealPlanRate;
    }
    
    public int getMealPlanQuantity() {
        return mealPlanQuantity;
    }

    public void setMealPlanQuantity(int mealPlanQuantity) {
        this.mealPlanQuantity = mealPlanQuantity;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Cart)) {
//            return false;
//        }
//        Cart other = (Cart) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }    
    
    @Override
    public String toString() {
        return "com.diljeet.myProject.entities.Cart[ id=" + id + " ]";
    }

}
