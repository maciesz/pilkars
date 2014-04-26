package com.github.maciesz.gala.core;

import android.util.Log;
import com.github.maciesz.gala.common.Direction;

import java.util.List;

/**
 * Prosta, tymczasowa implementacja Managera akceptująca każdy ruch gracza.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.uw.edu.pl>
 */
public class MockManager extends AbstractManager {
    public MockManager() {
        super();
    }

    @Override
    public boolean isMoveLegal(Direction direction) {
        Log.d(MockManager.class.getCanonicalName(), "isPropperMove()");
        return true;
    }

    @Override
    public void executeSingleMove(Direction direction) {}

    @Override
    public AbstractManager getInstance() {
        return new MockManager();
    }

    @Override
    public List<Direction> getComputerDirectionSeq() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
