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
         * -> pozycja piłki zostaje zaktualizowana
         * -> ruch w postaci kierunku trafia na stos ruchów danej kolejki
         */
        chart.executeSingleMove(direction);

        /**
         * Oceń stan rozgrywki.
         * Oznacz wierzchołek jako tylko 'do-odbicia'.
         */
        final GameState gameState = chart.observer.rateActualState();
        chart.observer.markFinal(chart.getBoalPosition());

        /**
         * Jeśli jesteśmy w stanie akceptującym(patrz enums/GameState), to:
         * -> zmieniamy kolejkę
         * -> czyścimy kontener przechowujący dotychczasowe ruchy
         * -> informujemy widok o zmianie zawodnika
         */
        if (gameState == GameState.ACCEPTABLE) {
            /**
             * Wyczyść kontener w planszy odpowiedzialny za przechowywanie danych.
             */
            chart.executeMoveSequence();

            /**
             * Zaznacz zmianę kolejki w logice.
             */
            chart.observer.changeTurn();

            /**
             * Poproś o sekwencję ruchów komputera i przekaż widokowi, aby ją narysował.
             */
            final List<Direction> resList = getComputerDirectionSeq();
            boardView.drawSequence(resList);

            /**
             * Ponownie zmień kolejkę gracza.
             */
            chart.observer.changeTurn();

        } else {
            /**
             * Ustawiamy stan gry w widoku.
             */
            boardView.setGameState(gameState);
        }
    }

    @Override
    public List<Direction> getComputerDirectionSeq() {
        List<Direction> resList = ai.executeMoveSequence(chart);

        /**
         * Iterując po liście ruchów dla gracza komputerowego:
         * -> wykonaj pojedynczy ruch na planszy(patrz executeMove w klasie Chart),
         * -> oceń stan gry dla aktualnie rozpatrywanego pola,
         * -> po uprzedniej ocenie oznacz pole jako odwiedzone
         */
        GameState gameState = GameState.ACCEPTABLE;
        for (Direction direction: resList) {
            chart.executeSingleMove(direction);
            gameState = chart.observer.rateActualState();
            chart.observer.markFinal(chart.getBoalPosition());
        }

        /**
         * Czyszczenie kontenera przechowującego dotychczasowe ruchy.
         */
        chart.executeMoveSequence();

        /**
         * Ustawiamy stan gry w widoku.
         */
        boardView.setGameState(gameState);

        return resList;
    }
}