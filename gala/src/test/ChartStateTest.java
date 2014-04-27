package test;

import main.gala.chart.Chart;
import main.gala.enums.GameState;
import main.gala.enums.Players;
import main.gala.exceptions.ImparitParameterException;
import main.gala.exceptions.InvalidGoalWidthException;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Testy sprawdzające
 * Maciej Szeszko <m.szeszko@student.uw.edu.pl
 */
public class ChartStateTest {

    /**
     * Test sprawdzający domyślną planszą wejściową 4x4 z bramką o szerokości 2,
     * pod kątem poprawnie ustawionych pól 'do-odbicia'.
     */
    @Test
    public void checkVisitedChartStates() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 4;
        final int height = 4;
        final int goalWidth = 2;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, goalWidth);
        chart.buildChart();

        /**
         * Domyślnie rozpoczynającym grę jest gracz BOTTOM
         */
        chart.observer.setPlayer(Players.BOTTOM);
        List<Integer> visList = getInitiallyVisitedStates(width, height, goalWidth);

        boolean allFetched = true;
        int counter = 0;
        final int size = (width + 1) * (height + 3);
        for (int i = 0; i < size; ++i) {
            if (chart.observer.rateState(i) == GameState.OBLIGATORY_MOVE) {
                if (visList.contains(i))
                    ++counter;
                else {
                    allFetched = false;
                    break;
                }
            }
        }

        assertTrue("Invalid visited-initialization in ChartBuilder", allFetched && (counter == visList.size()));

    }

    /**
     * Test sprawdzający poprawne określenie stanów wygrywających i przegrywających dla gracza BOTTOM.
     * @throws InvalidGoalWidthException
     * @throws ImparitParameterException
     */
    @Test
    public void checkPlayersVictoryStates() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 6;
        final int height = 6;
        final int goalWidth = 4;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, goalWidth);
        chart.buildChart();

        final int shift = (width - goalWidth) / 2;
        final int bottomStartPos = shift;
        final int topStartPos = (height + 1) * (width + 2) + shift;
        List<Integer> bottomWinList = getVictoryStates(topStartPos, goalWidth);
        List<Integer> topWinList = getVictoryStates(bottomStartPos, goalWidth);

        /**
         * Sprawdź dla gracza BOTTOM.
         * Aby sprawdzić dla TOP należy zamienić listy w warunkach i ustawić gracza na Players.TOP.
         */
        chart.observer.setPlayer(Players.BOTTOM);

        boolean allFetchedWin = true;
        boolean allFetchedDefeat = true;
        int winStatesCounter = 0;
        int defeatStatesCounter = 0;
        final int size = (width + 1) * (height + 3);

        for (int i = 0; i< size; ++i) {
            if (chart.observer.rateState(i) == GameState.DEFEATED) {
                if (topWinList.contains(i))
                    ++defeatStatesCounter;
                else
                    allFetchedDefeat = false;
            } else if (chart.observer.rateState(i) == GameState.VICTORIOUS) {
                if (bottomWinList.contains(i))
                    ++winStatesCounter;
                else
                    allFetchedWin = false;
            }
        }

        final boolean decision = allFetchedDefeat && allFetchedWin
                && (winStatesCounter == defeatStatesCounter) &&  winStatesCounter == bottomWinList.size();
        assertTrue("Invalid player-states-initialization in ChartBuilder", decision);
    }

    /**
     * Funkcja zwraca zbiór zwycięskich stanów.
     *
     * @param startPos pierwsza pozycja typu zwycięskiego
     * @param goalWidth szerokość bramki
     * @return
     */
    private List<Integer> getVictoryStates(final int startPos, final int goalWidth) {
        List<Integer> list = new LinkedList<>();
        for (int i = startPos; i<= startPos + goalWidth; ++i)
            list.add(i);

        return list;
    }

    /**
     * Funkcja zwracająca zbiór pól będących zainicjowanych jako tylko 'do-odbicia'.
     *
     * @param width szerokość boiska
     * @param height długość boiska
     * @param goalWidth szerokość bramki
     * @return zbiór pól 'do-odbicia' only
     */
    private List<Integer> getInitiallyVisitedStates(final int width, final int height, final int goalWidth) {
        List<Integer> list = new LinkedList<>();
        int multiplier;

        /**
         * Linie autowe.
         */
        for (int i = 1; i<= height + 1; ++i) {
            multiplier = i * (width + 1);
            list.add(multiplier);
            list.add(multiplier + width);
        }

        /**
         * Linie bramkowe.
         */
        final int shift = (width - goalWidth) / 2;
        final int bottomGoalLevel = width + 1;
        final int topGoalLevel = (width + 1) * (height + 1);
        for (int i = 1, j = width -  1; i<= shift; ++i, --j) {
            list.add(bottomGoalLevel + i);
            list.add(bottomGoalLevel + j);

            list.add(topGoalLevel + i);
            list.add(topGoalLevel + j);
        }

        /**
         * Pozycja startowa.
         */
        list.add(((width + 1) * (height + 3) - 1) / 2);

        return list;
    }
}
