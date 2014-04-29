package main.gala.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import main.gala.common.GameSettings;

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

    private int boardWidth;
    private int boardHeight;
    private int goalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().hide();
        setUI();

        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, GameSettings.DEFAULT_BOARD_WITH);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, GameSettings.DEFAULT_BOARD_HEIGHT);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, GameSettings.DEFAULT_GOAL_WIDTH);
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
    }

    /**
     * Metoda do ustawiania tych części UI, których nie można zrobić w xmlach, lub
     * nie umiem :)
     */
    private void setUI() {
        puricaFont = Typeface.createFromAsset(getAssets(), "fonts/purisa_bold.ttf");
        List<TextView> elements = new LinkedList<>();
        elements.add((TextView) findViewById(R.id.ai_button));
        elements.add((TextView) findViewById(R.id.ai_difficulty_text));
        elements.add((TextView) findViewById(R.id.board_height));
        elements.add((TextView) findViewById(R.id.board_height_text));
        elements.add((TextView) findViewById(R.id.board_width));
        elements.add((TextView) findViewById(R.id.board_width_text));
        elements.add((TextView) findViewById(R.id.goal_width));
        elements.add((TextView) findViewById(R.id.goal_width_text));

        for (TextView textView : elements) {
            textView.setTypeface(puricaFont);
        }
    }
}
