package main.gala.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import main.gala.common.GameSettings;
import main.gala.common.StaticContent;
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

    private Button viaPhoneButton;
    private Button viaBluetoothButton;
    private Button viaWiFiButton;

    private Typeface puricaFont;
    private ProgressDialog wifiConnectProgressDialog;

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
        puricaFont = Typeface.createFromAsset(getAssets(), StaticContent.textFontLocation);
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
        if (isSDKWiFiReady()) {
            dialog.setContentView(R.layout.dialog_multiplayer_target);
        } else  {
            dialog.setContentView(R.layout.dialog_multiplayer);
        }

        viaPhoneButton = (Button) dialog.findViewById(R.id.viaPhone);
        viaPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viaPhone(view);
                dialog.hide();
            }
        });
//        viaBluetoothButton = (Button) dialog.findViewById(R.id.viaBluetooth);
//        viaBluetoothButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bluetoothDialog(view);
//                dialog.hide();
//            }
//        });
        if (isSDKWiFiReady()) {
            viaWiFiButton = (Button) dialog.findViewById(R.id.viaWiFi);
            viaWiFiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viaWifi(view);
                    dialog.hide();
                }
            });
        }

        dialog.show();

        ((TextView) dialog.findViewById(R.id.viaPhone)).setTypeface(puricaFont);
//        ((TextView) dialog.findViewById(R.id.viaBluetooth)).setTypeface(puricaFont);

        if (isSDKWiFiReady()) {
            ((TextView)dialog.findViewById(R.id.viaWiFi)).setTypeface(puricaFont);
        }
    }

    /**
     * Wywoływana, gdy użytkownik zdecyduje się na tryb gry multiplayer
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
     * trybów gry dla bluetooth (client, server).
     *
     * @param view widok przekazywany przez aplikacje
     */
    public void bluetoothDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bluetooth);
        //TODO dodać listenerów na oba buttony, tj be client i be server

        dialog.show();

        TextView tv = (TextView) dialog.findViewById(R.id.beClient);
        TextView tv2 = (TextView)dialog.findViewById(R.id.beServer);
        tv2.setTypeface(puricaFont);
        tv.setTypeface(puricaFont);
    }

    /**
     * Odpowiada za wyświetlenie dialogu z opcjami wyboru
     * trybów gry dla wifi (host, server).
     *
     * @param view widok przekazywany przez aplikacje
     */
    @Deprecated
    public void wifiDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bluetooth);

        Button beHostButton = (Button) dialog.findViewById(R.id.beClient);
        beHostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viaWifi(v);
                dialog.hide();
            }
        });

        Button beServerButton = (Button) dialog.findViewById(R.id.beServer);
        beServerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viaWifiAsServer(v);
                dialog.hide();
            }

        });

        dialog.show();

        TextView tv = (TextView) dialog.findViewById(R.id.beClient);
        TextView tv2 = (TextView)dialog.findViewById(R.id.beServer);
        tv2.setTypeface(puricaFont);
        tv.setTypeface(puricaFont);
    }

    /**
     * Metoda wywoływana, gdy użytkownik zdecyduje się wifi jako serwer.
     *
     * @param view widok przekazywany przez aplikacje
     */
    @Deprecated
    private void viaWifiAsServer(View view) {
        wifiConnectProgressDialog = ProgressDialog.show(this, "Waiting for connections...", "Please wait", true, true);
    }

    /**
     * Metoda wywoływana, gdy użytkownik zdecyduje się wifi jako klient.
     *
     * @param view widok przekazywany przez aplikacje
     */
    private void viaWifi(View view) {
        Intent intent = new Intent(this, WiFiActivity.class);
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

    /**
     * Zwraca informację o tym, czy wersja SDK na urządzeniu
     * potrafi obsłużyć WiFi direct.
     *
     * @return true jeżeli wersja androida jest >= 4.0 false wpp
     */
    private boolean isSDKWiFiReady() {
        return Build.VERSION.SDK_INT >= 14;
    }
}
