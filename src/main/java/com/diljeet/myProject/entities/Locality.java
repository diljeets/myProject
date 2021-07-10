/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author diljeet
 */
@Entity
@Table(name = "locality_master")
@NamedQuery(
        name = "getLocalitiesByCityId",
        query = "SELECT DISTINCT l FROM Locality l, IN (l.city.localities) c where c.city.id = :cityId"
)
public class Locality implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locality;

    private boolean isActive;

    private boolean isDefault;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLocalityCreated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLocalityUpdated;

    @ManyToOne
    @JoinColumn(name = "CITY_ID")
    private City city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Date getDateLocalityCreated() {
        return dateLocalityCreated;
    }

    public void setDateLocalityCreated(Date dateLocalityCreated) {
        this.dateLocalityCreated = dateLocalityCreated;
    }

    public Date getDateLocalityUpdated() {
        return dateLocalityUpdated;
    }

    public void setDateLocalityUpdated(Date dateLocalityUpdated) {
        this.dateLocalityUpdated = dateLocalityUpdated;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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
        if (!(object instanceof Locality)) {
            return false;
        }
        Locality other = (Locality) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.myProject.entities.Locality[ id=" + id + " ]";
    }

}
