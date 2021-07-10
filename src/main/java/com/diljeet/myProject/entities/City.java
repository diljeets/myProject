/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author diljeet
 */
@Entity
@Table(name = "city_master")
@NamedQueries({
    @NamedQuery(
            name = "getAllCities",
            query = "Select DISTINCT c from City c order by c.id DESC"
    ),
    @NamedQuery(
            name = "getActiveCities",
            query = "Select DISTINCT c from City c where c.isActive = true order by c.id DESC"
    )
})
public class City implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String state;

    private boolean isActive;

    private boolean isDefault;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCityCreated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCityUpdated;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "city")
    @JsonbTransient
    private List<Locality> localities = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Date getDateCityCreated() {
        return dateCityCreated;
    }

    public void setDateCityCreated(Date dateCityCreated) {
        this.dateCityCreated = dateCityCreated;
    }

    public Date getDateCityUpdated() {
        return dateCityUpdated;
    }

    public void setDateCityUpdated(Date dateCityUpdated) {
        this.dateCityUpdated = dateCityUpdated;
    }

    public List<Locality> getLocalities() {
        return localities;
    }

    public void setLocalities(List<Locality> localities) {
        this.localities = localities;
    }

    public void addLocality(Locality locality) {
        localities.add(locality);
        locality.setCity(this);
    }

    public void removeLocality(Locality locality) {
        localities.remove(locality);
        locality.setCity(null);
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
        if (!(object instanceof City)) {
            return false;
        }
        City other = (City) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.myProject.entities.City[ id=" + id + " ]";
    }

}
