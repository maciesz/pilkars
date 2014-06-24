package main.gala.chart;

import main.gala.common.Direction;
import main.gala.enums.GameState;
import main.gala.enums.Players;
import main.gala.exceptions.ImparitParameterException;
import main.gala.exceptions.InvalidGoalWidthException;

import java.util.*;

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
	private int ballPosition;

	/**
	 * Struktura przechowująca dostępne krawędzie w grafie.
	 */
	private Set<Integer> edges;

	/**
	 * Klasa budująca planszę.
	 */
	private class ChartBuilder {

        /**
         * Zbuduj planszę do gry.
         */
        public void buildChart() {
            constructCrossEdges(WIDTH, 1, HEIGHT, 0, WIDTH);
            constructVerticalEdges(WIDTH, 1, HEIGHT, 1, WIDTH);
            constructHorizontalEdges(WIDTH, 2, HEIGHT, 0, WIDTH);
            constructGoalEdges(GOAL_WIDTH, WIDTH, 0, 1);
            constructGoalEdges(GOAL_WIDTH, WIDTH, HEIGHT + 1, HEIGHT + 2);
        }
        /**
         * Budujemy krzyże na [boisku bez bramek]
         *
         * @param width szerokość boiska
         * @param sHeight wysokość, na której zaczynamy
         * @param fHeight wysokość, na której kończymy
         * @param lhs numer pozycji w wierszu, od której zaczynamy(numerujemy od 0 do (width - 1))
         * @param rhs numer pozycji w wierszu, na której kończymy(numerujemy od 1 do width)
         */
        private void constructCrossEdges
        (final int width, final int sHeight, final int fHeight, final int lhs, final int rhs) { //width, 1, height, 0, width
            for (int i = sHeight; i<= fHeight; ++i) {
                final int multiplier = i * (width + 1);

                /**
                 * Lewy-dolny do prawego-górnego.
                 */
                for (int j = lhs; j < rhs; ++j)
                    edges.add(computeHash(multiplier + j, multiplier + j + (width + 2)));

                /**
                 * Prawy-dolny do lewego-górnego
                 */
                for (int j = lhs + 1; j<= rhs; ++j)
                    edges.add(computeHash(multiplier + j, multiplier + j + width));
            }
        }

        /**
         * Budujem pionowe połączenia na [boisku bez bramek].
         * Konkretniej - budujemy krawędź idąc w kierunku do góry po krawędzi
         *
         * @param width szerokość boiska
         * @param sHeight wysokość, na której zaczynamy
         * @param fHeight wysokość, na której kończymy
         * @param lhs numer pozycji w wierszu, od której zaczynamy(numerujemy od 0 do (width - 1))
         * @param rhs numer pozycji w wierszu, na której kończymy(numerujemy od 1 do width)
         */
        private void constructVerticalEdges
        (final int width, final int sHeight, final int fHeight, final int lhs, final int rhs) { // width, 1, height, 1, width
            for (int i = sHeight; i<= fHeight; ++i) {
                final int multiplier = i * (width + 1);
                for (int j = lhs; j< rhs; ++j)
                    edges.add(computeHash(multiplier + j, multiplier + j + (width + 1)));
            }
        }

        /**
         * Budujemy poziome połączenia na [boisku bez bramek].
         * Konkretniej - budujemy krawędź idąc w kierunku na prawo po krawędzi.
         *
         * @param width szerokość boiska
         * @param sHeight wysokość, na której zaczynamy
         * @param fHeight wysokość, na której kończymy
         * @param lhs numer pozycji w wierszu, od której zaczynamy(numerujemy od 0 do (width - 1))
         * @param rhs numer pozycji w wierszu, na której kończymy(numerujemy od 1 do width)
         */
        private void constructHorizontalEdges
        (final int width, final int sHeight, final int fHeight, final int lhs, final int rhs) { // width, 2, height, 0, width
            for (int i = sHeight; i<= fHeight; ++i) {
                final int multiplier = i * (width + 1);
                for (int j = lhs; j< rhs; ++j)
                    edges.add(computeHash(multiplier + j, multiplier + j + 1));
            }
        }

        /**
         * Dobudowujemy bramkę i łączymy ją krawędziami z boskiem.
         *
         * @param goalWidth szerkość bramki
         * @param width szerokość boiska
         * @param sHeight wysokość, na której zaczynamy
         * @param fHeight wysokość, na której kończymy
         */
        private void constructGoalEdges
        (final int goalWidth, final int width, final int sHeight, final int fHeight) {
            final int shift = (width - goalWidth) / 2;
            final int lhs = shift;
            final int rhs = width - shift;

            constructCrossEdges(width, sHeight, sHeight, lhs, rhs);
            constructVerticalEdges(width, sHeight, sHeight, width / 2, (width / 2) + 1);

            switch (sHeight) {
                case 0:
                    constructHorizontalEdges(width, fHeight, fHeight, lhs, rhs);
                    break;
                default:
                    constructHorizontalEdges(width, sHeight, sHeight, lhs, rhs);
            }
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
	public static final int MULTIPLIER = 1000;

    /**
     * Kierunki prawie według wskazówek zegara, bo zaczynamy od W(zachodu).
     * Jest to jeden z kluczowych elementów wpływających na poprawność budowy planszy.
     */
    public static final int[] X_COORDS = {-1, -1, 0, 1, 1, 1, 0, -1};
    public static final int[] Y_COORDS = {0, 1, 1, 1, 0, -1, -1, -1};


	//=========================================================================
	//
	// Klasy, zmienne i struktury publiczne
	//
	//=========================================================================
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
        private List<Direction> dirList;


        //=====================================================================
        //
        // Konstruktory
        //
        //=====================================================================
        public Deposit() {
            dirList = new LinkedList<>();
        }


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
			final int startPosition = ballPosition;
			final int nextPosition = computeNext(direction);
			ballPosition = nextPosition;

			final int hash = computeHash(startPosition, nextPosition);

            /**
			 * Usunięcie krawędzi z oryginalnego grafu i dodanie na stos zachcianek gracza.
			 */
			edges.remove(hash);
			dirList.add(direction);
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
        private final boolean[] austereBoard;


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
            austereBoard = new boolean[maxStatesSize];

			/**
			 * Instancjacja mapy.
			 */
			winStates = new EnumMap<>(Players.class);

			/**
			 * Inicjalizacja parametrów bramek oraz oddalenia słupka od linii autu.
			 */
			final int shift = (WIDTH - GOAL_WIDTH) / 2;
			final int bottomStart = 0;
			final int topStart = (WIDTH + 1) * (HEIGHT + 2);

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
		 * Procedura zwracająca gracza aktualnie posiadającego piłkę.
		 *
		 * @return gracz aktualnie posiadający piłkę.
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
            //System.out.println("MARKED POS: " + position);
            visited[position] = true;
		}

		/**
		 * Funkcja oceniająca stan gry dla aktualnego wierzchołka.
		 *
		 * @return stan rozgrywki (typ GameState)
		 */
		public GameState rateActualState() {
            return rateGameState(ballPosition);
		}

        /**
         * Funkcja oceniająca stan gry dla wskazanego wierzchołka.
         *
         * @param state wierzchołek
         * @return stan gry dla wierzchołka 'state'
         */
        public GameState rateState(final int state) {
            return rateGameState(state);
        }

		//=====================================================================
		//
		// Metody i procedury prywatne
		//
		//=====================================================================
        /**
         * Funkcja oceniająca stan gry dla wskazanego wierzchołka.
         *
         * @param gameState wierzchołek
         * @return stan gry dla wierzchołka 'fameState'
         */
        private GameState rateGameState(final int gameState) {
            final List<Integer> winList = winStates.get(player);
            final List<Integer> opponentWinList = winStates.get(getOtherPlayer());

            if (winList.contains(gameState))
                return GameState.VICTORIOUS;
            if (opponentWinList.contains(gameState))
                return GameState.DEFEATED;
            if (isBlocked(gameState)) // TODO!
                return GameState.BLOCKED;
            if (visited[gameState])
                return GameState.OBLIGATORY_MOVE;

            //visited[gameState] = true;
            return GameState.ACCEPTABLE;
        }

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
		 * @param ballPosition pozycja piłki na planszy
		 * @return czy gracz został zablokowany przez przeciwnika
		 */
		private boolean isBlocked(final int ballPosition) {
            final int size = (WIDTH + 1) * (HEIGHT + 3) + 1;
			Queue<Integer> queue = new LinkedList<>();
            boolean[] queued = new boolean[size];
            Set<Integer> bEdges = new HashSet<>();

            /**
             * Inicjalizacja struktur danych.
             */
            System.arraycopy(austereBoard, 0, queued, 0, austereBoard.length);
            queued[ballPosition] = true;
            bEdges.addAll(edges);
            queue.add(ballPosition);

            /**
             * Deklaracja zmiennych oraz inicjalizacja stałych.
             */
            final int level = WIDTH + 1;
            int front;
            int next;
            int hash;
            Direction direction;
            boolean isMoveLegal;

            /**
             * Źródłem nazywamy wierzchołek startowy.
             *
             *
             * Definicje:
             *
             * Dobry wierzchołek:
             * Powiemy, że wierzchołek jest dobry,
             * jeżeli nie był on jeszcze odwiedzony w drzewie przeszukiwań,
             * nie jest przeznaczony tylko 'do-odbicia' oraz jest osiągalny ze źródła.
             *
             * Dobra ścieżka:
             * Ścieżka łącząca źródło z dobrym wierzchołkiem.
             *
             *
             * Chcąc sprawdzić, czy została założona blokada,
             * chcemy tak naprawdę stwierdzić czy istnieje dobry wierzchołek.
             * Jeśli dobry wierzchołek istnieje,
             * to leży on na pewnej najkrótszej ścieżce łączącej go ze źródłem.
             * Zauważmy również, że ruch w każdą z 8 stron świata kosztuje dokładnie tyle samo.
             *
             * Cel: Stwierdzić o istnieniu dobrej ścieżki.
             *
             * Do realizacji powyższego celu, w oparciu o podane wyżej założenia
             * wykorzystamy algorytm przeszukiwania BFS.
             */
            while (!queue.isEmpty()) {
                front = queue.remove();

                /**
                 * Jeżeli pozycja nie należy do tylko 'do-odbicia', to osiągnięto dobry wierzchołek.
                 */
                if (!visited[front])
                    return false;

                //System.out.println("Możliwe ruchy: ");
                for (int i = 0; i< X_COORDS.length; ++i) {
                    direction = new Direction(X_COORDS[i], Y_COORDS[i]);
                    next = computeNext(front, direction);
                    //System.out.println("Next: " + next);
                    hash = computeHash(front, next);
                    isMoveLegal = bEdges.contains(hash);

                    /**
                     * Jeżeli krawędź nie była jeszcze odwiedzona, a wierzchołek zakolejkowany...
                     */
                    if (isMoveLegal && !queued[next]) {
                        //System.out.println("(" + front + ", " + next + ")");
                        bEdges.remove(hash);
                        queued[next] = true;
                        queue.add(next);
                    }
                }
            }
			return true;
		}

		/**
		 * Procedura inicjująca pola, z których możliwe jest tylko odbicie.
		 */
		private void initVisited() {
			/**
			 * Odznacz linie autowe.
			 */
			markOuterSide(WIDTH + 1); // odznacz lewą linię autową
			markOuterSide((2 * (WIDTH + 1))- 1); // odznacz prawą linię autową

			/**
			 * Odznacz pozycje na linii bramki.
			 */
			final int shift = (WIDTH - GOAL_WIDTH) / 2;
			markGoalLine(1, shift); // linia przy dolnej bramce
			markGoalLine(HEIGHT + 1, shift); // linia przy górnej bramce

			/**
			 * Odznacz środek boiska jako odwiedzony.
			 */
			visited[ballPosition] = true;
		}

		/**
		 * Procedura odznaczające linie autowe boiska jako stany tylko do odbicia.
		 *
		 * @param startPos pozycja początkowa, od której przemieszczamy się już tylko w górę
		 */
		private void markOuterSide(final int startPos) {
            int pos;
			for (int i = 0; i<= HEIGHT; ++i) {
                pos = startPos + i * (WIDTH + 1);
                visited[pos] = true;
                austereBoard[pos] = true;
            }
		}

		/**
		 * Procedura odznaczająca linie rzutów różnych jako stany tylko do odbicia.
		 *
		 * @param goalLineLevel poziom linii na boisku (mierzony od 1 do HEIGHT + 1)
		 * @param shift odległość słupka bramki od chorągiewki przy rzucie rożnym
		 */
		private void markGoalLine(final int goalLineLevel, final int shift) {
			final int startPos = goalLineLevel * (WIDTH + 1);
            int posLhs;
            int posRhs;
			for (int i = 0; i<= shift; ++i) {
                posLhs = startPos + i;
                posRhs = startPos + i + shift + GOAL_WIDTH;

                visited[posLhs] = true;
                austereBoard[posLhs] = true;

				visited[posRhs] = true;
                austereBoard[posRhs] = true;
			}
		}


		public boolean[] getVisited() {
			// gosh...
			return visited;
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
	}


	//=========================================================================
	//
	// Gettery i settery

    public int getGoalWidth() {
        return GOAL_WIDTH;
    }

    public void setGOAL_WIDTH(int GOAL_WIDTH) {
        this.GOAL_WIDTH = GOAL_WIDTH;
    }

    //
	//=========================================================================
	/**
	 * Procedura inicjująca parametry planszy.
	 *
	 * @param width szerokość planszy
	 * @param height wysokość planszy
	 * @param goalWidth szerokość bramki(co najmniej 2, co najwyżej width - 2)
	 * @throws main.gala.exceptions.ImparitParameterException
	 * @throws main.gala.exceptions.InvalidGoalWidthException
	 */
	
	/*k*/
	public boolean[] getVisited()
	{
		return observer.getVisited();
	}
	
	public void setChartParametres(final int width, final int height, final int goalWidth)
			throws ImparitParameterException, InvalidGoalWidthException {

		chartInspector.checkChartParametres(width, height, goalWidth);
		/**
		 * Set chart parametres
		 */
		this.WIDTH = width;
		this.HEIGHT = height;
		this.GOAL_WIDTH = goalWidth;
		this.ballPosition = ((HEIGHT + 3) * (WIDTH + 1) - 1) / 2;

        observer = new Observer();
		observer.initVisited();
	}

	/* getery wymiarow - k */
	
	public int getWidth()
	{
		return WIDTH;
	}
	
	public int getHeight()
	{
		return HEIGHT;
	}
	/**
	 * Getter na pozycję piłki na boisku.
	 *
	 * @return pozycja piłki na planszy
	 */
	public int getballPosition() {
		return ballPosition;
	}

	/**
	 * Setter na pozycję piłki na boisku.
	 *
	 * @param ballPosition nowa pozycja piłki
	 */
	public void setballPosition(final int ballPosition) {
		this.ballPosition = ballPosition;
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

	public void executeMoveSequence() {
		//List<Direction> moveSeq = depo.passMoveSequence();
		depo.clearContainer();
		//observer.changeTurn();

		//return moveSeq;
	}
	/**
	 * Procudura budująca planszę.
	 */
	public void buildChart() {
        chartBuilder.buildChart();
	}

	/**
	 * @param direction kierunek, w którym chcę się poruszyć
	 * @return czy da się przejść we wskazanym kierunku
	 */
	public boolean isMoveLegal(Direction direction) {
		final int start = ballPosition;
		final int next =
				ballPosition + direction.getX() + (WIDTH + 1) * direction.getY();

		return (next > 0) ? edges.contains(computeHash(start, next)) : false;
	}

    /**
     * @param start wierzchołek startowy
     * @param next wierzchołek docelowy
     * @return czy da się przejść z pola start na pole next
     */
    public boolean isMoveLegal(final int start, final int next) {
        return (next > 0) ? edges.contains(computeHash(start, next)) : false;
    }

    /**
     * Funkcja wyznaczająca numer pola na planszy w kierunku direction
     *
     * @param direction kierunek
     * @return numer docelowego pola na planszy
     */
    public int computeNext(final Direction direction) {
        return ballPosition + direction.getX() + (WIDTH + 1) * direction.getY();
    }

    /**
     * Funkcja wyznaczająca numer pola na planszy w kierunku direction
     *
     * @param start pole startowe
     * @param direction kierunek
     * @return numer docelowego pola na planszy
     */
    public int computeNext(final int start, final Direction direction) {
        return start + direction.getX() + (WIDTH + 1) * direction.getY();
    }

	/**
	 * Funkcja zwracająca stan rozgrywki w danym momencie.
	 *
	 * @return stan rozgrywki(zwycięstwo/porażka/blok/konieczny_ruch/akceptujący)
	 */
	/*public GameState rateGameState() {
		return observer.rateActualState();
	}*/

    /**
     * Funkcja wyznaczająca liczbę reprezentującą połączenie między wierzchołkami start i next.
     *
     * @param start wierzchołek, z którego zaczynamy ruch
     * @param next wierzchołek, do którego chcemy się przemieścić
     * @return wartość reprezentująca krawędź nieskierowaną (start, next)
     */
    public int computeHash(final int start, final int next) {
        return Math.max(start, next) * MULTIPLIER + Math.min(start, next);
    }
    //=========================================================================
    //
    // Metody służące do testowania klasy Chart
    //
    //=========================================================================
	/**
	 * Getter na liczbę krawędzi na planszy.
	 *
	 * @return liczba krawędzi na planszy
	 */
	public int getConnectsNumber() {
		return edges.size();
	}

    /**
     * Getter na zbiór dostępnych krawędzi.
     *
     * @return zbiór dostępnych krawędzi
     */
    public Set<Integer> getEdges() { return edges; }


	//=========================================================================
	//
	// Funkcje i procedury prywatne
	//
	//=========================================================================
}
