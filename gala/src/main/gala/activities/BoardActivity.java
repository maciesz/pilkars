package main.gala.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import main.gala.activities.R;
import main.gala.common.GameSettings;
import main.gala.core.AbstractManager;
import main.gala.core.MockManager;
import main.gala.core.PvPManager;
import main.gala.enums.GameMode;
import main.gala.enums.Players;
import main.gala.exceptions.ImparitParameterException;
import main.gala.exceptions.InvalidGoalWidthException;
import main.gala.exceptions.UnknownGameModeException;
import main.gala.factories.ManagerFactory;

/**
 * Klasa aktywności planszy łącząca obiekty widoku z obiektami zarządającymi grą.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */

public class BoardActivity extends Activity {

    private static final int DEFAULT_BOARD_WITH = 8;
    private static final int DEFAULT_BOARD_HEIGHT = 10;
    private static final int DEFAULT_GOAL_WIDTH = 2;

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

        try {
            gameManager = ManagerFactory.createManager(GameMode.PlayerVsPlayer);
        } catch (UnknownGameModeException e) {
            e.printStackTrace();
        }

        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, DEFAULT_BOARD_WITH);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, DEFAULT_BOARD_HEIGHT);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, DEFAULT_GOAL_WIDTH);

        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setManager(gameManager);
        boardView.setBoardHeight(boardHeight);
        boardView.setBoardWidth(boardWidth);
        boardView.setGoalWidth(goalWidth);

        gameManager.setView(boardView);
        try {
            gameManager.setChart(boardWidth, boardHeight, goalWidth);
        } catch (ImparitParameterException e) {
            e.printStackTrace();
        } catch (InvalidGoalWidthException e) {
            e.printStackTrace();
        }
        gameManager.setPlayer(Players.BOTTOM);
    }
}
