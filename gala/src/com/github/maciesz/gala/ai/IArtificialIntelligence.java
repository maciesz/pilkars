package com.github.maciesz.gala.ai;

import com.github.maciesz.gala.chart.Chart;
import java.util.List;

/**
* Interfejs sztucznej inteligencji
*
* @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
*/
public interface IArtificialIntelligence {
    
    /**
    * Funkcja zwracająca ciąg następujących po sobie ruchów.
    *
    * @param chart plansza
    * @return lista kolejnych pozycji
    */
    List<Integer> executeMoveSequence(Chart chart);
}