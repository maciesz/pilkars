package main.gala.ai;

import main.gala.chart.Chart;
import main.gala.common.Direction;
import main.gala.enums.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasa opisująca losowo grającego przeciwnika komputerowego.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class RandomPlayer implements IArtificialIntelligence {
    //private Set<Integer> edges;

    public List<Direction> executeMoveSequence(final Chart chart) {
        List<Direction> moveSequence = new LinkedList<>();
        List<Integer> indexList = new ArrayList<Integer>() {
            {
                for (int i = 0; i < chart.DIRECTIONS; ++i)
                    add(i);
            }};

        /**
         * Ustal pozycję piłki w grze i zadeklaruj zmienne pomocnicze.
         */
        int boalPosition = chart.getBoalPosition();
        int nextPosition;
        Direction direction = new Direction(0, 0);

        /**
         * Wyznacz sekwencję ruchów.
         */
        while (chart.observer.rateState(boalPosition) == GameState.OBLIGATORY_MOVE) {
            Collections.shuffle(indexList);

            boolean isMovePossible = false;
            for (Integer index: indexList) {
                direction.setX(chart.X_COORDS[index]);
                direction.setY(chart.Y_COORDS[index]);
                nextPosition = chart.computeNext(boalPosition, direction);

                if (chart.isMoveLegal(boalPosition, nextPosition)) {
                    moveSequence.add(direction);
                    boalPosition = nextPosition;
                    isMovePossible = true;
                    break;
                }
            }

            /**
             * Jeśli nie udało się poruszyć a byliśmy w stanie,
             * z którego musieliśmy wykonać ruch, to nastąpił blok.
             */
            if (!isMovePossible)
                break;
        }

        return moveSequence;
    }

    public IArtificialIntelligence getInstance() {
        return new RandomPlayer();
    }
}
