package main.gala.core;

import main.gala.common.Direction;
import main.gala.enums.GameState;

import java.util.List;

/**
 * Główny zarządzający rozgrywką pomiędzy człowiekiem, a nieludzkim graczem.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class CvPManager extends AbstractManager {

    public CvPManager() {
        super();
    }

    /**
     * @return instancja zarządcy dla rogrywki typu człowiek - komputer.
     */
    @Override
    public AbstractManager getInstance() {
        return new CvPManager();
    }

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
        final GameState gameState = chart.observer.rateActualState();

        /**
         * Jeśli jesteśmy w stanie akceptującym(patrz enums/GameState), to:
         * -> zmieniamy kolejkę
         * -> czyścimy kontener przechowujący dotychczasowe ruchy
         * -> informujemy widok o zmianie zawodnika
         */
        if (gameState == GameState.ACCEPTABLE) {
            chart.observer.changeTurn();
            chart.executeMoveSequence();
            boardView.changePlayer();
        }

        /**
         * Ustawiamy stan gry w widoku.
         */
        boardView.setGameState(gameState);

        /**
         * Przekaż widokowi sekwencję ruchów gracza komputerowego,
         */
        boardView.drawSequence(getComputerDirectionSeq());
    }

    @Override
    public List<Direction> getComputerDirectionSeq() {
        List<Direction> resList = ai.executeMoveSequence(chart);

        /**
         * Automatyczne uaktualnienie stanu końcowego przy ostatnim wywołaniu execute'a.
         */
        for (Direction direction: resList) {
            chart.executeSingleMove(direction);
        }

        /**
         * Czyszczenie kontenera przechowującego dotychczasowe ruchy.
         */
        chart.executeMoveSequence();

        /**
         * Ustawiamy stan gry w widoku.
         */
        GameState gameState = chart.observer.rateActualState();
        boardView.setGameState(gameState);

        /**
         * Jeśli komputer zakończy swoją sekwencję na polu akceptującym(patrz enums/GameState),
         * to zmień zawodnika.
         */
        if (gameState == GameState.ACCEPTABLE)
            boardView.changePlayer();


        return resList;
    }
}