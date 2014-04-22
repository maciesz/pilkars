/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.github.maciesz.gala.exceptions;

/**
 * Wyjątek zgłaszany przy próbie ustawienia pewnego spośród wymiarów planszy,
 * bądź bramki na nieparzysty.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class ImparitParameterException extends BaseException {
    static {
        DEFAULT_MSG = "Attempt to create parametrized game chart with imparit parameter";
    }
}
