/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author diljeet
 */
@Entity
@Table(name = "registeredUsersAddress")
@NamedQuery(name = "getAllRegisteredAddress",
        query = "Select a From RegisteredUsersAddress AS a Order By a.id Desc"
)
@NamedQuery(
        name = "getAllRegisteredAddressByUsername",
        query = "SELECT NEW com.diljeet.myProject.entities.RegisteredUsersAddress(a.id, a.houseNo, a.buildingNo, a.street, a.city, a.state, a.pincode) FROM RegisteredUsersAddress a WHERE a.registeredUser.username = :username"
//        query = "SELECT DISTINCT a FROM RegisteredUsersAddress a, IN (a.registeredUser.addresses) u where u.registeredUser.username = :username"
)
@NamedQuery(name = "getRegisteredAddressById",
        query = "Select a From RegisteredUsersAddress a where a.id = :addressId"
)
public class RegisteredUsersAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_no")
    private String houseNo;

    @Column(name = "building_no")
    private String buildingNo;

    private String street;

    private String city;

    private String state;

    private String pincode;

    @ManyToOne
    @JoinColumn(name = "REGISTEREDUSER_ID")
    private RegisteredUsers registeredUser;

    public RegisteredUsersAddress() {
    }

    public RegisteredUsersAddress(Long id, String houseNo, String buildingNo, String street, String city, String state, String pincode) {
        this.id = id;
        this.houseNo = houseNo;
        this.buildingNo = buildingNo;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public RegisteredUsers getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUsers registeredUser) {
        this.registeredUser = registeredUser;
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
        if (!(object instanceof RegisteredUsersAddress)) {
            return false;
        }
        RegisteredUsersAddress other = (RegisteredUsersAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.myProject.entities.RegisteredUsersAddress[ id=" + id + " ]";
    }

}
