/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.utils;

/**
 *
 * @author diljeet
 */
public class RedirectForm {
    private String actionUrl;
    private String method;
    private String type;
    private String contentType;
    private String md;
    private String paReq;
    private String termUrl;
    private String sbmttype;
    private String pid;
    private String es;

    public RedirectForm(){        
    }
    
    public RedirectForm(String actionUrl, String method, String type, String contentType, String md, String paReq, String termUrl) {
        this.actionUrl = actionUrl;
        this.method = method;
        this.type = type;
        this.contentType = contentType;
        this.md = md;
        this.paReq = paReq;
        this.termUrl = termUrl;
    }

    public RedirectForm(String actionUrl, String method, String type, String contentType, String md, String sbmttype, String pid, String es) {
        this.actionUrl = actionUrl;
        this.method = method;
        this.type = type;
        this.contentType = contentType;
        this.md = md;
        this.sbmttype = sbmttype;
        this.pid = pid;
        this.es = es;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getPaReq() {
        return paReq;
    }

    public void setPaReq(String paReq) {
        this.paReq = paReq;
    }

    public String getTermUrl() {
        return termUrl;
    }

    public void setTermUrl(String termUrl) {
        this.termUrl = termUrl;
    }

    public String getSbmttype() {
        return sbmttype;
    }

    public void setSbmttype(String sbmttype) {
        this.sbmttype = sbmttype;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }
    
    
}
