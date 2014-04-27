package main.gala.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import main.gala.activities.R;

/**
 * Klasa reprezentująca główne menu i jego pochodne.
 * //TODO TODO TODO nic tu nie ma
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */

public class MainMenuActivity extends Activity {

    private Button viaPhoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getActionBar().hide();
    }

    /**
     * Metoda wywoływana, gdy użytkownik zdecyduje się na tryb gry single player.
     * Uruchamia aktywność z grą z opowiednimi parametrami.
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void singlePlayer(View view) {
//        Intent intent = new Intent(this, BoardActivity.class);
//        startActivity(intent);
    }

    /**
     * Metoda wywoływana, gdy użytkownik zdecyduje się na tryb gry multiplayer.
     * Uruchamia aktywność z grą z opowiednimi parametrami.
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void multiPlayer(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_multiplayer);
        dialog.setTitle("Choose game type");

        viaPhoneButton = (Button) dialog.findViewById(R.id.viaPhone);
        viaPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viaPhone(view);
                dialog.hide();
            }
        });

        dialog.show();
    }

    /**
     * Metoda wywoływana, gdy użytkownik zdecyduje się na tryb gry multiplayer
     * na telefonie.
     * Uruchamia aktywność z grą z opowiednimi parametrami.
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void viaPhone(View view) {
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }

    /**
     * Metoda wywoływana, gdy użytkownik zacznie dłubać w menu.
     * Uruchamia aktywność z grą z opowiednimi parametrami.
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void settings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}