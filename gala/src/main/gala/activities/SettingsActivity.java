package main.gala.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import main.gala.common.GameSettings;
import main.gala.common.StaticContent;
import main.gala.enums.Strategy;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
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

        setPreferences();
        setUI();
    }

    /**
     * Metoda do ustawiania istniejących preferencji.
     */
    private void setPreferences() {
        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
        strategy = preferences.getString(GameSettings.STRATEGY, Strategy.RANDOM.name());
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, GameSettings.DEFAULT_BOARD_WITH);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, GameSettings.DEFAULT_BOARD_HEIGHT);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, GameSettings.DEFAULT_GOAL_WIDTH);
    }

    /**
     * Metoda wywoływana, gdy user zechce zmienić poziom trudności.
     *
     * @param view widok przekazywany z góry
     */
    public void strategyDialog(View view) {
        final List<String> difficultyLevels = new ArrayList<>();
        for (Strategy s : Strategy.values()) {
            difficultyLevels.add(s.name().toLowerCase());
        }

        createSettingsDialog(difficultyLevels, GameSettings.STRATEGY);
    }

    /**
     * Metoda wywoływana przy kliknięciu na button z opcjami
     * dotyczącymi wyboru szerokości planszy.
     *
     * @param view widok przekazywany z góry
     */
    public void boardWidthDialog(View view) {
        final List<String> widths = new ArrayList<>();
        widths.add("8");
        widths.add("10");
        widths.add("12");
        widths.add("14");

        createSettingsDialog(widths, GameSettings.BOARD_WIDTH);
    }

    /**
     * Metoda wywoływana przy kliknięciu na button z opcjami
     * dotyczącymi wyboru wysokości planszy.
     *
     * @param view widok przekazywany z góry
     */
    public void boardHeightDialog(View view) {
        final List<String> heights = new ArrayList<>();
        heights.add("8");
        heights.add("10");
        heights.add("12");
        heights.add("14");

        createSettingsDialog(heights, GameSettings.BOARD_HEIGHT);
    }

    /**
     * Metoda wywoływana przy kliknięciu na button z opcjami
     * dotyczącymi wyboru szerokości boiska.
     *
     * @param view widok przekazywany z góry
     */
    public void goalWidth(View view) {
        final List<String> widths = new ArrayList<>();
        widths.add("2");
        widths.add("4");

        createSettingsDialog(widths, GameSettings.GOAL_WIDTH);
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
        elements.add((TextView) findViewById(R.id.board_height));
        elements.add((TextView) findViewById(R.id.board_width));
        elements.add((TextView) findViewById(R.id.goal_width));

        for (TextView textView : elements) {
            textView.setTypeface(puricaFont);
        }

        aiDifficultyText.setText(strategy.toLowerCase());
        boardHeightText.setText(String.valueOf(boardHeight));
        boardWidthText.setText(String.valueOf(boardWidth));
        goalWidthText.setText(String.valueOf(goalWidth));
    }

    /**
     * Odpowiada za utworzenie dialogu wyboru ustawień dla odpowiedniej preferencji.
     *
     * @param settings lista stringow z ustawieniami do wyboru
     * @param preferenceName nazwa preferencji do ustawienia
     */
    private void createSettingsDialog(final List<String> settings, final String preferenceName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(this, R.layout.item_text, R.id.puricaText);
        arrayAdapter.addAll(settings);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor preferencesEditor = preferences.edit();

                try {
                    int prefNumber = Integer.parseInt(settings.get(i));
                    preferencesEditor.putInt(preferenceName, prefNumber);
                } catch(NumberFormatException e) {
                    preferencesEditor.putString(preferenceName, settings.get(i).toUpperCase());
                }
                preferencesEditor.commit();

                setPreferences();
                setUI();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
