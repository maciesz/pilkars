/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.gala.exceptions;

/**
 * Klasa abstrakcyjna wyjątku bazowego.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public abstract class BaseException extends Exception {
    protected static String DEFAULT_MSG;
    
    /**
     * Instancja wyjątku z domyślną wiadomością.
     */
    public BaseException() {
        super(DEFAULT_MSG);
    }

    /**
     * Instancja wyjątku z wyspecyfikowaną wiadomością.
     * 
     * @param msg wiadomość
     */
    public BaseException(String msg) {
        super(msg);
    }
}
