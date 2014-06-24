package main.gala.core;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import main.gala.common.Direction;
import main.gala.enums.GameState;
import main.gala.enums.MultiMode;
import main.gala.utils.Converter;

import java.util.LinkedList;
import java.util.List;

/**
 * Zarządca rozgrywki dla trybu gry WiFi p2p.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class P2PManager extends AbstractManager {

    private static final String WAITING_FOR_OPPONENT = "Waiting for opponent move...";
    private static final String MAKE_A_MOVE = "Make a move";
    private static final String LOST_CONNECTION_MESSAGE = "Opponent player left";

    private List<Direction> myMoves; //moje ruchy
    private P2PStrategy p2PStrategy;
    private boolean opponentLeft = false;

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
        chart.observer.markFinal(chart.getballPosition());

        if (gameState == GameState.ACCEPTABLE) {
            chart.executeMoveSequence();
            chart.observer.changeTurn();
            new AsyncSendAndReceive().execute();
        } else if (gameState == GameState.VICTORIOUS ||
                   gameState == GameState.DEFEATED ||
                   gameState == GameState.BLOCKED) {
            new AsyncSend().execute();
        } else {
            boardView.setGameState(chart.observer.rateActualState());
        }

    }

    @Override
    public void startGame() {
        if (multiMode == MultiMode.CLIENT) {
            new AsyncReceive().execute();
        }
    }

    @Override
    public void setMultiMode(MultiMode multiMode) {
        super.setMultiMode(multiMode);
        boardView.setShouldShowMessage(false);
        if (multiMode == MultiMode.SERVER) {
            p2PStrategy = new P2PStrategyServer(this, multiMode);
            makeToastMakeMove();
            p2PStrategy.sendParameters(chart.getWidth(), chart.getHeight(), chart.getGoalWidth());
        } else if (multiMode == MultiMode.CLIENT) {
            p2PStrategy = new P2PStrategyClient(this, multiMode);
            makeToastWaiting();
        }
    }

    private void makeToastWaiting() {
        if (!opponentLeft) {
            Toast.makeText(parentActivity, WAITING_FOR_OPPONENT, Toast.LENGTH_SHORT).show();
        }
    }

    private void makeToastMakeMove() {
        if (!opponentLeft) {
            Toast.makeText(parentActivity, MAKE_A_MOVE, Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncSendAndReceive extends AsyncTask<Void, Void, Void> {

        List<Direction> resList;

        @Override
        protected Void doInBackground(Void... aVoid) {
            Log.d(this.getClass().getCanonicalName(), "AsyncSendAndReceive");
            p2PStrategy.sendMyMoves(myMoves);
            myMoves.clear();
            isUserEnabled = false;
            parentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    makeToastWaiting();
                }
            });
            resList = p2PStrategy.getOpponentMoves();
            parentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    makeToastMakeMove();
                }
            });
            isUserEnabled = true;
            publishProgress();

            chart.observer.changeTurn();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            boardView.setGameState(chart.observer.rateActualState());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d(this.getClass().getCanonicalName(), "AsyncSendAndReceive OnProgressUpdate");

            List<Direction> convertedList = new LinkedList<>(); //konwersja, aby chart zrozumiał
            for (Direction direction : resList) {
                convertedList.add(Converter.cuseMVConversion(direction));
            }

            for (int i = 0; i < convertedList.size(); i++) {
                chart.executeSingleMove(convertedList.get(i));
                chart.observer.markFinal(chart.getballPosition());
            }

            chart.executeMoveSequence();
            boardView.drawSequence(resList);
        }
    }

    private class AsyncReceive extends AsyncTask<Void, Void, Void> {

        List<Direction> resList;

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(this.getClass().getCanonicalName(), "AsyncReceive");

            isUserEnabled = false;
            resList = p2PStrategy.getOpponentMoves();
            isUserEnabled = true;
            publishProgress();
            chart.observer.changeTurn();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            boardView.setGameState(chart.observer.rateActualState());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d(this.getClass().getCanonicalName(), "AsyncSendAndReceive OnProgressUpdate");

            List<Direction> convertedList = new LinkedList<>(); //konwersja, aby chart zrozumiał
            for (Direction direction : resList) {
                convertedList.add(Converter.cuseMVConversion(direction));
            }

            for (int i = 0; i < convertedList.size(); i++) {
                chart.executeSingleMove(convertedList.get(i));
                chart.observer.markFinal(chart.getballPosition());
            }

            boardView.drawSequence(resList);
        }
    }

    private class AsyncSend extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... aVoid) {
            Log.d(this.getClass().getCanonicalName(), "AsyncSend");
            p2PStrategy.sendMyMoves(myMoves);
            chart.executeMoveSequence();
            chart.observer.changeTurn();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            boardView.setGameState(chart.observer.rateActualState());
            boardView.invalidate();
        }
    }

    @Override
    public void sendLostConnectionMessage() {
        opponentLeft = true;
        p2PStrategy.sendLostConnectionMessage();
    }

    @Override
    public void finishGame() {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                opponentLeft = true;
                Toast.makeText(parentActivity, LOST_CONNECTION_MESSAGE, Toast.LENGTH_LONG).show();
            }
        });
        parentActivity.finish();
    }
}
