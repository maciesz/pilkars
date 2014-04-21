package com.github.maciesz.gala.chart;

import com.github.maciesz.gala.exceptions.ImparitParameterException;
import com.github.maciesz.gala.exceptions.InvalidGoalWidthException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Klasa planszy do gry.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class Chart {
    
    //=========================================================================
    //
    // Prywatne klasy, stałe i struktury pomocnicze instancji
    //
    //=========================================================================
    /**
     * Zmienne opisujące planszę.
     */
    private int WIDTH;
    private int HEIGHT;
    private int GOAL_WIDTH;
    
    /**
     * Struktura przechowująca dostępne krawędzie w grafie.
     */
    private Set<Integer> edges;
    
    /**
     * Struktura determinująca konieczność odbicia z konkretnego pola na planszy.
     */
    private boolean[] visited;
    
    /**
     * Klasa budująca planszę.
     */
    private final class ChartBuilder {
        private final int BOTTOM = 1;
        private final int TOP = HEIGHT + 1;
        private final int BOTTOM_GOAL = 0;
        private final int TOP_GOAL = HEIGHT + 2;
        
        /**
         * Funkcja budująca planszę do gry.
         * 
         * @return zbiór krawędzi opisujący planszę 
         */
        public Set<Integer> buildChart() {
            Set<Integer> chart = new HashSet<Integer>();
            
            for (int k = 1; k<= HEIGHT; ++k) {
                final int lvlBase = k * WIDTH;
                for (int pos = lvlBase + 1; pos< lvlBase + WIDTH - 1; ++pos)
                    for (int j = 0; j< X_COORDS.length; ++j)
                        chart.add(computeHash(pos, pos + X_COORDS[j] + Y_COORDS[j] * WIDTH));
            }
            
            /**
             * Podłączenie bramek i rogów boiska.
             */
            chart.addAll(connectCorners());
            chart.addAll(connectGoal(4, BOTTOM, BOTTOM_GOAL));
            chart.addAll(connectGoal(0, TOP, TOP_GOAL));
            
            return chart;
        }
        
        /**
         * Funkcja dobudowująca krawędzie na rogach planszy.
         * 
         * @return połączenia na rogach
         */
        private List<Integer> connectCorners() {
            List<Integer> list = new LinkedList<Integer>();
            
            list.add(computeHash(WIDTH + 1, 2 * WIDTH)); // lewy-dolny
            list.add(computeHash((2 * WIDTH) - 2, (3 * WIDTH) - 1)); // prawy-dolny
            list.add(computeHash(WIDTH * HEIGHT, (WIDTH * (HEIGHT + 1)) + 1)); // lewy-górny
            list.add(computeHash((WIDTH * (HEIGHT + 1)) - 1, (WIDTH * (HEIGHT + 2)) - 2)); // prawy-górny
            
            return list;
        }
        
        /**
         * Funkcja budująca połączenie bramki z boiskiem.
         * 
         * @param startIndex startowy indeks w tablicy kierunków
         * @param pitchLevel poziom murawy, który łączymy z bramką(numerujemy od 1 do (wysokość + 1))
         * @param goalLevel poziom bramki, z którą chcemy suę połączyć
         * @return krawędzie łączące bramkę z boiskiem
         */
        private List<Integer> connectGoal(final int startIndex, final int pitchLevel, final int goalLevel) {
            List<Integer> list = new LinkedList<Integer>();
            
            final int shift = (WIDTH - GOAL_WIDTH) / 2;
            final int directions = 5;
            
            /**
             * Podstawa połączenia.
             */
            for (int i = shift; i<= GOAL_WIDTH; ++i) {
                final int pos = (pitchLevel * WIDTH) + shift;
                for (int k = 0; k< directions; ++k) {
                    int idx = (startIndex + k) % X_COORDS.length;
                    list.add(computeHash(pos, pos + X_COORDS[idx] + Y_COORDS[idx] * WIDTH));
                }
            }
            
            /**
             * Połączenie z rogami bramki.
             */
            list.add(computeHash((pitchLevel * WIDTH) + shift, (goalLevel * WIDTH) + shift + 1)); // lewy
            list.add(computeHash(((pitchLevel + 1) * WIDTH) - shift, (goalLevel + 1) * WIDTH) - (shift + 1)); // prawy
            
            return list;
        }
    }
    private final ChartBuilder chartBuilder;
    
    /**
     * Klasa sprawdzająca poprawność parametrów całkowitoliczbowych opisujących planszę.
     */
    private class ChartInspector {
        public void checkChartParametres(final int width, final int height, final int goalWidth) 
                throws ImparitParameterException, InvalidGoalWidthException {
           /**
            * Sprawdź parzystość podanych argumentów.
            */
           final boolean widthParity = checkParameterParity(width);
           final boolean heightParity = checkParameterParity(height);
           final boolean goalWidthParity = checkParameterParity(goalWidth);

           if (!(widthParity && heightParity && goalWidthParity))
               throw new ImparitParameterException();

           /**
            * Oceń właściwość proporcji szerokości bramki do szerokości planszy.
            */
           final boolean goalToWidthProportion = 
                   checkGoalToWidthProportion(width, goalWidth);

           if (!goalToWidthProportion)
               throw new InvalidGoalWidthException();
        }
        
        /**
         * Funkcja sprawdzająca odpowiednią proporcję szerokości bramki do szerokości boiska.
         * 
         * @param width szerokość planszy
         * @param goalWidth szerokość bramki
         * @return czy powyższe proporcje są odpowiednie
         */
        private boolean checkGoalToWidthProportion(final int width, final int goalWidth) {
            return ((width >= goalWidth + 2) && (goalWidth >= 2));
        }

        /**
         * Funkcja sprawdzająca parzystość wskazanego parametru opisującego boisko.
         * 
         * @param parameter parametr całkowitoliczbowy opisujący boisko
         * @return czy rozpatrywany parametr jest parzysty
         */
        private boolean checkParameterParity(final int parameter) {
            return (parameter % 2 == 0);
        }
    }
    private final ChartInspector chartInspector;
    

    // ========================================================================
    //
    // Stałe [struktury i klasy] statyczne
    //
    // ========================================================================
    /**
     * Maksymalna liczba różnych posunięć.
     */
    private static final int DIRECTIONS = 8;
    
    /**
     * Współczynnik wykorzystywany do obliczania hasha dla krawędzi nieskierowanej (x, y).
     * Hash(x, y) = MULTIPLIER * max(x, y) + min(x, y);
     */
    private static final int MULTIPLIER = 1000;
    
    /**
     * Kierunki prawie według wskazówek zegara, bo zaczynamy od W(zachodu).
     * Jest to jeden z kluczowych elementów wpływających na poprawność budowy planszy.
     */
    private static final int[] X_COORDS = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static final int[] Y_COORDS = {0, 1, 1, 1, 0, -1, -1, -1};
    
    
    //=========================================================================
    //
    // Klasy, zmienne i struktury publiczne
    //
    //=========================================================================
    /**
     * Klasa przechowująca sekwencję ruchów gracza.
     */
    public class Deposit {
        
        //=====================================================================
        //
        // Zmienne i struktury pomocnicze
        //
        //=====================================================================
        /**
         * Struktura przechowująca ruchy zawodnika w zachowanej kolejności.
         */
        private List<Integer> reservedEdges;
        
        
        //=====================================================================
        //
        // Metody i procudury
        //
        //=====================================================================
        /**
         * Procedura rezerwująca ruch po krawędzi w grafie.
         * 
         * @param hash liczba jednoznacznie determinująca krawędź w grafie
         */
        public void reserveEdge(final int hash) {
            /**
             * Usunięcie krawędzi z oryginalnego grafu i dodanie na stos zachcianek gracza.
             */
            edges.remove(hash);
            reservedEdges.add(hash);
        }
        
        /**
         * Funkcja cofająca ostatni ruch.
         */
        public void undoEdge() {
            final int lastIndex = reservedEdges.size() - 1;
            final int edgeHash = reservedEdges.remove(lastIndex);
            
            edges.add(edgeHash);
        }
        
        /**
         * Funkcja zwracająca sekwencję ruchów gracza.
         * 
         * @return sekwencja ruchów gracza
         */
        public List<Integer> passMoveSequence() {
            return reservedEdges;
        }
        
        /**
         * Procedura czyszcząca zawartość tymczasowego pojemnika.
         */
        public void clearContainer() {
            reservedEdges.clear();
        }
    }
    public Deposit depo;
    
    //=========================================================================
    //
    // Konstruktory
    //
    //=========================================================================
    public Chart() {
        edges = new HashSet<Integer>();
        chartBuilder = new ChartBuilder();
        chartInspector = new ChartInspector();
    }
    
    
    //=========================================================================
    //
    // Funkcje i procedury
    //
    //=========================================================================
    /**
     * Procedura inicjująca parametry planszy.
     * 
     * @param width szerokość planszy
     * @param height wysokość planszy
     * @param goalWidth szerokość bramki(co najmniej 2, co najwyżej width - 2)
     * @throws ImparitParameterException
     * @throws InvalidGoalWidthException 
     */
    public void setChartParametres(final int width, final int height, final int goalWidth) 
            throws ImparitParameterException, InvalidGoalWidthException {
        
        chartInspector.checkChartParametres(width, height, goalWidth);
        /**
         * Set chart parametres
         */
        this.WIDTH = width;
        this.HEIGHT = height;
        this.GOAL_WIDTH = goalWidth;
    }
    
    /**
     * Procuedra budująca planszę.
     */
    public void buildChart() {
        edges = chartBuilder.buildChart();
    }
    
    /**
     * @param start pole początkowe
     * @param next pole końcowe
     * @return czy da się przejść z pola start na pole next
     */
    public boolean isMoveLegal(final int start, final int next) {
        return edges.contains(computeHash(start, next));
    }
    /**
     * Funkcja wyznaczająca liczbę reprezentującą połączenie między wierzchołkami start i next.
     * 
     * @param start wierzchołek, z którego zaczynamy ruch
     * @param next wierzchołek, do którego chcemy się przemieścić
     * @return wartość reprezentująca krawędź nieskierowaną (start, next)
     */
    private int computeHash(final int start, final int next) {
        return Math.max(start, next) * MULTIPLIER + Math.min(start, next);
    }
}
