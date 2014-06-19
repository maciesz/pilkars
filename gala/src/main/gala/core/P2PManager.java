package main.gala.core;

import android.os.AsyncTask;
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
    private P2PStrategy p2PStrategy;

    public P2PManager() {
        super();
        ai = null;
        myMoves = new LinkedList<>();
    }

    @Override
    public AbstractManager getInstance() {
        return new P2PManager();
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

            new AsyncSendAndReceive().execute();
        } else {
            boardView.setGameState(chart.observer.rateActualState());
        }

    }

    @Override
    public void startGame() { //TODO zmienić, dodać, poprawić
        if (beginner == PlayerType.COMPUTER) {
            boardView.drawSequence(p2PStrategy.getOpponentMoves());
            chart.observer.changeTurn();
        }
    }

    @Override
    public void setMultiMode(MultiMode multiMode) {
        super.setMultiMode(multiMode);
        if (multiMode == MultiMode.SERVER) {
            p2PStrategy = new P2PStrategyServer(this);
        } else if (multiMode == MultiMode.CLIENT) {
            p2PStrategy = new P2PStrategyClient(this);
        }
    }

    private class AsyncSendAndReceive extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... aVoid) {
            p2PStrategy.sendMyMoves(myMoves);
            myMoves.clear();
            final List<Direction> resList = p2PStrategy.getOpponentMoves();
            boardView.drawSequence(resList);

            chart.observer.changeTurn();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            boardView.setGameState(chart.observer.rateActualState());
        }
    }
}
