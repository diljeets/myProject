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
import java.util.logging.Logger;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "registeredUsers")
@Entity
@NamedQuery(name = "getAllUsers",
        query = "Select u from RegisteredUsers u")
public class RegisteredUsers implements Serializable {
    
    private static final Logger logger = Logger.getLogger(RegisteredUsers.class.getCanonicalName());

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    
    @NotBlank
    @NotEmpty    
    @Pattern(regexp = "^[A-Za-z]+(\\s\\w+)*$" , message="invalid name")
    private String name;
    
    @NotBlank
    @NotEmpty
    @Size(min = 10 , max = 10 , message = "Mobile should be a 10 digit number.")
    @Pattern(regexp = "^[0-9]+$" , message="invalid mobile")
    private String mobile;
    
    @NotBlank
    @NotEmpty
    @Email(message = "Not a valid Email Id.")
    private String username;
    
    @NotNull
    @Size(min = 8 , max = 15 , message = "Password can be 8-15 characters in length")
    @Pattern(regexp = "^[A-Za-z0-9!@#$%&]+$" , message="invalid password")
    private String password;    
    
    @Transient
    private String confirmPassword;
    
    private String role;
    
    private String salt;   
    
    private String isActive;
    
    private String isPasswordChangeRequest;
    
    private Date dateCustomerCreated;
    
    private Date dateCustomerLastUpdated;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "registeredUser")
    @JsonbTransient
//    @XmlTransient
    private List<RegisteredUsersAddress> addresses = new ArrayList<>();

    public RegisteredUsers() {        

    }       

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }   

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }  

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }    

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsPasswordChangeRequest() {
        return isPasswordChangeRequest;
    }

    public void setIsPasswordChangeRequest(String isPasswordChangeRequest) {
        this.isPasswordChangeRequest = isPasswordChangeRequest;
    }    

    public Date getDateCustomerCreated() {
        return dateCustomerCreated;
    }

    public void setDateCustomerCreated(Date dateCustomerCreated) {
        this.dateCustomerCreated = dateCustomerCreated;
    }

    public Date getDateCustomerLastUpdated() {
        return dateCustomerLastUpdated;
    }

    public void setDateCustomerLastUpdated(Date dateCustomerLastUpdated) {
        this.dateCustomerLastUpdated = dateCustomerLastUpdated;
    }

    public List<RegisteredUsersAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<RegisteredUsersAddress> addresses) {
        this.addresses = addresses;
    }
      
    public void addAddress(RegisteredUsersAddress address) {
        addresses.add(address);
        address.setRegisteredUser(this);
    }

    public void removeAddress(RegisteredUsersAddress address) {
        addresses.remove(address);
        address.setRegisteredUser(this);
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
        if (!(object instanceof RegisteredUsers)) {
            return false;
        }
        RegisteredUsers other = (RegisteredUsers) object;
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
