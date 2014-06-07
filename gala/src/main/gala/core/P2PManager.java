package main.gala.core;

import main.gala.common.Direction;
import main.gala.enums.GameState;

import java.util.List;

/**
 * Zarządca rozgrywki dla trybu gry WiFi p2p.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class P2PManager extends AbstractManager {
    public P2PManager() {
        super();
        ai = null;
    }

    @Override
    public AbstractManager getInstance() {
        return new PvPManager();
    }

    @Override
    public List<Direction> getComputerDirectionSeq() {
        return null;
    }

    @Override
    public void executeSingleMove(Direction direction) {
        chart.executeSingleMove(direction);

        GameState gameState = chart.observer.rateActualState();
        chart.observer.markFinal(chart.getBoalPosition());

        if (gameState == GameState.ACCEPTABLE) {
            chart.observer.changeTurn();
            chart.executeMoveSequence();
            boardView.changePlayer();
            gameState = chart.observer.rateActualState();
        }


        boardView.setGameState(gameState);
    }
}
