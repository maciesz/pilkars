package main.gala.core;

import main.gala.common.Direction;

import java.util.List;

/**
 * Abstrakcyjna strategia działania dla managera P2P.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public abstract class P2PStrategy {

    protected AbstractManager manager;

    public P2PStrategy(AbstractManager manager) {
        this.manager = manager;
    }

    public abstract void sendMyMoves(List<Direction> myMoves);

    public abstract List<Direction> getOpponentMoves();
}
