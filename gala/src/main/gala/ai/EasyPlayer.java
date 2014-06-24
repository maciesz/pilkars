package main.gala.ai;

import main.gala.chart.Chart;
import main.gala.common.Direction;
import main.gala.enums.GameState;

import java.util.*;


public class EasyPlayer implements IArtificialIntelligence {
    public static final int[] X_COORDS = {0, -1, 1, 1, 1, -1, 0, -1};
    public static final int[] Y_COORDS = {-1, -1, -1, 1, 0, 1, 1, 0};
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
        int ballPosition = chart.getballPosition();
        int nextPosition;
        Direction direction;

        /**
         * Wyznacz sekwencję ruchów.
         */
        int edgeHash;
        boolean observerCondition;
        boolean visitedCondition;
        boolean usedEdgeCondition;
        boolean isMovePossible;
        while(true) {
            observerCondition = chart.observer.rateState(ballPosition) == GameState.OBLIGATORY_MOVE;
            visitedCondition = visited.contains(ballPosition);

            /**
             * Jeżeli wierzchołek nie był odwiedzony:
             * -> zanim zaczęła się tura komputera
             * lub
             * -> w ciągu kilku ruchów komputera wykonanych na początku,
             * to znaczy, że jest on wierzchołkiem końcowym - przerwij szukanie ścieżki.
             */
            if (!(observerCondition || visitedCondition))
                break;

            visited.add(ballPosition);
            /**
             * W przeciwnym wypadku, jeśli musimy wykonać ruch, to:
             */

            isMovePossible = false;
            for (int index = 0; index < chart.DIRECTIONS; index++) {
                direction = new Direction(X_COORDS[index], Y_COORDS[index]);
                nextPosition = chart.computeNext(ballPosition, direction);

                edgeHash = chart.computeHash(ballPosition, nextPosition);
                usedEdgeCondition = edges.contains(edgeHash);
                if (chart.isMoveLegal(ballPosition, nextPosition) && !usedEdgeCondition) {
                    moveSequence.add(direction);
                    ballPosition = nextPosition;
                    isMovePossible = true;
                    edges.add(edgeHash);
                    break;
                }
            }

            /**
             * Jeśli nie byliśmy w stanie wykonać żadnego ruchu, to blok.
             */
            if (!isMovePossible)
                break;
        }

        return moveSequence;
    }

    public IArtificialIntelligence getInstance() {
        return new EasyPlayer();
    }
}
