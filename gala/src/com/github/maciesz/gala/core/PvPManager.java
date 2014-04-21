package com.github.maciesz.gala.core;

import com.github.maciesz.gala.common.Direction;

/**
 * Główny zarządzający rozgrywką między CZŁOWIEKAMI.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class PvPManager extends AbstractManager {
    
    /**
     * @return instancja zarządcy dla rozgrywki człowieków.
     */
    @Override
    public AbstractManager getInstance() {
        return new PvPManager();
    }

    @Override
    public boolean isMoveLegal(Direction direction) {
        return true;
    }
}