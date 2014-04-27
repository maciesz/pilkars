package main.gala.core;

import main.gala.common.Direction;

import java.util.List;

/**
 * Główny zarządzający rozgrywką pomiędzy człowiekiem, a nieludzkim graczem.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class CvPManager extends AbstractManager {

    public CvPManager() {
        super();
    }

    /**
     * @return instancja zarządcy dla rogrywki typu człowiek - komputer.
     */
    @Override
    public AbstractManager getInstance() {
        return new CvPManager();
    }

    @Override
    public List<Direction> getComputerDirectionSeq() {
        List<Direction> resList = ai.executeMoveSequence(chart);
        for (Direction direction: resList) {
            chart.executeSingleMove(direction);
        }

        /**
         * Czyszczenie kontenera przechowującego dotychczasowe ruchy.
         */
        chart.executeMoveSequence();

        /**
         * Ustawiamy stan gry w widoku.
         */
        /*
        TODO: Ustawić flagę ze stanem gry w widoku.
        view.setGameState(gameState); // coś takiego
         */
        
        return resList;
    }
}