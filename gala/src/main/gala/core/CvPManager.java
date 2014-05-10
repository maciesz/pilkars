package main.gala.core;

import main.gala.common.Direction;
import main.gala.utils.Converter;
import main.gala.enums.GameState;
import main.gala.enums.PlayerType;

import java.util.LinkedList;
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

        }
        /**
         * Ustawiamy stan gry w widoku.
         */
        boardView.setGameState(chart.observer.rateActualState());


    }

    @Override
    public List<Direction> getComputerDirectionSeq() {
        /**
         * Jeśli przed rozpoczęciem ruchu jesteśmy zblokowani, to zwróć pustą listę.
         */
        if (chart.observer.rateActualState() == GameState.BLOCKED)
            return new LinkedList<>();

        /**
         * W przeciwnym wypadku poproś AI o ruch.
         */
        List<Direction> resList = ai.executeMoveSequence(chart);

        /**
         * Iterując po liście ruchów dla gracza komputerowego:
         * -> wykonaj pojedynczy ruch na planszy(patrz executeMove w klasie Chart),
         * -> oceń stan gry dla aktualnie rozpatrywanego pola,
         * -> po uprzedniej ocenie oznacz pole jako odwiedzone
         */
        GameState gameState = GameState.ACCEPTABLE;
        for (int i = 0; i < resList.size(); i++) {
            chart.executeSingleMove(resList.get(i));
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
//        boardView.setGameState(gameState);

        /**
         * Przekonwertuj kierunki na format rozpoznawalny przez Widok.
         */
        List<Direction> convertedList = new LinkedList<>();
        for (Direction direction: resList)
            convertedList.add(Converter.cuseMVConversion(direction));

        /**
         * Zwróć skonwertowaną listę kierunków.
         */
        return convertedList;
    }

    @Override
    public void startGame() {
        if (beginner == PlayerType.COMPUTER) {
            boardView.drawSequence(getComputerDirectionSeq());
            chart.observer.changeTurn();
        }
    }
}