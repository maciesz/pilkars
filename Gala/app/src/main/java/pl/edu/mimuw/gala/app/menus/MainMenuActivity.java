package pl.edu.mimuw.gala.app.menus;

import pl.edu.mimuw.gala.app.R;

import android.app.Activity;
import android.os.Bundle;


/**
 * Główne menu aplikacji.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

}
