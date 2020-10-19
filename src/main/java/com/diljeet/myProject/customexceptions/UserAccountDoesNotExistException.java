/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.customexceptions;

/**
 *
 * @author diljeet
 */
public class UserAccountDoesNotExistException extends RuntimeException{

    public UserAccountDoesNotExistException(String message) {
        super(message);        
    }
       
}
