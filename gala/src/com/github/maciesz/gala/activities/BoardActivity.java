package com.github.maciesz.gala.activities;

import android.app.Activity;
import android.os.Bundle;
import com.github.maciesz.gala.core.AbstractManager;
import com.github.maciesz.gala.core.MockManager;

/**
 * Klasa aktywności planszy łącząca obiekty widoku z obiektami zarządającymi grą.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */

public class BoardActivity extends Activity {

    /**
     * Obiekt głównego zarządcy gry.
     */
    private AbstractManager gameManager;

    /**
     * Widok powiazany z aktywnością.
     */
    private BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        getActionBar().hide();

        boardView = (BoardView) findViewById(R.id.boardView);
        gameManager = new MockManager();
        gameManager.setView(boardView);
        boardView.setManager(gameManager);
    }
}
