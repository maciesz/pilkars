package main.gala.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.github.maciesz.gala.activities.R;
import main.gala.common.GameSettings;

/**
 * Odpowiedzialność za sterowanie globalnymi ustawieniami gry, dostępnymi dla użytkownika.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class SettingsActivity extends Activity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().hide();

        preferences = getSharedPreferences(GameSettings.PREF_NAME, Activity.MODE_PRIVATE);
    }
}
