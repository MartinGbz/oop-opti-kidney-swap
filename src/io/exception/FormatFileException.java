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
public class FormatFileException extends ReaderException {
    public FormatFileException(String formatLong, String formatCourt) {
        super("Impossible d'ouvrir le fichier",
                "Assurez-vous qu'il s'agit bien d'un fichier au format "+formatLong+" (."+formatCourt+")");
    }
}
