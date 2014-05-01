package main.gala.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.widget.TextView;
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
import org.w3c.dom.Text;


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
        Log.d(BoardActivity.class.getCanonicalName(), "ON CREATE()");
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
            gameManager.setStrategy(strategy);
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

    /**
     * Zachowanie w przypadku kliknięcia przycisku "wstecz" na telefonie -
     * - wyświetlenie dialogu z zapytaniem czy na pewno chcemy zakonczyc.
     * //TODO ladniej wizualnie
     */
    @Override
    public void onBackPressed() {
        Typeface puricaFont = Typeface.createFromAsset(getAssets(), "fonts/purisa_bold.ttf");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you really want to quit?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(SettingsActivity.class.getCanonicalName(), "NO " + String.valueOf(i));
                //Wałek, nic się nie dzieje :)
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(SettingsActivity.class.getCanonicalName(), "YES " + String.valueOf(i));
                        BoardActivity.this.finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(puricaFont);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(puricaFont);

    }
}
