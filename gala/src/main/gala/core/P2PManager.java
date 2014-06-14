package main.gala.core;

import main.gala.common.Direction;
import main.gala.enums.GameState;
import main.gala.enums.MultiMode;
import main.gala.enums.PlayerType;

import java.util.LinkedList;
import java.util.List;

/**
 * Zarządca rozgrywki dla trybu gry WiFi p2p.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class P2PManager extends AbstractManager {

    private List<Direction> myMoves; //moje ruchy
    private P2PStrategy strategy;

    public P2PManager() {
        super();
        ai = null;
        if (multiMode == MultiMode.SERVER) {
            strategy = new P2PStrategyServer(this);
        } else if (multiMode == MultiMode.CLIENT) {
            strategy = new P2PStrategyClient(this);
        }
        myMoves = new LinkedList<>();
    }

    @Override
    public AbstractManager getInstance() {
        return new PvPManager();
    }

    @Override
    public void executeSingleMove(Direction direction) {
        chart.executeSingleMove(direction);
        myMoves.add(direction);

        final GameState gameState = chart.observer.rateActualState();
        chart.observer.markFinal(chart.getBoalPosition());

        if (gameState == GameState.ACCEPTABLE) {
            chart.executeMoveSequence();

            chart.observer.changeTurn();

            strategy.sendMyMoves(myMoves);
            myMoves.clear();
            final List<Direction> resList = strategy.getOpponentMoves();
            boardView.drawSequence(resList);

            chart.observer.changeTurn();
        }
        boardView.setGameState(chart.observer.rateActualState());


    }

    @Override
    public void startGame() { //TODO zmienić, dodać, poprawić
        if (beginner == PlayerType.COMPUTER) {
            boardView.drawSequence(strategy.getOpponentMoves());
            chart.observer.changeTurn();
        }
    }
}
