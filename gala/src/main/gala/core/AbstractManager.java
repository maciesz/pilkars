package main.gala.core;

import android.view.View;
import main.gala.ai.IArtificialIntelligence;
import main.gala.chart.Chart;
import main.gala.common.Direction;
import main.gala.enums.Players;
import main.gala.enums.Strategy;
import main.gala.exceptions.ImparitParameterException;
import main.gala.exceptions.InvalidGoalWidthException;
import main.gala.exceptions.UnknownStrategyException;
import main.gala.factories.StrategyFactory;

import java.util.List;

/**
 * Klasa abstrakcyjna głównego zarządcy gry.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public abstract class AbstractManager {
    //=========================================================================
    //
    // Klasy i zmienne chronione
    //
    //=========================================================================
    /**
     * Klasa Converter odpowiadająca za konwersję danych przekazanych przez widok.
     */
    protected final class Converter {
        public int convertData(final Direction direction) {
            return 0;
        }
    }
    
    /**
     * Converter.
     */
    protected Converter converter;
    
    /**
     * Plansza.
     */
    protected Chart chart;
    
    /**
     * Strategia - sztuczna inteligencja.
     */
    protected IArtificialIntelligence ai;
    
    /**
     * Widok.
     */
    protected View boardView;
    
    /**
     * Pomocnicza zmienna zapobiegająca męczeniu widoku, 
     * gdy użytkownik nie ma obecnie ruchu, 
     * a klika jak wściekły i system na to reaguje.
     */
    protected boolean isUserEnabled;


    //=========================================================================
    //
    // Konstruktory
    //
    //=========================================================================
    /**
     * Abstrakcyjny konstruktor zarządcy.
     */
    AbstractManager() {
        chart = new Chart();
        //depo = chart.new Deposit();
        converter = new Converter();
        isUserEnabled = true;
    }


    //=========================================================================
    //
    // Publiczne metody abstrakcyjne
    //
    //=========================================================================
    /**
     * Funkcja wykorzystywana głównie we wzorcu projektowym fabryki zarządców,
     * zwraca instancję danej klasy.
     *
     * @return instancja konkretnego zarządcy
     */
    public abstract AbstractManager getInstance();


    //=========================================================================
    //
    // Publiczne metody
    //
    //=========================================================================
    /**
     * Funkcja zwracająca ciąg kierunków w których przemieszczał się gracz komputerowy.
     *
     * @return wektor kierunków
     */
    public List<Direction> getComputerDirectionSeq() {
        return ai.executeMoveSequence(chart);
    }

    /**
     * Funkcja odpowiadająca na pytania widoku w kwestii możliwości ruchu.
     *
     * @param direction kierunek
     * @return czy ruch we wskazanym kierunku jest dozwolony
     */
    public boolean isMoveLegal(Direction direction) {
        return chart.isMoveLegal(direction);
    }

    /**
     * Procedura odznaczająca ruch w zadanym kierunku.
     *
     * @param direction konkretny kierunek
     */
    public void executeSingleMove(Direction direction) {
        chart.executeSingleMove(direction);
    }

    /**
     * Procedura potwierdzająca zakończenie sekwencji ruchów.
     */
    public void executeMoveSequence() {
        chart.executeMoveSequence();
    }

    public boolean isUserEnabled() {
        return isUserEnabled;
    }


    //=========================================================================
    //
    // Settery
    //
    //=========================================================================
    /**
     * Procedura ustawiająca widok.
     * 
     * @param view widok planszy
     */
    public void setView(View view) {
        this.boardView = view;
    }
    
    /**
     * Procedura ustawiająca parametry fizyczne planszy.
     * 
     * @param width szerokość
     * @param height wysokość
     * @param goalWidth szerokość bramki
     * 
     * @throws main.gala.exceptions.ImparitParameterException
     * @throws main.gala.exceptions.InvalidGoalWidthException
     */
    public void setChart(final int width, final int height, final int goalWidth) 
            throws ImparitParameterException, InvalidGoalWidthException {
        chart.setChartParametres(width, height, goalWidth);
        chart.buildChart();
    }

    /**
     * Procedura inicjująca gracza rozpoczynającego rozgrywkę
     *
     * @param player gracz
     */
    public void setPlayer(final Players player) {
        chart.observer.setPlayer(player);
    }

    /**
     * Procedura inicjująca stretegię(w przypadku gracza komputerowego).
     * 
     * @param strategy poziom sztucznej inteligencji lub jej brak
     */
    public void setStrategy(Strategy strategy) throws UnknownStrategyException {
        ai = StrategyFactory.initializeStrategy(strategy);
    }
    
    public void setUserEnabled(boolean isUserEnabled) {
        this.isUserEnabled = isUserEnabled;
    }
}
