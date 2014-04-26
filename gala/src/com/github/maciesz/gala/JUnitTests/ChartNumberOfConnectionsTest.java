package com.github.maciesz.gala.JUnitTests;

import com.github.maciesz.gala.chart.Chart;
import com.github.maciesz.gala.exceptions.ImparitParameterException;
import com.github.maciesz.gala.exceptions.InvalidGoalWidthException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Testy na liczbę połączeń w planszy o podanych wymiarach.
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class ChartNumberOfConnectionsTest {
    @Test
    public void checkConnectionsNumber() throws InvalidGoalWidthException, ImparitParameterException {
        final int WIDTH = 4;
        final int HEIGHT = 4;
        final int GOAL_WIDTH = 2;

        Chart chart = new Chart();
        chart.setChartParametres(WIDTH, HEIGHT, GOAL_WIDTH);
        chart.buildChart();

        final int connects =
                countCrosses(WIDTH, HEIGHT) + countVerticalEdges(WIDTH, HEIGHT) +
                        countHorizontalEdges(WIDTH, HEIGHT) + countGoalsEdges(GOAL_WIDTH);

        assertEquals("Invalid edges number created by ChartBuilder!", connects, chart.getConnectsNumber());
    }

    /**
     * Funkcja zwracająca liczbę krzyży na podanej powierzchni.
     *
     * @param WIDTH szerokość boiska
     * @param HEIGHT wysokość boiska
     * @return liczba krzyży(krawędzi ,,na-krzyż'') na boisku bez bramek
     */
    private int countCrosses(final int WIDTH, final int HEIGHT) {
        return WIDTH * HEIGHT * 2;
    }

    /**
     * Funkcja zwracająca liczbę poziomych krawędzi na boisku bez bramek.
     *
     * @param WIDTH szerokość boiska
     * @param HEIGHT wysokość boiska
     * @return liczba poziomych krawędzi
     */
    private int countVerticalEdges(final int WIDTH, final int HEIGHT) {
        return (WIDTH - 1) * HEIGHT;
    }

    /**
     * Funkcja zwracająca liczbę pionowych krawędzi na boisku bez bramek.
     *
     * @param WIDTH szerokość boiska
     * @param HEIGHT wysokość boiska
     * @return liczba pionowych krawędzi
     */
    private int countHorizontalEdges(final int WIDTH, final int HEIGHT) {
        return (HEIGHT - 1) * WIDTH;
    }

    /**
     * Funkcja zwracająca 2 x (liczbę krawędzi ,,w okolicy'' bramki).
     *
     * @param GOAL_WIDTH szerokść bramki
     * @return 2 x (liczba krawędzi związana z bramką)
     */
    private int countGoalsEdges(final int GOAL_WIDTH) {
        return 2 * (4 + 2 + 1);
    }
}
