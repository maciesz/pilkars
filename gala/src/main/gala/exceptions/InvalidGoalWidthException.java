package main.gala.exceptions;

/**
 * Wyjątek zgłaszany, gdy szerokość bramki wynosi 0
 * albo gdy szerokość bramki jest co najmniej taka jak szerokość boiska.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class InvalidGoalWidthException extends BaseException {
    static {
        DEFAULT_MSG = "Attempt to set invalid goal width";
    }
}
