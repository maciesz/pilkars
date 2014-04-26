package main.gala.exceptions;

/**
 * Wyjątek zgłaszany przez StrategyFactory w przypadku próby zainicjowania
 * nieokreślonej strategii dla gracza komputerowego.
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class UnknownStrategyException extends BaseException {
    static {
        DEFAULT_MSG = "Attempt to create invalid Strategy";
    }
}
