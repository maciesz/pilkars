package main.gala.exceptions;

/**
 * Wyjątek rzucany, gdy ruch gracza palcem po planszy nie jest jednoznaczny.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class AmbiguousMoveException extends RuntimeException {
    public AmbiguousMoveException() {
    }
}
