package main.gala.ai;

import main.gala.chart.Chart;
import main.gala.common.Direction;

import java.util.List;

/**
* Interfejs sztucznej inteligencji
*
* @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
*/
public interface IArtificialIntelligence {
    
    /**
    * Funkcja zwracająca ciąg następujących po sobie kierunków ruchu.
    *
    * @param chart plansza
    * @return lista kolejnych kierunków
    */
    List<Direction> executeMoveSequence(Chart chart);

    /**
     * Funkcja ułatwiająca tworzenie obiektów w fabryce Strategii.
     *
     * @return instancja klasy implementującej interfejs
     */
    IArtificialIntelligence getInstance();
}