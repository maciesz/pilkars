package com.github.maciesz.gala.JUnitTests;

import com.github.maciesz.gala.chart.Chart;
import com.github.maciesz.gala.common.Direction;
import com.github.maciesz.gala.exceptions.ImparitParameterException;
import com.github.maciesz.gala.exceptions.InvalidGoalWidthException;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Zestaw testów sprawdzających poprawność działania metody isMoveLegal.
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class ChartIsMoveLegalTest {
    private static final String MSG_WAS_LEGAL;
    private static final String MSG_WAS_ILLEGAL;

    static {
        MSG_WAS_LEGAL = "Legal move treated unfairly as illegal";
        MSG_WAS_ILLEGAL = "Illegal move treated as valid one";
    }

    /**
     * TestCase na ruch poza planszę.
     * Domyślna plansza 4x4 z bramką o szerokości 2.
     */
    @Test
    public void moveSequenceOutOfChart() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 4;
        final int height = 4;
        final int goalWidth = 2;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, goalWidth);
        chart.buildChart();

        Direction direction = new Direction(-1, 0);

        /**
         * <--
         */
        assertTrue(MSG_WAS_LEGAL, chart.isMoveLegal(direction));
        chart.executeSingleMove(direction);

        /**
         *    /
         *   /
         * |__
         *
         */
        direction.setX(-1);
        direction.setY(-1);
        assertTrue(MSG_WAS_LEGAL, chart.isMoveLegal(direction));
        chart.executeSingleMove(direction);

        /**
         * <--
         */
        direction.setX(-1);
        direction.setY(0);
        assertFalse(MSG_WAS_ILLEGAL, chart.isMoveLegal(direction));
    }

    /**
     * Test sprawdzający możliwośc przejścia dwa razy tą samą krawędzią.
     *
     * @throws InvalidGoalWidthException
     * @throws ImparitParameterException
     */
    @Test
    public void doubleUsageOfEdge() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 4;
        final int height = 4;
        final int goalWidth = 2;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, goalWidth);
        chart.buildChart();

        Direction direction = new Direction(1, 1);

        /**
         *  __
         *    |
         *  /
         * /
         *
         */
        //if(chart.isMoveLegal(direction))
            //System.out.println("DA SIE!");
        assertTrue(MSG_WAS_LEGAL, chart.isMoveLegal(direction));
        chart.executeSingleMove(direction);

        /**
         *  |
         *  |
         * \/
         *
         */
        direction.setX(0);
        direction.setY(-1);
        assertTrue(MSG_WAS_LEGAL, chart.isMoveLegal(direction));
        chart.executeSingleMove(direction);

        /**
         * <--
         */
        direction.setX(-1);
        direction.setY(0);
        assertTrue(MSG_WAS_LEGAL, chart.isMoveLegal(direction));
        chart.executeSingleMove(direction);

        /**
         * Gdyby się udało tak przejść, to znaczyłoby że przeszliśmy dwa razy tą samą krawędzią.
         *  __
         *    |
         *  /
         * /
         *
         */
        direction.setX(1);
        direction.setY(1);
        assertFalse(MSG_WAS_ILLEGAL, chart.isMoveLegal(direction));
    }
}
