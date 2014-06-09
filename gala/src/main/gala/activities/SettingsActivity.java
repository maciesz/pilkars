package main.gala.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import main.gala.common.GameSettings;
import main.gala.common.StaticContent;
import main.gala.enums.Strategy;

import java.util.ArrayList;
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
    private int topPlayerColor;
    private int bottomPlayerColor;
    private static List<Integer> availableColors;

    static {
        availableColors = new LinkedList<>();
        availableColors.add(GameSettings.DEFAULT_TOP_PLAYER_COLOR);
        availableColors.add(GameSettings.DEFAULT_BOTTOM_PLAYER_COLOR);
        availableColors.add(Color.CYAN);
        availableColors.add(Color.MAGENTA);
        availableColors.add(Color.RED);
        availableColors.add(Color.GREEN);
        availableColors.add(Color.parseColor("#8B4513"));
    }

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
        boardWidth = preferences.getInt(GameSettings.BOARD_WIDTH, GameSettings.DEFAULT_BOARD_WIDTH);
        boardHeight = preferences.getInt(GameSettings.BOARD_HEIGHT, GameSettings.DEFAULT_BOARD_HEIGHT);
        goalWidth = preferences.getInt(GameSettings.GOAL_WIDTH, GameSettings.DEFAULT_GOAL_WIDTH);
        topPlayerColor = preferences.getInt(GameSettings.TOP_PLAYER_COLOR, GameSettings.DEFAULT_TOP_PLAYER_COLOR);
        bottomPlayerColor = preferences.getInt(GameSettings.BOTTOM_PLAYER_COLOR, GameSettings.DEFAULT_BOTTOM_PLAYER_COLOR);
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
     * Wywoływana po kliknięciu na button z opcjami
     * dotyczący koloru krechy górnego gracza.
     *
     * @param view widok przekazywany z góry
     */
    public void topPlayerColorDialog(View view) {
        List<Integer> colors = new LinkedList<>();
        for (int i = 0; i < availableColors.size(); i++) {
            int color = availableColors.get(i);
            if (color != bottomPlayerColor) {
                colors.add(color);
            }
        }

        createSettingsColorDialog(colors, GameSettings.TOP_PLAYER_COLOR);
    }

    /**
     * Wywoływana po kliknięciu na button z opcjami
     * dotyczący koloru krechy dolnego gracza.
     *
     * @param view widok przekazywany z góry
     */
    public void bottomPlayerColorDialog(View view) {
        List<Integer> colors = new LinkedList<>();
        for (int i = 0; i < availableColors.size(); i++) {
            int color = availableColors.get(i);
            if (color != topPlayerColor) {
                colors.add(color);
            }
        }

        createSettingsColorDialog(colors, GameSettings.BOTTOM_PLAYER_COLOR);
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
        TextView topPlayerColorText = (TextView) findViewById(R.id.top_player_color_text);
        TextView bottomPlayerColorText = (TextView) findViewById(R.id.bottom_player_color_text);


        List<TextView> elements = new LinkedList<>();
        elements.add((TextView) findViewById(R.id.ai_button));
        elements.add((TextView) findViewById(R.id.board_height));
        elements.add((TextView) findViewById(R.id.board_width));
        elements.add((TextView) findViewById(R.id.goal_width));
        elements.add((TextView) findViewById(R.id.top_player_color));
        elements.add((TextView) findViewById(R.id.bottom_player_color));

        for (TextView textView : elements) {
            textView.setTypeface(puricaFont);
        }

        aiDifficultyText.setText(strategy.toLowerCase());
        boardHeightText.setText(String.valueOf(boardHeight));
        boardWidthText.setText(String.valueOf(boardWidth));
        goalWidthText.setText(String.valueOf(goalWidth));

        topPlayerColorText.setTextColor(topPlayerColor);
        bottomPlayerColorText.setTextColor(bottomPlayerColor);
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

        dialog.getListView().setDivider(new ColorDrawable(Color.parseColor(StaticContent.DIVIDER_COLOR)));
        dialog.getListView().setDividerHeight(2);
    }

    /**
     * Odpowiada za utworzenie dialogu wyboru ustawień dla odpowiedniej preferencji(kolor krechy)
     *
     * @param colors możliwe kolory do wyboru dla danego gracza
     * @param preferenceName nazwa preferencji do ustawienia
     */
    private void createSettingsColorDialog(final List<Integer> colors, final String preferenceName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(this, R.layout.item_text, R.id.puricaText) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.puricaText);
                text.setTextColor(colors.get(position));

                return view;
            }
        };

        for (int i = 0; i < colors.size(); i++) {
            arrayAdapter.add("color");
        }

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor preferencesEditor = preferences.edit();

                preferencesEditor.putInt(preferenceName, colors.get(i));
                preferencesEditor.commit();

                setPreferences();
                setUI();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getListView().setDivider(new ColorDrawable(Color.parseColor(StaticContent.DIVIDER_COLOR)));
        dialog.getListView().setDividerHeight(2);
    }
}
