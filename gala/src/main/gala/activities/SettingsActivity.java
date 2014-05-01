package main.gala.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import main.gala.common.GameSettings;
import main.gala.common.StaticContent;
import main.gala.enums.Strategy;

import java.util.LinkedList;
import java.util.List;

/**
 * Odpowiedzialność za sterowanie globalnymi ustawieniami gry, dostępnymi dla użytkownika.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class SettingsActivity extends Activity {

    private SharedPreferences preferences;
    private Typeface puricaFont;

    private String strategy;
    private int boardWidth;
    private int boardHeight;
    private int goalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().hide();

        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
        strategy = preferences.getString(GameSettings.STRATEGY, Strategy.RANDOM.name());
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, GameSettings.DEFAULT_BOARD_WITH);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, GameSettings.DEFAULT_BOARD_HEIGHT);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, GameSettings.DEFAULT_GOAL_WIDTH);

        setUI();
    }

    /**
     * Metoda wywoływana, gdy user zechce zmienić poziom trudności.
     *
     * @param view widok przekazywany z góry
     */
    public void ai(View view) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        builder.setView(findViewById(R.layout.dialog_bluetooth));
//        builder.setNeutralButton("chuj", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Log.d(SettingsActivity.class.getCanonicalName(), "CHUJ " + String.valueOf(i));
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getButton();
//        dialog.show();
    }

    /**
     * Metoda do ustawiania tych części UI, których nie można zrobić w xmlach, lub
     * nie umiem :)
     */
    private void setUI() {
        puricaFont = Typeface.createFromAsset(getAssets(), StaticContent.textFontLocation);

        TextView aiDifficultyText = (TextView) findViewById(R.id.ai_difficulty_text);
        TextView boardHeightText = (TextView) findViewById(R.id.board_height_text);
        TextView boardWidthText = (TextView) findViewById(R.id.board_width_text);
        TextView goalWidthText = (TextView) findViewById(R.id.goal_width_text);

        List<TextView> elements = new LinkedList<>();
        elements.add((TextView) findViewById(R.id.ai_button));
        elements.add(aiDifficultyText);
        elements.add((TextView) findViewById(R.id.board_height));
        elements.add(boardHeightText);
        elements.add((TextView) findViewById(R.id.board_width));
        elements.add(boardWidthText);
        elements.add((TextView) findViewById(R.id.goal_width));
        elements.add(goalWidthText);

        for (TextView textView : elements) {
            textView.setTypeface(puricaFont);
        }

        aiDifficultyText.setText(strategy);
        boardHeightText.setText(String.valueOf(boardHeight));
        boardWidthText.setText(String.valueOf(boardWidth));
        goalWidthText.setText(String.valueOf(goalWidth));
    }
}
