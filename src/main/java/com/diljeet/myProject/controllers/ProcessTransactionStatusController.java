/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.controllers;

import com.diljeet.myProject.utils.ProcessTransactionStatus;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author diljeet
 */
@Named(value = "processTransactionStatusController")
@SessionScoped
public class ProcessTransactionStatusController implements Serializable{
    
    private static final Logger logger = Logger.getLogger(ProcessTransactionStatusController.class.getCanonicalName());
    private static final long serialVersionUID = 1L;
    
    private ProcessTransactionStatus processTransactionStatus;
    private String resultMsg;

    public ProcessTransactionStatusController() {
    }

    public ProcessTransactionStatus getProcessTransactionStatus() {
        return processTransactionStatus;
    }

    public void setProcessTransactionStatus(ProcessTransactionStatus processTransactionStatus) {
        this.processTransactionStatus = processTransactionStatus;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }    
    
}
