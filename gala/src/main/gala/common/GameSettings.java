package main.gala.common;

import android.graphics.Color;

/**
 * Tymczasowa klasa przetrzymująca stałe pomocnicze używane w grze,
 * oraz nazwy preferencji w SharedPreferences.
 *
 * @author Maciej Andrearczyk
 */
public class GameSettings {


    /**
     * Abstrakcyjna wartość mówiąca o "gęstości" kratek na ekranie.
     */
    public static int CANVAS_SEPARATOR = 10;

    /**
     * Nazwa pliku z preferencjami.
     */
    public static final String PREF_NAME = "gamePreferences";

    /**
     * Nazwa preferencji określającej szerokość boiska.
     */
    public static final String BOARD_WIDTH = "BOARD_WIDTH";

    /**
     * Nazwa preferencji określającej wysokość boiska.
     */
    public static final String BOARD_HEIGHT = "HEIGHT";

    /**
     * Nazwa preferencji okreslającej szerokość bramki.
     */
    public static final String GOAL_WIDTH = "GOAL_WIDTH";

    /**
     * Nazwa extrasa przekazywanego jako parametr w Intentach.
     */
    public static final String GAME_MODE = "GAME_MODE";

    /**
     * Extras oznaczający czy gracz jest serwere czy klientem.
     */
    public static final String MULTI_MODE = "MULTI_MODE";

    /**
     * Nazwa preferencji określającej poziom trudności.
     */
    public static final String STRATEGY = "STRATEGY";

    /**
     * Nazwa preferencji określającej kolor górnego gracza.
     */
    public static final String TOP_PLAYER_COLOR = "TOP_PLAYER_COLOR";

    /**
     * Nazwa preferencji określającej kolor dolnego gracza.
     */
    public static final String BOTTOM_PLAYER_COLOR = "BOTTOM_PLAYER_COLOR";

    /**
     * Stała z domyślną szerokością boiska.
     */
    public static final int DEFAULT_BOARD_WIDTH = 8;

    /**
     * Stała z domyślną wysokością boiska.
     */
    public static final int DEFAULT_BOARD_HEIGHT = 10;

    /**
     * Stała z domyślną szerokością bramki.
     */
    public static final int DEFAULT_GOAL_WIDTH = 2;

    /**
     * Stała z domyślnym kolorem dla górnego gracza.
     */
    public static final int DEFAULT_TOP_PLAYER_COLOR = Color.rgb(0, 0, 112);

    /**
     * Stała z domyślnym kolorem dla dolnego gracza.
     */
    public static final int DEFAULT_BOTTOM_PLAYER_COLOR = Color.rgb(189, 183, 107);
}
