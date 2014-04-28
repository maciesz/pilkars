package main.gala.ai;

import main.gala.chart.Chart;
import main.gala.common.Direction;
import main.gala.enums.GameState;

import java.util.*;

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
        Set<Integer> edges = new HashSet<>();
        Set<Integer> visited = new HashSet<>();

        /**
         * Ustal pozycję piłki w grze i zadeklaruj zmienne pomocnicze.
         */
        int boalPosition = chart.getBoalPosition();
        int nextPosition;
        Direction direction = new Direction(0, 0);

        /**
         * Dodaj do odwiedzonych pozycję startową.
         */
        visited.add(boalPosition);

        /**
         * Wyznacz sekwencję ruchów.
         */
        int edgeHash;
        boolean observerCondition;
        boolean visitedCondition;
        boolean usedEdgeCondition;
        boolean isMovePossible;
        do {
            observerCondition = chart.observer.rateState(boalPosition) == GameState.OBLIGATORY_MOVE;
            visitedCondition = visited.contains(boalPosition);

            /**
             * Jeżeli wierzchołek nie był odwiedzony:
             * -> zanim zaczęła się tura komputera
             * lub
             * -> w ciągu kilku ruchów komputera wykonanych na początku,
             * to znaczy, że jest on wierzchołkiem końcowym - przerwij szukanie ścieżki.
             */
            if (!(observerCondition || visitedCondition))
                break;

            /**
             * W przeciwnym wypadku, jeśli musimy wykonać ruch, to:
             */
            Collections.shuffle(indexList);

            isMovePossible = false;
            for (int index : indexList) {
                direction.setX(chart.X_COORDS[index]);
                direction.setY(chart.Y_COORDS[index]);
                nextPosition = chart.computeNext(boalPosition, direction);

                edgeHash = chart.computeHash(boalPosition, nextPosition);
                usedEdgeCondition = edges.contains(edgeHash);
                if (chart.isMoveLegal(boalPosition, nextPosition) && !usedEdgeCondition) {
                    moveSequence.add(direction);
                    boalPosition = nextPosition;
                    isMovePossible = true;
                    visited.add(boalPosition);
                    edges.add(edgeHash);
                    break;
                }
            }

            /**
             * Jeśli nie byliśmy w stanie wykonać żadnego ruchu, to blok.
             */
            if (!isMovePossible)
                break;
        } while (true);


        return moveSequence;
    }

    public IArtificialIntelligence getInstance() {
        return new RandomPlayer();
    }
}
