package main.gala.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import main.gala.common.GameSettings;
import main.gala.common.StaticContent;
import main.gala.core.AbstractManager;
import main.gala.enums.*;
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
    private int topPlayerColor;
    private int bottomPlayerColor;
    private Strategy strategy;

    private Typeface puricaFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        puricaFont = Typeface.createFromAsset(getAssets(), StaticContent.textFontLocation);
        setContentView(R.layout.activity_board);
        getActionBar().hide();
        gameMode = GameMode.valueOf(getIntent().getStringExtra(GameSettings.GAME_MODE));

        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, GameSettings.DEFAULT_BOARD_WIDTH);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, GameSettings.DEFAULT_BOARD_HEIGHT);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, GameSettings.DEFAULT_GOAL_WIDTH);
        topPlayerColor = preferences.getInt(GameSettings.TOP_PLAYER_COLOR, GameSettings.DEFAULT_TOP_PLAYER_COLOR);
        bottomPlayerColor = preferences.getInt(GameSettings.BOTTOM_PLAYER_COLOR, GameSettings.DEFAULT_BOTTOM_PLAYER_COLOR);
        strategy = Strategy.valueOf(preferences.getString(GameSettings.STRATEGY, Strategy.RANDOM.name()));

        createMainGameObjects();
    }


    /**
     * Zachowanie w przypadku kliknięcia przycisku "wstecz" na telefonie -
     * - wyświetlenie dialogu z zapytaniem czy na pewno chcemy zakonczyc.
     */
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        builder.setCustomTitle(inflater.inflate(R.layout.dialog_backbutton, null));
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Wałek, nic się nie dzieje :)
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BoardActivity.this.finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        setDialogFontAndColor(dialog);
    }

    /**
     * Metoda wywoływana, gdy gra zostanie zakończona. Zapytuje, czy chcemy rozegrać kolejną partię.
     *
     * @param gameState  stan gry
     * @param lastPlayer gracz, który ostatnio był przy ruchu
     */
    public void showEndGameDialog(GameState gameState, String lastPlayer) { //TODO wszystkie stałe stringowe przerzucić do R
        final String TOP_PLAYER_WINS = "Top player wins!";
        final String BOTTOM_PLAYER_WINS = "Bottom player wins";

        String msg;
        if (gameState == GameState.VICTORIOUS) {
            if (lastPlayer == StaticContent.TOP_PLAYER) {
                msg = TOP_PLAYER_WINS;
            }
            else {
                msg = BOTTOM_PLAYER_WINS;
            }
        } else if (gameState == GameState.DEFEATED) {
            if (lastPlayer == StaticContent.TOP_PLAYER) {
                msg = BOTTOM_PLAYER_WINS;
            }
            else {
                msg = TOP_PLAYER_WINS;
            }
        } else {
            msg = lastPlayer + " blocked!";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        builder.setCustomTitle(inflater.inflate(R.layout.dialog_endgame, null));
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BoardActivity.this.finish();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createMainGameObjects();
                boardView.reset();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(R.id.title);
        textView.setText(msg + " Do you want to play again?");

        setDialogFontAndColor(dialog);
    }

    /**
     * Ustawia czcionkę i kolory w dialogach.
     *
     * @param dialog alert dialog
     */
    private void setDialogFontAndColor(AlertDialog dialog) {
        TextView textView = (TextView) dialog.findViewById(R.id.title);
        textView.setTypeface(puricaFont);

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setTypeface(puricaFont);
        positiveButton.setBackgroundColor(StaticContent.backgroundColor);
        positiveButton.setTextColor(StaticContent.textColor);
        negativeButton.setTypeface(puricaFont);
        negativeButton.setBackgroundColor(StaticContent.backgroundColor);
        negativeButton.setTextColor(StaticContent.textColor);
    }

    /**
     * Metoda odpowiedzialna za utworzenie nowych obiektow w grze i odpowiednie ustawienie
     * ich parametrów.
     */
    private void createMainGameObjects() {
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
        boardView.setParentActivity(this);

        boardView.setTopPlayerColor(topPlayerColor);
        boardView.setBottomPlayerColor(bottomPlayerColor);

        boardView.reset();

        gameManager.setView(boardView);
        try {
            gameManager.setChart(boardWidth, boardHeight, goalWidth);
        } catch (ImparitParameterException e) {
            e.printStackTrace();
        } catch (InvalidGoalWidthException e) {
            e.printStackTrace();
        }
        gameManager.setPlayer(Players.BOTTOM);
        gameManager.setBeginnerType(PlayerType.PLAYER);
        gameManager.startGame();
    }
}
