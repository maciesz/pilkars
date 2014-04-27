package main.gala.ai;

import main.gala.chart.Chart;
import main.gala.common.Direction;

import java.util.List;

/**
* Interfejs sztucznej inteligencji.
*
* @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
*/
public interface IArtificialIntelligence {
    
    /**
    * Funkcja zwracająca sekwencję kierunków(obiektów typu Direction)
    * jako propozycję ruchu dla gracza komputerowego.
    * To w gestii klasy pochodnej Abstract Managera leży wykonanie tych ruchów,
    * czyli wykonanie sekwencji executeSingleMove'ów na obiekcie plansza(typ Chart).
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