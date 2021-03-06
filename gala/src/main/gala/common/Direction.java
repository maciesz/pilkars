package main.gala.common;

import main.gala.exceptions.AmbiguousMoveException;

import java.io.Serializable;

/**
 * Klasa reprezentująca kierunek ruchu w postaci pary [-1, 0, 1][-1, 0, 1]
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class Direction implements Serializable {

    private static final long serialVersionUID = 12412412L;

    /**
     * Błąd graniczny dla ruchów po przekątnej.
     */
    private static final float MPE_DIAG = (float) 0.5;

    /**
     * Błąd graniczny dla ruchów góra/dół/lewo/prawo.
     */
    private static final float MPE_STRAIGHT = (float) 0.25;
    private int x = 0;
    private int y = 0;

    public Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Direction(float x, float y) throws AmbiguousMoveException {
        float absmax = Math.max(Math.abs(x), Math.abs(y));
        float dif = Math.abs(Math.abs(x) - Math.abs(y));
        float mpe_straight = absmax * MPE_STRAIGHT;
        float mpe_diag = absmax * MPE_DIAG;

        if (Math.abs(x) < mpe_straight) {
            this.x = 0;
            this.y = (int) Math.signum(y);
        } else if (Math.abs(y) < mpe_straight) {
            this.x = (int) Math.signum(x);
            this.y = 0;
        } else if (dif < mpe_diag) {
            this.x = (int) Math.signum(x);
            this.y = (int) Math.signum(y);
        } else {
            throw new AmbiguousMoveException();
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {

        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public String toString() {
        return "(" + getX() + ", " + getY() + ")\n";
    }
}
