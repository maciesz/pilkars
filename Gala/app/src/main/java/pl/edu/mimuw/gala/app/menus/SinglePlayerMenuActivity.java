package pl.edu.mimuw.gala.app.menus;

import android.app.Activity;
import android.os.Bundle;

import pl.edu.mimuw.gala.app.R;

/**
 * Menu, które się wyświetli po kliknięciu "Single player".
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@students.edu.pl>
 */
public class SinglePlayerMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity.ac);
    }
}
