package main.gala.exceptions;

/**
 * Wyjątek zgłaszany przez ManagerFactory w przypadku próby stworzenia zarządcy 
 * dla rozgrywki nieokreślonego typu.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class UnknownGameModeException extends BaseException {
    static {
        DEFAULT_MSG = "Attempt to create Manager for non-existent game mode";
    }
}
