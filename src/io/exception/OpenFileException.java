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
public class OpenFileException extends ReaderException {

    public OpenFileException() {
        super("Impossible d'ouvrir le fichier", 
                "Assurez-vous que le nom du fichier est correctement rentré");
    }
    
}
