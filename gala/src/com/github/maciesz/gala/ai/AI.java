package com.github.maciesz.gala.ai;

import java.util.List;

/**
 * Interfejs sztucznej inteligencji
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public interface AI {
    public List<Integer> executeMoveSeq(Chart chart);
}
