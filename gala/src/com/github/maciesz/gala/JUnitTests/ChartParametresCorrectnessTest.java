package com.github.maciesz.gala.JUnitTests;

import com.github.maciesz.gala.chart.Chart;
import com.github.maciesz.gala.exceptions.ImparitParameterException;
import com.github.maciesz.gala.exceptions.InvalidGoalWidthException;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Test sprawdzający
 */
public class ChartParametresCorrectnessTest {
    /**
     * TestCase ze źle określoną wysokością planszy.
     *
     * @throws InvalidGoalWidthException
     * @throws ImparitParameterException
     */
    @Test(expected = ImparitParameterException.class)
    public void imparitHeightParity() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 6;
        final int height = 9;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, 2);
    }

    /**
     * TestCase ze źle dobraną szerokością bramki.
     *
     * @throws InvalidGoalWidthException
     * @throws ImparitParameterException
     */
    @Test(expected = ImparitParameterException.class)
    public void imparitGoalWidth() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 6;
        final int height = 6;
        final int goalWidth = 1;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, goalWidth);
    }

    /**
     * TestCase z szerokością bramki >= szerokości boiska.
     *
     * @throws InvalidGoalWidthException
     * @throws ImparitParameterException
     */
    @Test(expected = InvalidGoalWidthException.class)
    public void goalWidthEqualsChartWidthError() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 6;
        final int height = 6;
        final int goalWidth = 6;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, goalWidth);
    }

    /**
     * Plansza z właściwymi parametrami.
     *
     * @throws InvalidGoalWidthException
     * @throws ImparitParameterException
     */
    @Test
    public void correctChartParametres() throws InvalidGoalWidthException, ImparitParameterException {
        final int width = 6;
        final int height = 6;
        final int goalWidth = 2;

        Chart chart = new Chart();
        chart.setChartParametres(width, height, goalWidth);

        assertTrue("IncorrectChartParametres", true);
    }

}
