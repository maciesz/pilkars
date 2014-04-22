package com.github.maciesz.gala.core;

import android.view.View;
import com.github.maciesz.gala.ai.IArtificialIntelligence;
import com.github.maciesz.gala.chart.Chart;
import com.github.maciesz.gala.common.Direction;
import com.github.maciesz.gala.enums.Strategy;
import com.github.maciesz.gala.exceptions.ImparitParameterException;
import com.github.maciesz.gala.exceptions.InvalidGoalWidthException;
import java.util.List;

/**
 * Klasa abstrakcyjna głównego zarządcy gry.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public abstract class AbstractManager {
    
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
     * Depozyt - przechowuje sekwencję ruchów gracza.
     */
    protected Chart.Deposit depo;
    
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

    /**
     * Abstrakcyjny konstruktor zarządcy.
     */
    AbstractManager() {
        chart = new Chart();
        depo = chart.new Deposit();
        converter = new Converter();
        isUserEnabled = true;
    }

    /**
     * Funkcja odpowiadająca na pytania widoku w kwestii możliwości ruchu.
     * 
     * @param direction kierunek
     * @return czy ruch we wskazanym kierunku jest dozwolony
     */
    public abstract boolean isMoveLegal(Direction direction);

    public boolean isUserEnabled() {
        return isUserEnabled;
    }
    
    
    //=========================================================================
    //
    // Gettery
    //
    //=========================================================================
    /**
     * Funkcja zwracająca ciąg kierunków w które przemieszczał się gracz komputerowy.
     * 
     * @return wektor kierunków
     */
    public abstract List<Direction> getComputerDirectionSeq();
    
    /**
     * Funkcja wykorzystywana głównie we wzorcu projektowym fabryki zarządców,
     * zwraca instancję danej klasy.
     * 
     * @return instancja konkretnego zarządcy
     */
    public abstract AbstractManager getInstance();

    
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
     * @throws ImparitParameterException
     * @throws InvalidGoalWidthException 
     */
    public void setChart(final int width, final int height, final int goalWidth) 
            throws ImparitParameterException, InvalidGoalWidthException {
        chart.setChartParametres(width, height, goalWidth);
        chart.buildChart();
    }
    
    /**
     * Procedura inicjująca stretegię(w przypadku gracza komputerowego).
     * 
     * @param strategy poziom sztucznej inteligencji lub jej brak
     */
    public void setStrategy(Strategy strategy) {
        // Fabryka strategii
    }
    
    public void setUserEnabled(boolean isUserEnabled) {
        this.isUserEnabled = isUserEnabled;
    }
}
