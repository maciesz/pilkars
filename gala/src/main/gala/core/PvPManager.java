package main.gala.core;

import main.gala.activities.BoardView;
import main.gala.common.Direction;
import main.gala.enums.GameState;

import java.util.List;

/**
 * Główny zarządzający rozgrywką między CZŁOWIEKAMI.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class PvPManager extends AbstractManager {

    public PvPManager() {
        super();

        /**
         * Managera Player vs Player nie interesuje żadna sztuczna inteligencja.
         */
        ai = null;
    }

    /**
     * @return instancja zarządcy dla rozgrywki człowieków.
     */
    @Override
    public AbstractManager getInstance() {
        return new PvPManager();
    }

    /**
     * Manager PvP nie wspomaga gracza komputerowego.
     * @return nic
     */
    @Override
    public List<Direction> getComputerDirectionSeq() { return null; }

    @Override
    public void executeSingleMove(Direction direction) {
        /**
         * Wykonujemy pojedynczy ruch.
         * Tak naprawdę pod spodem dzieje się co następuje:
         * -> krawędź zostaje zaznaczona jako 'odwiedzona'
         * -> wierzchołek w kierunku direction zostaje zamarkowany jako 'do odbicia'
         * -> pozycja piłki zostaje zaktualizowana
         * -> ruch w postaci kierunku trafia na stos ruchów danej kolejki
         */
        chart.executeSingleMove(direction);

        /**
         * Oceń stan rozgrywki.
         */
        GameState gameState = chart.observer.rateActualState();
        chart.observer.markFinal(chart.getBoalPosition());

        /**
         * Jeśli jesteśmy w stanie akceptującym(patrz enums/GameState), to:
         * -> zmieniamy kolejkę
         * -> czyścimy kontener przechowujący dotychczasowe ruchy
         * -> informujemy widok o zmianie zawodnika
         * -> ustalamy stan gry dla nowego zawodnika na nowym polu
         */
        if (gameState == GameState.ACCEPTABLE) {
            chart.observer.changeTurn();
            chart.executeMoveSequence();
            boardView.changePlayer();
            gameState = chart.observer.rateActualState();
        }

        /**
         * Ustawiamy stan gry w widoku(dla aktualnego gracza).
         */
        boardView.setGameState(gameState);
    }
}