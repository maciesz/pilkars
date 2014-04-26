package com.github.maciesz.gala.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.github.maciesz.gala.common.GameSettings;

/**
 * Klasa reprezentująca główne menu i jego pochodne.
 * //TODO TODO TODO nic tu nie ma
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getActionBar().hide();
    }

    public void singlePlayer(View view) {
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}