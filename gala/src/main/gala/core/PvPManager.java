package main.gala.core;

import main.gala.activities.BoardView;
import main.gala.common.Direction;

import java.util.List;

/**
 * Główny zarządzający rozgrywką między CZŁOWIEKAMI.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class PvPManager extends AbstractManager {

    public PvPManager() {
        super();

        /**
         * Managera Player vs Player nie interesuje żadna sztuczna inteligencja.
         */
        ai = null;
    }

    /**
     * @return instancja zarządcy dla rozgrywki człowieków.
     */
    @Override
    public AbstractManager getInstance() {
        return new PvPManager();
    }

    /**
     * Manager PvP nie wspomaga gracza komputerowego.
     * @return nic
     */
    @Override
    public List<Direction> getComputerDirectionSeq() { return null; }
}