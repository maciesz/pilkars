package com.github.maciesz.gala.chart;

import com.github.maciesz.gala.common.Direction;
import com.github.maciesz.gala.enums.GameState;
import com.github.maciesz.gala.enums.Players;
import com.github.maciesz.gala.exceptions.ImparitParameterException;
import com.github.maciesz.gala.exceptions.InvalidGoalWidthException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
     * Zmienne całkowitoliczbowe opisujące planszę.
     */
    private int WIDTH;
    private int HEIGHT;
    private int GOAL_WIDTH;
    
    /**
     * Pozycja piłki na planszy.
     */
    private int boalPosition;
    
    /**
     * Struktura przechowująca dostępne krawędzie w grafie.
     */
    private Set<Integer> edges;

    /**
     * Klasa budująca planszę.
     */
    private class ChartBuilder {
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
            Set<Integer> chart = new HashSet<>();
            
            for (int k = 1; k<= HEIGHT; ++k) {
                final int lvlBase = k * (WIDTH + 1);
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
            List<Integer> list = new LinkedList<>();
            
            list.add(computeHash(WIDTH + 2, 2 * (WIDTH + 1))); // lewy-dolny
            list.add(computeHash(2 * WIDTH, (3 * (WIDTH + 1)) - 1)); // prawy-dolny
            list.add(computeHash((WIDTH + 1) * HEIGHT, ((WIDTH + 1) * (HEIGHT + 1)) + 1)); // lewy-górny
            list.add(computeHash(((WIDTH + 1) * (HEIGHT + 1)) - 1, ((WIDTH + 1) * (HEIGHT + 2)) - 2)); // prawy-górny
            
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
        private List<Integer> connectGoal
        (final int startIndex, final int pitchLevel, final int goalLevel) {
            List<Integer> list = new LinkedList<>();
            
            final int shift = (WIDTH - GOAL_WIDTH) / 2;
            final int directions = 5;
            
            /**
             * Podstawa połączenia.
             */
            for (int i = 0; i<= GOAL_WIDTH; ++i) {
                final int pos = (pitchLevel * (WIDTH + 1)) + shift + i;
                for (int k = 0; k< directions; ++k) {
                    int idx = (startIndex + k) % X_COORDS.length;
                    list.add(computeHash(pos, pos + X_COORDS[idx] + Y_COORDS[idx] * (WIDTH + 1)));
                }
            }
            
            /**
             * Połączenie z rogami bramki.
             */
            list.add(computeHash((pitchLevel * (WIDTH + 1)) + shift, 
                    (goalLevel * (WIDTH + 1)) + shift + 1)); // lewy
            list.add(computeHash(((pitchLevel + 1) * (WIDTH + 1)) - shift, 
                    (goalLevel + 1) * (WIDTH + 1)) - (shift + 1)); // prawy
            
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
     * Współczynnik wykorzystywany do obliczania hasha dla krawędzi nieskierowanej (x, y).
     * Hash(x, y) = MULTIPLIER * max(x, y) + min(x, y);
     */
    private static final int MULTIPLIER = 1000;
    
    
    //=========================================================================
    //
    // Klasy, zmienne i struktury publiczne
    //
    //=========================================================================    
    /**
     * Kierunki prawie według wskazówek zegara, bo zaczynamy od W(zachodu).
     * Jest to jeden z kluczowych elementów wpływających na poprawność budowy planszy.
     */
    public static final int[] X_COORDS = {-1, -1, 0, 1, 1, 1, 0, -1};
    public static final int[] Y_COORDS = {0, 1, 1, 1, 0, -1, -1, -1};

    /**
     * Maksymalna liczba różnych posunięć.
     */
    public static final int DIRECTIONS = 8;

    /**
     * Klasa przechowująca sekwencję ruchów gracza.
     */
    private class Deposit {
        
        //=====================================================================
        //
        // Zmienne i struktury pomocnicze
        //
        //=====================================================================
        /**
         * Struktura przechowująca ruchy zawodnika w zachowanej kolejności.
         */

        /*
         * private List<Integer> reservedEdges;
         *
         * Może się przydać kiedyś, jeśli będziemy chcieli umożliwiać cofanie ruchów.
         */

        private List<Direction> dirList;
        
        
        //=====================================================================
        //
        // Metody i procudury
        //
        //=====================================================================
        /**
         * Procedura rezerwująca ruch po krawędzi w grafie.
         *
         * @param direction kierunek, do poruszenia się
         */
        public void executeMove(final Direction direction) {
            final int startPosition = boalPosition;
            final int nextPosition = computeNext(direction);
            boalPosition = nextPosition;

            observer.markFinal(nextPosition);

            final int hash = computeHash(startPosition, nextPosition);
            /**
             * Usunięcie krawędzi z oryginalnego grafu i dodanie na stos zachcianek gracza.
             */
            edges.remove(hash);
            dirList.add(direction);
            //reservedEdges.add(hash);
        }
        
        /**
         * Funkcja zwracająca sekwencję ruchów gracza.
         * 
         * @return sekwencja ruchów gracza
         */
        public List<Direction> passMoveSequence() {
            return dirList;
        }
        
        /**
         * Procedura czyszcząca zawartość tymczasowego pojemnika.
         */
        public void clearContainer() {
            dirList.clear();
            //reservedEdges.clear();
        }
    }
    private Deposit depo;
    
    /**
     * Klasa pełniąca rolę obserwarota meczu.
     */
    public class Observer {
        
        //=====================================================================
        //
        // Zmienne i stałe struktury prywatne
        //
        //=====================================================================
        private final Map<Players, List<Integer>> winStates;
        
        /**
         * Zmienna wskazująca na tego spośród dwóch(domyślnie) zawodników,
         * który jest teraz przy piłce.
         */
        private Players player;
        
        /**
         * Struktura determinująca konieczność odbicia się z dowolnego pola na planszy.
         */
        private boolean[] visited;
    
        
        //=====================================================================
        //
        // Konstruktory
        //
        //=====================================================================
        public Observer() {
            /**
             * Lekko pogrubione oszacowanie na maksymalną liczbę stanów w grze.
             */
            final int maxStatesSize = (HEIGHT + 3) * (WIDTH + 1) + 1;
            
            /**
             * Inicjalizacja tablicy odwiedzonych.
             */
            visited = new boolean[maxStatesSize];
            
            /**
             * Instancjacja mapy.
             */
            winStates = new EnumMap<>(Players.class);
            
            /**
             * Inicjalizacja parametrów bramek oraz oddalenia słupka od linii autu.
             */
            final int shift = WIDTH - GOAL_WIDTH;
            final int bottomStart = 0;
            final int topStart = (WIDTH + 1) * (HEIGHT + 1);
            
            /**
             * Inicjalizacja mapy zwycięskimi stanami dla poszczególnych zawodników.
             */
            winStates.put(Players.BOTTOM, buildList(shift, topStart));
            winStates.put(Players.TOP, buildList(shift, bottomStart));
        }
        
        
        //=====================================================================
        //
        // Metody i procedury publiczne
        //
        //=====================================================================
        /**
         * Procedura odznaczająca gracza rozpoczynającego rozgrywkę.
         * 
         * @param player gracz rozpoczynający grę 
         */
        public void setPlayer(final Players player) {
            this.player = player;
        }
        
        /**
         * Procedura zmieniająca gracza posiadającego piłkę.
         */
        public void changeTurn() {
            player = getOtherPlayer();
        }

        /**
         * Procedura zwracająca aktualnie posiadającego piłkę gracza.
         *
         * @return aktualnie posiadający piłkę gracz.
         */
        public Players getCurrentPlayer() {
            return player;
        }
        
        /**
         * Funkcja zaznaczająca daną pozycję jako odwiedzoną.
         * 
         * @param position pozycja do odznaczenia
         */
        public void markFinal(final int position) {
            visited[position] = true;
        }
        
        /**
         * Funkcja oceniająca stan gry.
         * 
         * @return stan rozgrywki (typ GameState)
         */
        public GameState rateGameState() {
            
            final List<Integer> winList = winStates.get(player);
            final List<Integer> opponentWinList = winStates.get(getOtherPlayer());
            
            if (winList.contains(boalPosition))
                return GameState.VICTORIOUS;
            if (opponentWinList.contains(boalPosition))
                return GameState.DEFEATED;
            if (isBlocked(boalPosition)) // TODO!
                return GameState.BLOCKED;
            if (visited[boalPosition])
                return GameState.OBLIGATORY_MOVE;
            
            return GameState.ACCEPTABLE;
        }
        
        
        //=====================================================================
        //
        // Metody i procedury prywatne
        //
        //=====================================================================
        /**
         * Funkcja zwracająca pozycje wygrywające.
         * 
         * @param shift odległość słupka bramki od linii autowej
         * @param start pierwsza pozycja w rzędzie bramki
         * @return pozycje wygrywające
         */
        private List<Integer> buildList(final int shift, final int start) {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i<= GOAL_WIDTH; ++i)
                list.add(start + shift + i);
            
            return list;
        }
        
        /** 
         * @return bramka zajmowana przez przeciwnika 
         */
        private Players getOtherPlayer() {
            return (player == Players.BOTTOM) ? Players.TOP : Players.BOTTOM;
        }

        /**
         * @param boalPosition pozycja piłki na planszy
         * @return czy gracz został zablokowany przez przeciwnika
         */
        private boolean isBlocked(final int boalPosition) {
            // TODO: BFS do napisania
            return false;
        }

        /**
         * Procedura inicjująca pola, z których możliwe jest tylko odbicie.
         */
        private void initVisited() {
            /**
             * Odznacz linie autowe.
             */
            markOuterSide(WIDTH + 1); // odznacz lewą linię autową
            markOuterSide(2 * WIDTH - 1); // odznacz prawą linię autową
            
            /**
             * Odznacz pozycje na linii bramki.
             */
            final int shift = (WIDTH - GOAL_WIDTH) / 2;
            markGoalLine(1, shift); // linia przy dolnej bramce
            markGoalLine(HEIGHT + 1, shift); // linia przy górnej bramce
            
            /**
             * Odznacz środek boiska jako odwiedzony.
             */
            visited[boalPosition] = true;
        }
        
        /**
         * Procedura odznaczające linie autowe boiska jako stany tylko do odbicia.
         * 
         * @param startPos pozycja początkowa, od której przemieszczamy się już tylko w górę
         */
        private void markOuterSide(final int startPos) {
            for (int i = 0; i<= HEIGHT; ++i)
                visited[startPos + i * (WIDTH + 1)] = true;
        }
        
        /**
         * Procedura odznaczająca linie rzutów różnych jako stany tylko do odbicia.
         * 
         * @param goalLineLevel poziom linii na boisku (mierzony od 1 do HEIGHT + 1)
         * @param shift odległość słupka bramki od chorągiewki przy rzucie rożnym
         */
        private void markGoalLine(final int goalLineLevel, final int shift) {
            final int startPos = goalLineLevel * (WIDTH + 1);
            for (int i = 0; i<= shift; ++i) {
                visited[startPos + i] = true;
                visited[startPos + i + GOAL_WIDTH] = true;
            }
        }
    }
    public Observer observer;
    
    
    //=========================================================================
    //
    // Konstruktory
    //
    //=========================================================================
    public Chart() {
        /**
         * Depozyt oraz Obserwator powinni być zainicjowani po ustaleniu parametrów planszy.
         */
        edges = new HashSet<>();
        chartBuilder = new ChartBuilder();
        chartInspector = new ChartInspector();
        depo = new Deposit();
        observer = new Observer();
    }
    
    
    //=========================================================================
    //
    // Gettery i settery
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
        this.boalPosition = ((HEIGHT + 3) * (WIDTH + 1) - 1) / 2;
        
        observer.initVisited();
    }

    /**
     * Getter na pozycję piłki na boisku.
     * 
     * @return pozycja piłki na planszy
     */
    public int getBoalPosition() {
        return boalPosition;
    }
    
    /**
     * Setter na pozycję piłki na boisku.
     * 
     * @param boalPosition nowa pozycja piłki
     */
    public void setBoalPosition(final int boalPosition) {
        this.boalPosition = boalPosition;
    }
    
    
    //=========================================================================
    //
    // Funkcje i procedury publiczne
    //
    //=========================================================================

    /**
     * Procedura zlecająca wykonanie ruchu i przechowanie hasha dla krawędzi w depozycie.
     *
     * @param direction kierunek ruchu
     */
    public void executeSingleMove(Direction direction) {
        depo.executeMove(direction);
    }

    public List<Direction> executeMoveSequence() {
        List<Direction> moveSeq = depo.passMoveSequence();
        depo.clearContainer();
        observer.changeTurn();

        return moveSeq;
    }
    /**
     * Procuedra budująca planszę.
     */
    public void buildChart() {
        edges = chartBuilder.buildChart();
    }
    
    /**
     * @param direction kierunek, w którym chcę się poruszyć
     * @return czy da się przejść z pola start na pole next
     */
    public boolean isMoveLegal(Direction direction) {
        final int start = boalPosition;
        final int next =
                boalPosition + direction.getX() + (WIDTH + 1) * direction.getY();
        return edges.contains(computeHash(start, next));
    }
    
    /**
     * Funkcja zwracająca stan rozgrywki w danym momencie.
     * 
     * @return stan rozgrywki(zwycięstwo/porażka/blok/konieczny_ruch/akceptujący) 
     */
    public GameState rateGameState() {
        return observer.rateGameState();
    }
    
    
    //=========================================================================
    //
    // Funkcje i procedury prywatne
    //
    //=========================================================================

    /**
     * Funkcja wyznaczająca numer pola na planszy w kierunku direction
     *
     * @param direction kierunek
     * @return numer docelowego pola na planszy
     */
    private int computeNext(final Direction direction) {
        return boalPosition + direction.getX() + (WIDTH + 1) * direction.getY();
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
