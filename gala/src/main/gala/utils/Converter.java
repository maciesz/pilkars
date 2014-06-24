package main.gala.utils;

import main.gala.common.Direction;

/**
 * Klasa konwertująca dane przesyłane między Widokiem, a Managerem.
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl></m.szeszko@student.uw.edu.pl>
 */
public class Converter {
    /**
     * Funkcja konwertująca obiekty typu Direction pomiędzy Managerem a View
     * niezależnie od wyboru nadawcy i odbiorcy.
     *
     * @param direction kierunek ruchu wskazany przez zarządcę lub widok
     * @return kierunek akceptowalny przez adresata
     */
    public static Direction cuseMVConversion(Direction direction) {
        final int xCoord = direction.getX();
        final int yCoord = direction.getY();

        return new Direction(xCoord, yCoord * (-1));
    }

    /**
     * Konwertuje dane otrzymane od gracza online.
     * (zawsze jesteśmy dolnym graczem)
     *
     * @param direction
     * @return
     */
    public static Direction reverseXY(Direction direction) {
        final int x = direction.getX();
        final int y = direction.getY();

        return new Direction(x *(-1), y*(-1));
    }
}
