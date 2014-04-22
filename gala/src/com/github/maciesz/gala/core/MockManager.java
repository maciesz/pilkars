package com.github.maciesz.gala.core;

import android.util.Log;
import com.github.maciesz.gala.common.Direction;
import android.view.View;

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
    public AbstractManager getInstance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
