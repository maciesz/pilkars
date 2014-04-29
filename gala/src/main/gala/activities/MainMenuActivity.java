package main.gala.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import main.gala.activities.R;
import main.gala.common.GameSettings;
import main.gala.enums.GameMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Klasa reprezentująca główne menu i jego pochodne.
 * //TODO TODO TODO nic tu nie ma
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */

public class MainMenuActivity extends Activity {

    private ImageButton viaPhoneButton;
    private ImageButton viaBluetoothButton;

    private Typeface puricaFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getActionBar().hide();
        setUI();
    }

    /**
     * Metoda do ustawiania tych części UI, których nie można zrobić w xmlach, lub
     * nie umiem :)
     */
    private void setUI() {
        puricaFont = Typeface.createFromAsset(getAssets(), "fonts/purisa_bold.ttf");
        List<TextView> elements = new LinkedList<>();
        elements.add((TextView) findViewById(R.id.singlePlayerButton));
        elements.add((TextView) findViewById(R.id.multiPlayerButton));
        elements.add((TextView) findViewById(R.id.settingsButton));

        for (TextView textView : elements) {
            textView.setTypeface(puricaFont);
        }
    }

    /**
     * Metoda wywoływana, gdy użytkownik zdecyduje się na tryb gry single player.
     * Uruchamia aktywność z grą z opowiednimi parametrami.
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void singlePlayer(View view) {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(GameSettings.GAME_MODE, GameMode.ComputerVsPlayer.name());
        startActivity(intent);
    }

    /**
     * Metoda wywoływana, gdy użytkownik zdecyduje się na tryb gry multiplayer.
     * Uruchamia aktywność z grą z opowiednimi parametrami.
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void multiPlayer(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_multiplayer);
        viaPhoneButton = (ImageButton) dialog.findViewById(R.id.viaPhone);
        viaPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viaPhone(view);
                dialog.hide();
            }
        });
        viaBluetoothButton = (ImageButton) dialog.findViewById(R.id.viaBluetooth);
        viaBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothDialog(view);
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
        intent.putExtra(GameSettings.GAME_MODE, GameMode.PlayerVsPlayer.name());
        startActivity(intent);
    }

    /**
     * Metoda odpowiadająca za wyświetlenie Dialogu z opcjami wyboru
     * trybów gry dla bluetooth (host, server).
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void bluetoothDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bluetooth);
        //TODO dodać listenerów na oba buttony, tj be host i be server

        dialog.show();
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