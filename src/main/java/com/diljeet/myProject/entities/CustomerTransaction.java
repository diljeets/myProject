/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author diljeet
 */
@Entity
@Table(name="customerTransaction")
public class CustomerTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankName;
    private String bankTxnId;
    private String currency;
    private String gatewayName;
    private String orderId;
    private String paymentMode;
    private String respCode;
    private String respMsg;
    private String txnAmount;
    private String txnDate;
    private String txnId;

    public CustomerTransaction(){        
    }

    public CustomerTransaction(String orderId, String paymentMode, String respCode, String txnAmount) {
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.respCode = respCode;
        this.txnAmount = txnAmount;
    }
    
    public CustomerTransaction(String bankName, String bankTxnId, String currency, String gatewayName, String orderId, String paymentMode, String respCode, String respMsg, String txnAmount, String txnDate, String txnId) {
        this.bankName = bankName;
        this.bankTxnId = bankTxnId;
        this.currency = currency;
        this.gatewayName = gatewayName;
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.respCode = respCode;
        this.respMsg = respMsg;
        this.txnAmount = txnAmount;
        this.txnDate = txnDate;
        this.txnId = txnId;
    }
      
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankTxnId() {
        return bankTxnId;
    }

    public void setBankTxnId(String bankTxnId) {
        this.bankTxnId = bankTxnId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
   
    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
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
        if (!(object instanceof CustomerTransaction)) {
            return false;
        }
        CustomerTransaction other = (CustomerTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.myProject.entities.CustomerTransaction[ id=" + id + " ]";
    }

}
