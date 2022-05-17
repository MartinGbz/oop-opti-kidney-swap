/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.exception;

/**
 *
 * @author mogier01
 */
public class ReaderException extends Exception {
    private String infos;
    private String conseils;

    public ReaderException(String infos, String conseils) {
        this.infos = infos;
        this.conseils = conseils;
    }

    @Override
    public String getMessage() {
        return "ERREUR : "+infos+"\n\t--> "+conseils;
    }
    
    
    
    
}
