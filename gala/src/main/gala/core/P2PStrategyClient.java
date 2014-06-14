package main.gala.core;

import main.gala.common.Direction;

import java.util.List;

/**
 * Strategia działania na managera w przypadku gry jako klient.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class P2PStrategyClient extends P2PStrategy {
    public P2PStrategyClient(AbstractManager manager) {
        super(manager);
    }

    @Override
    public void sendMyMoves(List<Direction> myMoves) {

    }

    @Override
    public List<Direction> getOpponentMoves() {
        return null;
    }
}
