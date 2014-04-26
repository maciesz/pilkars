package main.gala.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import main.gala.activities.R;
import main.gala.common.GameSettings;
import main.gala.core.AbstractManager;
import main.gala.core.MockManager;

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

    private SharedPreferences preferences;

    private int boardWidth;
    private int boardHeight;
    private int goalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        getActionBar().hide();
        gameManager = new MockManager();

        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, 8);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, 10);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, 2);

        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setManager(gameManager);
        boardView.setBoardHeight(boardHeight);
        boardView.setBoardWidth(boardWidth);
        boardView.setGoalWidth(goalWidth);

        gameManager.setView(boardView);
    }
}
