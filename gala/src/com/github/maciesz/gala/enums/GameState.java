/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.maciesz.gala.enums;

/**
 * Możliwe stany rozgrywki.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public enum GameState {
    /**
     * Odpowiednio:
     * -> zwycięstwo
     * -> porażka
     * -> zablokowany przez przeciwnika
     * -> żaden z powyższych, konieczność wykonania ruchu
     * -> żaden z powyższych, stan w którym można zakończyć swój ruch.
     */
    VICTORIOUS, DEFEATED, BLOCKED, OBLIGATORY_MOVE, ACCEPTABLE;
}
