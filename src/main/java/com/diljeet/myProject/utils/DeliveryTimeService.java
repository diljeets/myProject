/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.utils;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author diljeet
 */
@Named
@ApplicationScoped
public class DeliveryTimeService {
    
    private final static List<String> hours;
    private final static List<String> minutes;
    private final static List<String> meridiems;
    
    static {
        hours = new ArrayList<>();
        hours.add("01");
        hours.add("02");
        hours.add("03");
        hours.add("04");
        hours.add("05");
        hours.add("06");
        hours.add("07");
        hours.add("08");
        hours.add("09");
        hours.add("10");
        hours.add("11");
        hours.add("12");
        
        minutes = new ArrayList<>();
        minutes.add("00");
        minutes.add("15");
        minutes.add("30");
        minutes.add("45");
        
        meridiems = new ArrayList<>();
        meridiems.add("AM");
        meridiems.add("PM");        
    }
    
    public List<String> getHours(){
        return hours;
    }
    
    public List<String> getMinutes(){
        return minutes;
    }
    
    public List<String> getMeridiems(){
        return meridiems;
    }
    
}
