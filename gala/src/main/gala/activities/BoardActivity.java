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
import main.gala.enums.Strategy;
import main.gala.exceptions.ImparitParameterException;
import main.gala.exceptions.InvalidGoalWidthException;
import main.gala.exceptions.UnknownGameModeException;
import main.gala.exceptions.UnknownStrategyException;
import main.gala.factories.ManagerFactory;

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

    /**
     * Tryb gry.
     */
    private GameMode gameMode;
    private SharedPreferences preferences;

    private int boardWidth;
    private int boardHeight;
    private int goalWidth;
    private Strategy strategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        getActionBar().hide();
        gameMode = GameMode.valueOf(getIntent().getStringExtra(GameSettings.GAME_MODE));

        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, GameSettings.DEFAULT_BOARD_WITH);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, GameSettings.DEFAULT_BOARD_HEIGHT);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, GameSettings.DEFAULT_GOAL_WIDTH);
        strategy = Strategy.valueOf(preferences.getString(GameSettings.STRATEGY, Strategy.RANDOM.name()));

        try {
            gameManager = ManagerFactory.createManager(gameMode).getInstance();
            gameManager.setStrategy(Strategy.RANDOM);
        } catch (UnknownGameModeException e) {
            e.printStackTrace();
        } catch (UnknownStrategyException e) {
            e.printStackTrace();
        }

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
