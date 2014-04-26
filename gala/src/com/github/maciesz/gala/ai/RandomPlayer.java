package com.github.maciesz.gala.ai;

import com.github.maciesz.gala.chart.Chart;
import com.github.maciesz.gala.common.Direction;
import com.github.maciesz.gala.enums.GameState;

import java.util.*;

/**
 * Klasa opisująca losowo grającego przeciwnika komputerowego.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class RandomPlayer implements IArtificialIntelligence {
    //private Set<Integer> edges;
    
    public List<Direction> executeMoveSequence(final Chart chart) {
        //List<Direction> moveSequence = new LinkedList<>();
        List<Integer> indexList = new ArrayList<Integer>() {
            {
                for (int i = 0; i < chart.DIRECTIONS; ++i)
                    add(i);
            }};
        GameState state = chart.observer.rateGameState();

        while (state == GameState.OBLIGATORY_MOVE) {
            Collections.shuffle(indexList);
            for (Integer index: indexList) {
                final Direction direction = new Direction(chart.X_COORDS[index], chart.Y_COORDS[index]);
                if (chart.isMoveLegal(direction)) {
                    chart.executeSingleMove(direction);
                    break;
                }
            }
        }

        return chart.executeMoveSequence();
    }

    public IArtificialIntelligence getInstance() {
        return new RandomPlayer();
    }
}