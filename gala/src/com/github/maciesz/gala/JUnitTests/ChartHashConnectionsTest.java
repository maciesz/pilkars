package com.github.maciesz.gala.JUnitTests;

import com.github.maciesz.gala.chart.Chart;
import com.github.maciesz.gala.exceptions.ImparitParameterException;
import com.github.maciesz.gala.exceptions.InvalidGoalWidthException;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertTrue;

/**
 * Test sprawdzający poprawność krawędzi na planszy.
 * Domyślnie ustawiony dla planszy 4x4 z bramką o szerokości 2.
 * Funkcja computeHash powinna być identyczna jak w przypadku tej z klasy Chart.
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class ChartHashConnectionsTest {

    /**
     * Test sprawdzający poprawność hashy dla połączeń w domyślnej planszy.
     */
    @Test
    public void checkHashConnections() throws InvalidGoalWidthException, ImparitParameterException {
        /**
         * Struktura przechowująca wzorowo ohashowane krawędzie według fukcji computeHash.
         */
        Set<Integer> set = new HashSet<>();

        final int width = 4;
        final int height = 4;

        /**
         * 1) po skosie: lewy-dolny do prawego-górnego
         */
        set.add(computeHash(1, 7)); set.add(computeHash(2, 8));
        set.add(computeHash(26, 32)); set.add(computeHash(27, 33));
        for (int i = 1; i<= height; ++i) {
            final int multiplier = i * width;
            for (int j = 0; j< width; ++j) {
                set.add(computeHash(multiplier + j, multiplier + j + (width + 2)));
            }
        }

        /**
         * 2) po skosie: prawy-dolny do lewego-górnego
         */
        set.add(computeHash(2, 6)); set.add(computeHash(3, 7));
        set.add(computeHash(27, 31)); set.add(computeHash(28, 32));
        for (int i = 1; i<= height; ++i) {
            final int multiplier = i * width;
            for (int j = 1; j<= width; ++j) {
                set.add(computeHash(multiplier + j, multiplier + j + width));
            }
        }

        /**
         * 3) pionowe:
         */
        set.add(computeHash(2, 7));
        set.add(computeHash(27, 32));
        for (int i = 1; i<= height; ++i) {
            final int multiplier = i * width;
            for (int j = 1; j< width; ++j) {
                set.add(computeHash(multiplier + j, multiplier + j + width + 1));
            }
        }

        /**
         * 4) poziome:
         */
        set.add(computeHash(6, 7)); set.add(computeHash(7, 8));
        set.add(computeHash(26, 27)); set.add(computeHash(27, 28));
        for (int i = 2; i<= height; ++i) {
            final int multiplier = i * width;
            for (int j = 0; j< width; ++j) {
                set.add(computeHash(multiplier + j, multiplier + j + 1));
            }
        }

        /**
         * Inicjalizacja planszy.
         */
        Chart chart = new Chart();
        chart.setChartParametres(width, height, 2);
        chart.buildChart();

        /**
         * Sprawdzenie liczby krawędzi.
         */
        Set<Integer> chartSet = chart.getEdges();
        assertTrue("Incorrect number of edges in chart instance", chartSet.size() == set.size());

        /**
         * Czy identyczny zbiór przechowywanych wartości.
         */
        boolean decision = true;
        for (Integer edge: chartSet) {
            decision = set.contains(edge) ? true : false;
        }

        assertTrue("Chart instance does not contain required edges", decision);

    }

    /**
     * Funkcja wyznaczająca liczbę reprezentującą połączenie między wierzchołkami start i next.
     *
     * @param lhs wierzchołek, z którego zaczynamy ruch
     * @param rhs wierzchołek, do którego chcemy się przemieścić
     * @return wartość reprezentująca krawędź nieskierowaną (start, next)
     */
    private int computeHash(final int lhs, final int rhs) {
        return Math.max(lhs, rhs) * Chart.MULTIPLIER + Math.min(lhs, rhs);
    }
}
