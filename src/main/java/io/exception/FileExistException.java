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
public class FileExistException extends ReaderException {
    public FileExistException(String name) {
        super("Impossible d'ouvrir le fichier : "+name,
                "Assurez-vous que ce fichier est present dans le repertoire");
    }
}
