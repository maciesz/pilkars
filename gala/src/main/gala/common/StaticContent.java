package main.gala.common;

import android.graphics.Color;

/**
 * Klasa z wszelkimi stałymi używanymi gdzieś w kodzie, ale niebędącymi ustawieniami gry.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class StaticContent {

    /**
     * Kolor tła menu, dialogów itd.
     */
    public static final int backgroundColor = Color.parseColor("#FAF0D2");

    /**
     * Kolor tekstu.
     */
    public static final int textColor = Color.BLACK;

    /**
     * Lokalizacja domyślnej czcionki tekstu w resource.
     */
    public static final String textFontLocation = "fonts/purisa_bold.ttf";

}
