/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import org.jboss.security.auth.spi.Util;

/**
 *
 * @author diljeet
 */
@Table(name = "customerOrder")
@Entity
//@NamedQuery(name = "getAllUsers",
//        query = "Select u from RegisteredUsers u")
public class CustomerOrder implements Serializable {

    private static final Logger logger = Logger.getLogger(CustomerOrder.class.getCanonicalName());

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deliveryTime;
    
    private String deliveryAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customerOrder")
    @JsonbTransient
//    @XmlTransient
    private List<Cart> orders = new ArrayList<>();

    public CustomerOrder() {
    }

    public CustomerOrder(String deliveryTime, 
            String deliveryAddress,
            List<Cart> cartItems) {
        this.deliveryTime = deliveryTime;
        this.deliveryAddress = deliveryAddress;
        this.orders = cartItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<Cart> getOrders() {
        return orders;
    }

    public void setOrders(List<Cart> orders) {
        this.orders = orders;
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
        if (!(object instanceof CustomerOrder)) {
            return false;
        }
        CustomerOrder other = (CustomerOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.myProject.entities.CustomerOrder[ id=" + id + " ]";
    }

}
